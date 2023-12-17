package tooltip_area

import ApplicationTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.onClick
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipAreaCustom(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ApplicationTheme.colors.tooltipAreaBackground,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.shadow(4.dp).onClick { onClick.invoke() },
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(10.dp),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor
                )
            }
        },
        modifier = modifier,
        delayMillis = 400, // in milliseconds
        tooltipPlacement = TooltipPlacement.CursorPoint()
    ) {
        content.invoke()
    }
}