package drag_and_drop.draggable_in_window

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset


val LocalDragItemStateInFullWindow = compositionLocalOf { DragItemStateInFullWindow() }

class DragItemStateInFullWindow {
    var isDragging: Boolean by mutableStateOf(false)
    //offset
    var dragOffset by mutableStateOf(Offset.Zero)
    var dragItemOffsetInColumn by mutableStateOf(Offset.Zero)
    var dragItemPositionInFullWindow by mutableStateOf<Offset>(Offset.Zero)
    var dragOffsetForAnotherColumnWithConversion by mutableStateOf(Offset.Zero)
    var offsetConversionForAnotherColumn by mutableStateOf(Offset.Zero)
    var staticOffsetOfItemInsideColumn by mutableStateOf(Offset.Zero)

    var isProcessOfChangingColumn by mutableStateOf(false)

    //events
    var startedInitializingDraggableEvent by mutableStateOf(false)
    var onDragEventInFullWindow: () -> Unit = {}
    var onDragStartedEventInFullWindow: () -> Unit = {}
    var onDragEndEventInFullWindow: () -> Unit = {}
    var onDragCancelEventInFullWindow: () -> Unit = {}

    var parentColumnIndexOfDraggableItem by mutableStateOf(INDEX_IS_NOT_SET)
    var newColumnIndexForDragItem by mutableStateOf(INDEX_IS_NOT_SET)
    var newColumnItemIndexWhenOnDrag by mutableStateOf(INDEX_IS_NOT_SET)

    var dragItem by mutableStateOf<Any>(Unit)

    var drawingDragItemInFullWindowEvent by mutableStateOf<(@Composable BoxScope.(isDragging: Boolean) -> Unit)?>(
        null
    )

    fun cancelOnChangingBoardInFullWindow() {
        if (isProcessOfChangingColumn) {
            isProcessOfChangingColumn = false
            newColumnIndexForDragItem = INDEX_IS_NOT_SET
            newColumnItemIndexWhenOnDrag = INDEX_IS_NOT_SET
            onDragStartedEventInFullWindow = {}
            onDragEventInFullWindow = {}
            onDragEndEventInFullWindow.invoke()
            onDragCancelEventInFullWindow.invoke()
            onDragEndEventInFullWindow = {}
            onDragCancelEventInFullWindow = {}
            dragItemOffsetInColumn = Offset.Zero
            startedInitializingDraggableEvent = false
        }
    }

    fun resetSelectedColumnForDragItemInFullWindow() {
        newColumnIndexForDragItem = INDEX_IS_NOT_SET
        newColumnItemIndexWhenOnDrag = INDEX_IS_NOT_SET
        onDragStartedEventInFullWindow = {}
        onDragEventInFullWindow = {}
        onDragEndEventInFullWindow.invoke()
        onDragCancelEventInFullWindow.invoke()
        onDragEndEventInFullWindow = {}
        onDragCancelEventInFullWindow = {}
        dragItemOffsetInColumn = Offset.Zero
        startedInitializingDraggableEvent = false
    }

    companion object {
        const val INDEX_IS_NOT_SET = -1
    }
}