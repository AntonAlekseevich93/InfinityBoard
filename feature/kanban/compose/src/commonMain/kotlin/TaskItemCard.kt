import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import main_models.TaskItemVo

/**
 * dont use in this function fillMaxWidth
 * When you move the card, the card will stretch to fill the entire screen
 * Use minWidth
 */
@Composable
fun TaskItemCard(
    taskItem: TaskItemVo,
    minWidth: Dp,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ApplicationTheme.colors.cardBackgroundLight,
    taskCheckListener: (isChecked: Boolean) -> Unit
) {
    Card(
        modifier = modifier.defaultMinSize(minWidth = minWidth),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(0.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                CustomCheckbox(
                    taskItem.isDone,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                )
                Text(
                    text = taskItem.name,
                    style = ApplicationTheme.typography.bodyMedium,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }
            Box(
                modifier = Modifier.padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.RoundedCorner,
                    contentDescription = "",
                    tint = ApplicationTheme.colors.mainIconsColor
                )
            }
        }
    }
}

@Composable
fun CustomCheckbox(
    isChecked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.background(ApplicationTheme.colors.cardBackgroundDark),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, color = Color.Black)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(if (isChecked) Color.Gray else ApplicationTheme.colors.cardBackgroundDark)
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            if (isChecked)
                Icon(Icons.Default.Check, contentDescription = "", tint = Color.Red)
        }
    }
}