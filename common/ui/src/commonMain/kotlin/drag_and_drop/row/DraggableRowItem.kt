package drag_and_drop.row

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex

@ExperimentalFoundationApi
@Composable
fun LazyItemScope.DraggableRowItem(
    dragDropState: DragDropRowState,
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(isDragging: Boolean, index: Int) -> Unit
) {
    val current: Float by animateFloatAsState(dragDropState.draggingItemOffset * 0.67f)
    val previous: Float by animateFloatAsState(dragDropState.previousItemOffset.value * 0.67f)
    val dragging = index == dragDropState.currentIndexOfDraggedItem

    val draggingModifier = if (dragging) {
        Modifier
            .zIndex(1f)
            .graphicsLayer {
                translationX = current
            }
    } else if (index == dragDropState.previousIndexOfDraggedItem) {
        Modifier
            .zIndex(1f)
            .graphicsLayer {
                translationX = previous
            }
    } else {
        Modifier.animateItemPlacement(
            tween(easing = FastOutLinearInEasing)
        )
    }
    Column(modifier = modifier.then(draggingModifier)) {
        content(dragging, index)
    }
}