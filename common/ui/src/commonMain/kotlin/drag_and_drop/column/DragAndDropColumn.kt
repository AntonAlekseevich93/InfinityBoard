package drag_and_drop.column

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import drag_and_drop.draggable_in_window.DragItemStateInFullWindow
import drag_and_drop.draggable_in_window.DragItemStateInFullWindow.Companion.INDEX_IS_NOT_SET
import drag_and_drop.utils.rememberDragAndDropColumnState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> DragDropColumn(
    items: List<T>,
    onSwap: (Int, Int, Int, Boolean) -> Unit,
    columnIndex: Int,
    dragItemStateInFullWindow: DragItemStateInFullWindow,
    addItemInList: (itemIndex: Int, columnIndex: Int, item: Any) -> Unit,
    removeItemFromList: (item: Any, columnIndex: Int) -> Unit,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val dragAndDropColumnState =
        rememberDragAndDropColumnState(
            lazyListState,
            columnIndex = columnIndex
        ) { fromIndex, toIndex, shiftOnly ->
            if (dragItemStateInFullWindow.isProcessOfChangingColumn && dragItemStateInFullWindow.newColumnIndexForDragItem == columnIndex) {
                dragItemStateInFullWindow.newColumnItemIndexWhenOnDrag = toIndex
            }
            onSwap(fromIndex, toIndex, columnIndex, shiftOnly)
        }
    var isCurrentColumnDropTarget by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    var overscrollJobForParentColumn by remember { mutableStateOf<Job?>(null) }
    var overscrollJobForAnotherColumn by remember { mutableStateOf<Job?>(null) }
    LazyColumn(
        modifier = Modifier
            .pointerInput(dragAndDropColumnState) {
                detectDragGestures(
                    onDrag = { change, offset ->
                        change.consume()

                        dragItemStateInFullWindow.apply {

                            if (isProcessOfChangingColumn) {
                                dragAndDropColumnState.onDrag(
                                    offset = offset,
                                    isBlockingOnDrag = true
                                )
                                onDragEventInFullWindow.invoke()
                                dragItemOffsetInColumn = offset
                            } else {
                                dragAndDropColumnState.onDrag(offset = offset)
                            }

                            //todo подумать нужна ли проверка или оставить блок кода без проверки
                            //todo т.к. невозможно двигать item если offset будет 0
                            //todo у нас не должно быть ситуаций когда draggableItemPositionInFullWindow == 0
                            if (dragItemPositionInFullWindow != Offset.Zero) {
                                dragOffset = dragItemPositionInFullWindow + offset
                                dragItemPositionInFullWindow += offset
                            }
                            dragOffsetForAnotherColumnWithConversion =
                                offsetConversionForAnotherColumn + offset
                            offsetConversionForAnotherColumn += offset

                            if (overscrollJobForParentColumn?.isActive == true)
                                return@detectDragGestures

                            dragAndDropColumnState
                                .checkForOverScroll()
                                .takeIf { it != 0f }
                                ?.let {
                                    overscrollJobForParentColumn =
                                        scope.launch {
                                            dragAndDropColumnState.state.animateScrollBy(
                                                value = it * 0.3f,
                                                animationSpec = tween(
                                                    easing = FastOutLinearInEasing,
                                                    durationMillis = 1
                                                )
                                            )
                                        }
                                }
                                ?: run { overscrollJobForParentColumn?.cancel() }
                        }
                    },
                    onDragStart = { offset ->
                        dragAndDropColumnState.onDragStart(offset)
                        dragItemStateInFullWindow.apply {
                            dragItemOffsetInColumn = offset
                            parentColumnIndexOfDraggableItem = columnIndex
                            dragOffset = offset
                            isDragging = true
                        }
                    },
                    onDragEnd = {
                        onDragStop(
                            columnIndex = columnIndex,
                            dragItemStateInFullWindow = dragItemStateInFullWindow,
                            dragAndDropColumnState = dragAndDropColumnState,
                            removeItemInList = removeItemFromList
                        )
                        overscrollJobForParentColumn?.cancel()
                    },
                    onDragCancel = {
                        onDragStop(
                            columnIndex = columnIndex,
                            dragItemStateInFullWindow = dragItemStateInFullWindow,
                            dragAndDropColumnState = dragAndDropColumnState,
                            removeItemInList = removeItemFromList
                        )
                        overscrollJobForParentColumn?.cancel()
                    }
                )
            }
            .onGloballyPositioned {
                it.boundsInWindow().let { rect ->
                    if (dragItemStateInFullWindow.isDragging) {
                        isCurrentColumnDropTarget = defineBoundariesForDraggableElement(
                            rect = rect,
                            dragAndDropColumnState = dragAndDropColumnState,
                            dragItemStateInFullWindow = dragItemStateInFullWindow,
                            selectedAnotherColumnEvent = { columnIndex ->
                                val offset =
                                    dragItemStateInFullWindow.dragOffsetForAnotherColumnWithConversion + dragItemStateInFullWindow.staticOffsetOfItemInsideColumn
                                addItemInList.invoke(
                                    0, // we need to always insert at position 0
                                    columnIndex,
                                    dragItemStateInFullWindow.dragItem
                                )
                                dragAndDropColumnState.onDrag(
                                    offset = offset,
                                    isBlockingOnDrag = false,
                                    newDraggedDistance = offset.y,
                                )
                            },
                            returnedFromWindowToParentColumn = {
                                val offset =
                                    dragItemStateInFullWindow.dragOffsetForAnotherColumnWithConversion
                                dragAndDropColumnState.onDrag(
                                    offset = offset,
                                    isBlockingOnDrag = false,
                                    newDraggedDistance = offset.y
                                )
                            },
                            removeItemFromList = removeItemFromList,
                            columnIndex = columnIndex,
                            overscrollJobForAnotherColumn = overscrollJobForAnotherColumn,
                            overscrollJobForAnotherColumnEvent = {
                                overscrollJobForAnotherColumn = scope.launch {
                                    dragAndDropColumnState.state.animateScrollBy(
                                        value = it * 0.3f,
                                        animationSpec = tween(
                                            easing = FastOutLinearInEasing,
                                            durationMillis = 1
                                        )
                                    )
                                }
                            }
                        )
                    } else isCurrentColumnDropTarget = false
                }
            }.background(if (isCurrentColumnDropTarget) Color.Green else Color.Blue),
        state = lazyListState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items = items) { index, item ->
            DraggableColumnItem(
                dragAndDropColumnState = dragAndDropColumnState,
                itemIndex = index,
                columnIndex = columnIndex,
                dragItemStateInFullWindow = dragItemStateInFullWindow
            ) { isDragging ->

                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                Card(elevation = elevation, modifier = Modifier.defaultMinSize(minWidth = 250.dp)) {
                    itemContent(item)
                }
            }
        }
    }
}

