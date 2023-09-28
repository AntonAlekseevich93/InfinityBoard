import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import di.Inject
import drag_and_drop.DropTarget
import drag_and_drop.LocalDragTargetInfo
import drag_and_drop.LongPressDraggable
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
                            .fillMaxHeight() //this allows you to move cards even under a column
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