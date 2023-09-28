import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import di.Inject
import drag_and_drop.column.DragDropColumn
import drag_and_drop.draggable_in_window.DraggableItemInWindow
import drag_and_drop.draggable_in_window.LocalDragItemStateInFullWindow
import drag_and_drop.row.DragDropRow
import drag_and_drop_old.DropTarget
import drag_and_drop_old.LocalDragTargetInfo
import drag_and_drop_old.LongPressDraggable
import models.TaskItem
import task_creation_block.TaskCreationBlock


@Composable
fun KanbanBoard() {
    val kanbanViewModel = remember { Inject.instance<KanbanViewModel>() }
    val kanbanList by kanbanViewModel.kanbanList.collectAsState()
    val defaultKanbanCardColor = ApplicationTheme.colors.cardBackgroundDark
    val kanbanBackgroundColor = remember { mutableStateOf(defaultKanbanCardColor) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ApplicationTheme.colors.mainBackgroundColor
    ) {
        LongPressDraggable(modifier = Modifier.fillMaxSize()) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                itemsIndexed(kanbanList) { kanbanColumnIndex, kanbanColumnItem ->
                    /**
                     * this field must be present, otherwise endless recomposition will occur when the element moves
                     */
                    val rememberedList = remember(key1 = kanbanColumnItem.taskItems.size) {
                        mutableStateOf(kanbanColumnItem.taskItems)
                    }
                    DropTarget<TaskItem>(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxHeight() //this allows you to move cards even under a column // todo не актуально
                    ) { isInBound, newItem ->
                        if (isInBound && LocalDragTargetInfo.current.dataToDrop != null) {
                            kanbanBackgroundColor.value = Color.Red
                        } else {
                            kanbanBackgroundColor.value = defaultKanbanCardColor
                        }

                        if (newItem != null && isInBound) {

                            if (newItem.kanbanColumnId != kanbanColumnItem.id) {
                                LocalDragTargetInfo.current.dataToDrop = null
                                kanbanViewModel.deleteItemById(
                                    newItem,
                                    kanbanColumnItem.id,
                                    kanbanColumnIndex
                                )
                            } else {
                                kanbanBackgroundColor.value = defaultKanbanCardColor
                            }
                            //you need to set dataToDrop to null otherwise there will be endless recomposition
                            LocalDragTargetInfo.current.dataToDrop = null
                        }
                        Card(
                            modifier = Modifier.padding(8.dp).width(280.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = kanbanBackgroundColor.value,
                                disabledContainerColor = kanbanBackgroundColor.value
                            ),
                        ) {
                            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                                KanbanColumn(taskItemsState = rememberedList)
                                TaskCreationBlock(
                                    modifier = Modifier.padding(
                                        start = 14.dp,
                                        end = 14.dp,
                                        bottom = 1.dp
                                    ),
                                    contentTextFieldPadding = PaddingValues(
                                        start = 12.dp,
                                        end = 12.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    ),
                                    shape = RoundedCornerShape(
                                        topStart = 8.dp,
                                        topEnd = 8.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 0.dp
                                    ),
                                    hint = "New Task...",
                                    minHeight = 35.dp,
                                )
                                TaskCreationBlock(
                                    modifier = Modifier.padding(
                                        start = 14.dp,
                                        end = 14.dp,
                                        bottom = 1.dp
                                    ),
                                    contentTextFieldPadding = PaddingValues(
                                        start = 12.dp,
                                        end = 12.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    ),
                                    shape = RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 0.dp,
                                        bottomEnd = 8.dp,
                                        bottomStart = 8.dp
                                    ),
                                    hint = "Description...",
                                    minHeight = 50.dp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StuffListUI() {

    val viewModel = remember { Inject.instance<KanbanViewModel>() }
    val uiState = viewModel.uiState.collectAsState()

    DraggableItemInWindow {
        Row(modifier = Modifier.fillMaxSize()) {
            DragDropRow(
                items = uiState.value,
                onSwap = viewModel::swapSections2
            ) { section, index ->
                Column {
                    Text("Заголовок")
                    Spacer(modifier = Modifier.padding(20.dp))
                    Card(
                        modifier = Modifier.background(Color.Blue)
                            .defaultMinSize(minWidth = 40.dp, minHeight = 120.dp)
                            .clickable {},
                    ) {
                        val dragItemStateInFullWindow = LocalDragItemStateInFullWindow.current
                        DragDropColumn(
                            items = section.sec2,
                            columnIndex = index,
                            dragItemStateInFullWindow = dragItemStateInFullWindow,
                            onSwap = viewModel::taskSwap,
                            addItemInList = { itemIndex, columnIndex, item ->
                                viewModel.addNewItemInList(
                                    itemIndex,
                                    columnIndex,
                                    item
                                )
                            },
                            removeItemFromList = viewModel::removeItemFromColumn
                        ) { item ->
                            val positionInWindow = remember { mutableStateOf(Offset.Zero) }
                            Card(
                                modifier = Modifier
                                    .height(60.dp)
                                    .onGloballyPositioned {
                                        positionInWindow.value = it.positionInWindow()
//                                        positionIn.value = it.positionInParent()
                                    }
                                    .pointerInput(item) {
                                        detectTapGestures(onPress = {
                                            dragItemStateInFullWindow.apply {
                                                if (dragItem is Unit) {
                                                    dragItem = item
                                                }
                                                dragItemPositionInFullWindow =
                                                    Offset(
                                                        x = positionInWindow.value.x,
                                                        y = positionInWindow.value.y
                                                    )
                                            }
                                        },
                                            onTap = {
                                                //todo this for onclick
                                            }
                                        )
                                    }
                            ) {
                                Text(
                                    text = item.name,
                                    color = MaterialTheme.colors.onSurface,
                                    modifier = Modifier
                                        .background(Color(item.color))
                                        .padding(16.dp),
                                )
                            }
                        }
                    }
                }
            }
            //todo delete all
            val dragItemStateInFullWindow = LocalDragItemStateInFullWindow.current
            Column {
                Text(
                    "Offset x${dragItemStateInFullWindow.dragOffset.x}, y = ${dragItemStateInFullWindow.dragOffset.y}",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 100.dp, bottom = 40.dp)
                )

                Text(
                    "ИН РУТ, y = ${dragItemStateInFullWindow.dragOffsetForAnotherColumnWithConversion.y}",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 100.dp, bottom = 40.dp)
                )

                Text(
                    "ИН РУТ FOR ANOTHER COLUMN y = ${dragItemStateInFullWindow.staticOffsetOfItemInsideColumn.y + dragItemStateInFullWindow.dragOffsetForAnotherColumnWithConversion.y}",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 100.dp, bottom = 40.dp)
                )


                Text(
                    "Dragged Distance y = ${dragItemStateInFullWindow.staticOffsetOfItemInsideColumn}",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 100.dp, bottom = 40.dp)
                )
            }

        }

    }
}