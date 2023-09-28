package drag_and_drop.draggable_in_window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DraggableItemInWindow(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DragItemStateInFullWindow() }
    CompositionLocalProvider(
        LocalDragItemStateInFullWindow provides state
    ) {
        Box(modifier = modifier.fillMaxSize().background(Color.Gray)) {
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

@Composable
private fun DraggableTextLowLevel(state: DragItemStateInFullWindow) {
//    val offset1 = state.dragOffset
//    val offset1 = state.windowChangingOffset
    val offset1 = Offset.Zero
    Box(modifier = Modifier.background(Color.Black)) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Box(
            Modifier
                .offset { IntOffset(offset1.x.roundToInt(), offset1.y.roundToInt()) }
                .background(Color.Blue)
                .height(60.dp).width(250.dp)

        )
    }
}