private fun defineBoundariesForDraggableElement(
    rect: Rect,
    dragAndDropColumnState: DragAndDropColumnState,
    dragItemStateInFullWindow: DragItemStateInFullWindow,
    returnedFromWindowToParentColumn: () -> Unit,
    selectedAnotherColumnEvent: (columnIndex: Int) -> Unit,
    overscrollJobForAnotherColumn: Job?,
    overscrollJobForAnotherColumnEvent: (offset: Float) -> Unit,
    removeItemFromList: (item: Any, columnIndex: Int) -> Unit,
    columnIndex: Int
): Boolean {
    dragItemStateInFullWindow.apply {
        val offsetWithShift = Offset(
            x = dragOffset.x.plus(220),
            y = dragOffset.y
        )
        val isBound = rect.contains(offsetWithShift)

        if (parentColumnIndexOfDraggableItem == columnIndex) {
            if (isBound) {
                if (isProcessOfChangingColumn) {
                    returnedFromWindowToParentColumn.invoke()
                    cancelOnChangingBoardInFullWindow()
                }
            } else if (!isProcessOfChangingColumn) {
                isProcessOfChangingColumn = true
            }
        } else if (parentColumnIndexOfDraggableItem != columnIndex) {
            if (isBound) {
                if (newColumnIndexForDragItem == INDEX_IS_NOT_SET) {
                    setMultiColumnEvents(
                        dragItemStateInFullWindow = dragItemStateInFullWindow,
                        dragAndDropColumnState = dragAndDropColumnState,
                        overscrollJob = overscrollJobForAnotherColumn,
                        overscrollJobEvent = overscrollJobForAnotherColumnEvent
                    )
                    /**
                     * the call to windowOnDragStartedEvent must occur after
                     * the call to the setMultiColumnEvents() method
                     */
                    startedInitializingDraggableEvent = true
                    onDragStartedEventInFullWindow.invoke()
                    selectedAnotherColumnEvent.invoke(columnIndex)
                    newColumnIndexForDragItem = columnIndex
                }
            } else {
                /**
                 * here we can have a third column that is always called out of bounds
                 * at the same time the new selected column that will be within the boundaries will be called
                 * i.e. The new selected column should always reset the value if it is not in bound
                 */
                if (newColumnIndexForDragItem == columnIndex) {
                    removeItemFromList.invoke(dragItem, columnIndex)
                    resetSelectedColumnForDragItemInFullWindow()
                }
            }
        }
        return isBound
    }
}

private fun setMultiColumnEvents(
    dragItemStateInFullWindow: DragItemStateInFullWindow,
    dragAndDropColumnState: DragAndDropColumnState,
    overscrollJobEvent: (offset: Float) -> Unit,
    overscrollJob: Job?,
) {
    dragItemStateInFullWindow.apply {
        onDragStartedEventInFullWindow = {
            dragAndDropColumnState.onDragStart(dragItemOffsetInColumn)
        }
        onDragEventInFullWindow = {
            dragAndDropColumnState.onDrag(offset = dragItemOffsetInColumn)
            dragAndDropColumnState
                .checkForOverScroll()
                .takeIf { it != 0f }
                ?.let {
                    overscrollJobEvent.invoke(it)
                }
                ?: run { overscrollJob?.cancel() }

            onDragEndEventInFullWindow = {
                dragAndDropColumnState.onDragInterrupted()
                overscrollJob?.cancel()
                cancelOnChangingBoardInFullWindow()
            }

            onDragCancelEventInFullWindow = {
                dragAndDropColumnState.onDragInterrupted()
                overscrollJob?.cancel()
                cancelOnChangingBoardInFullWindow()
            }
        }
    }
}

private fun onDragStop(
    columnIndex: Int,
    dragItemStateInFullWindow: DragItemStateInFullWindow,
    dragAndDropColumnState: DragAndDropColumnState,
    removeItemInList: (item: Any, columnIndex: Int) -> Unit,
) {
    dragItemStateInFullWindow.apply {
        if (newColumnIndexForDragItem != INDEX_IS_NOT_SET) {
            removeItemInList.invoke(
                dragItem,
                columnIndex
            )
            onDragEndEventInFullWindow.invoke()
        }
        dragAndDropColumnState.onDragInterrupted()
        cancelOnChangingBoardInFullWindow()
        staticOffsetOfItemInsideColumn = Offset.Zero
        isDragging = false
        dragOffset = Offset.Zero
        dragItemPositionInFullWindow = Offset.Zero
        offsetConversionForAnotherColumn = Offset.Zero
        dragOffsetForAnotherColumnWithConversion = Offset.Zero
        onDragEndEventInFullWindow.invoke()
        dragItem = Unit
    }
}
