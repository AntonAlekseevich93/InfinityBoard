import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import main_models.TaskItemVo


/**
 * the function must always be of MutableState type,
 * otherwise endless recomposition will occur when moving the element
 */
@Composable
fun KanbanColumn(
    taskItemsState: MutableState<MutableList<TaskItemVo>>,
) {
    Column {
        Box(
            modifier = Modifier.padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn {
                itemsIndexed(
                    items = taskItemsState.value,
                ) { taskIndex, taskItem ->
                    val item = remember(key1 = taskItem.id) { mutableStateOf(taskItem) }

                }
            }
        }
    }
}

@Composable
internal fun ColumnTitle(
    columnName: String,
    modifier: Modifier = Modifier,
    columnMenuListener: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = columnName,
            modifier = Modifier.padding(start = 4.dp, end = 16.dp),
            style = ApplicationTheme.typography.title3Bold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f, fill = true))
        Image(
            painter = painterResource(Drawable.drawable_ic_more_button),
            contentDescription = null,
            colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { columnMenuListener.invoke() }
        )
    }
}