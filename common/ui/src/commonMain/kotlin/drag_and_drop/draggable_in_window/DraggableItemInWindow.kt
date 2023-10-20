package drag_and_drop.draggable_in_window

import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

@Composable
fun DraggableItemInWindow(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DragItemStateInFullWindow() }
    CompositionLocalProvider(
        LocalDragItemStateInFullWindow provides state
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            content()
            if (state.isDragging) {
                var targetSize by remember {
                    mutableStateOf(IntSize.Zero)
                }
                Box(modifier = Modifier
                    .graphicsLayer {
                        val offset = state.dragOffset
                        scaleX = 1.0f
                        scaleY = 1.0f
                        rotationZ = 4f
                        alpha = if (targetSize == IntSize.Zero) 0f else .9f
                        translationX = offset.x
                        translationY = offset.y
                            .minus(98)
                    }
                    .onGloballyPositioned {
                        targetSize = it.size
                    }
                ) {
                    state.drawingDragItemInFullWindowEvent?.invoke(this, true)
                }
            }
        }
    }
}