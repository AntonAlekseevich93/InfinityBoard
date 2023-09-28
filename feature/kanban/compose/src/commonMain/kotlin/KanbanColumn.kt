import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import models.TaskItem

/**
 * the function must always be of MutableState type,
 * otherwise endless recomposition will occur when moving the element
 */
@Composable
fun KanbanColumn(
    taskItemsState: MutableState<MutableList<TaskItem>>,
) {
    Column {
        ColumnTitle(columnName = "Todo", columnMenuListener = {})
        Box(
            modifier = Modifier.padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn {
                itemsIndexed(
                    items = taskItemsState.value,
                ) { taskIndex, taskItem ->
                    val item = remember(key1 = taskItem.id) { mutableStateOf(taskItem) }
                    TaskItemCard(
                        taskItem = item,
                        taskCheckListener = { }
                    )
                }
            }
        }
    }
}

@Composable
internal fun ColumnTitle(columnName: String, columnMenuListener: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = columnName,
            modifier = Modifier.padding(start = 4.dp, end = 16.dp).weight(5f),
            style = ApplicationTheme.typography.title3Bold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { columnMenuListener.invoke() }
                .weight(1f),
            imageVector = Icons.Outlined.MenuOpen,
            contentDescription = null,
            tint = ApplicationTheme.colors.mainIconsColor
        )
    }
}