package drag_and_drop.column

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import drag_and_drop.draggable_in_window.DragItemStateInFullWindow

@ExperimentalFoundationApi
@Composable
fun LazyItemScope.DraggableColumnItem(
    dragAndDropColumnState: DragAndDropColumnState,
    itemIndex: Int,
    columnIndex: Int,
    modifier: Modifier = Modifier,
    dragItemStateInFullWindow: DragItemStateInFullWindow,
    content: @Composable BoxScope.(isDragging: Boolean) -> Unit
) {
    val dragging = itemIndex == dragAndDropColumnState.currentIndexOfDraggedItem
    val itemOffsetInParent = remember { mutableStateOf(Offset.Zero) }

    val draggingModifier = getDraggableModifier(
        dragging = dragging,
        dragItemStateInFullWindow = dragItemStateInFullWindow,
        itemIndex = itemIndex,
        indexOfDraggedItem = dragAndDropColumnState.currentIndexOfDraggedItem,
        columnIndex = columnIndex,
        dragAndDropColumnState = dragAndDropColumnState
    ).onGloballyPositioned {
        itemOffsetInParent.value = it.positionInParent()
    }

    if(dragging && dragItemStateInFullWindow.staticOffsetOfItemInsideColumn == Offset.Zero){
        dragItemStateInFullWindow.staticOffsetOfItemInsideColumn = itemOffsetInParent.value
    }

    if (dragging && !dragItemStateInFullWindow.isProcessOfChangingColumn && columnIndex != dragItemStateInFullWindow.newColumnIndexForDragItem) {
        /** this field is responsible for the global drawing of item on the entire window*/
        dragItemStateInFullWindow.drawingDragItemInFullWindowEvent = content
    }
    Box(modifier = modifier.then(draggingModifier).background(Color.Transparent)) {
        content(dragging)
    }
}


@ExperimentalFoundationApi
@Composable
private fun LazyItemScope.getDraggableModifier(
    dragging: Boolean,
    dragItemStateInFullWindow: DragItemStateInFullWindow,
    itemIndex: Int,
    indexOfDraggedItem: Int?,
    columnIndex: Int,
    dragAndDropColumnState: DragAndDropColumnState
): Modifier {
    return if (dragging) {
        if (!dragItemStateInFullWindow.isProcessOfChangingColumn && columnIndex != dragItemStateInFullWindow.newColumnIndexForDragItem) {
            Modifier
                .zIndex(1f)
                .graphicsLayer {
                    alpha = 0f
                    translationY = dragAndDropColumnState.draggingItemOffset
                }
        } else if (dragItemStateInFullWindow.isProcessOfChangingColumn && columnIndex == dragItemStateInFullWindow.parentColumnIndexOfDraggableItem && itemIndex == indexOfDraggedItem) {
            Modifier.zIndex(1f)
                .height(0.dp)
                .graphicsLayer {
                    translationY = 0f
                    alpha = 0f
                }
        } else {
            Modifier
                .zIndex(1f)
                .graphicsLayer {
                    if (dragItemStateInFullWindow.isProcessOfChangingColumn && columnIndex == dragItemStateInFullWindow.newColumnIndexForDragItem && indexOfDraggedItem == dragItemStateInFullWindow.newColumnItemIndexWhenOnDrag) {
                        /**fake item*/
//                        alpha = 0f
                    }
                    translationY = dragAndDropColumnState.draggingItemOffset
                }
        }
    } else if (itemIndex == dragAndDropColumnState.previousIndexOfDraggedItem) {
        Modifier
//            .zIndex(1f)
//            .graphicsLayer {
//                translationY = -1000F
//            }
    } else {
        Modifier.animateItemPlacement(tween(durationMillis = 150, easing = FastOutLinearInEasing))
//            .graphicsLayer {
//                translationY = -10f
//            }
//            .animateItemPlacement(
//            tween(easing = FastOutLinearInEasing)
//        )
    }
}










