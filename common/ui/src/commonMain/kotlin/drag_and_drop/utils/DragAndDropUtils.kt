package drag_and_drop.utils

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import drag_and_drop.column.DragAndDropColumnState
import drag_and_drop.row.DragDropRowState

fun LazyListState.getVisibleItemInfoFor(absoluteIndex: Int): LazyListItemInfo? {
    return this
        .layoutInfo
        .visibleItemsInfo
        .getOrNull(absoluteIndex - this.layoutInfo.visibleItemsInfo.first().index)
}

val LazyListItemInfo.offsetEnd: Int
    get() = this.offset + this.size

@Composable
fun rememberDragAndDropColumnState(
    lazyListState: LazyListState,
    columnIndex: Int,
    onSwap: (Int, Int, Boolean) -> Unit
): DragAndDropColumnState {
    val scope = rememberCoroutineScope()
    val state = remember(lazyListState) {
        DragAndDropColumnState(
            state = lazyListState,
            columnIndex = columnIndex,
            onSwap = onSwap,
            scope = scope
        )
    }
    return state
}

@Composable
fun rememberDragDropRowState(
    lazyListState: LazyListState,
    onSwap: (Int, Int) -> Unit
): DragDropRowState {
    val scope = rememberCoroutineScope()
    val state = remember(lazyListState) {
        DragDropRowState(
            state = lazyListState,
            onSwap = onSwap,
            scope = scope
        )
    }
    return state
}
