package tooltip_area

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberCursorPositionProvider
import org.jetbrains.skia.Drawable

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TooltipIconArea(
    text: String,
    drawableResName: String,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    showPointerEvent: Boolean = false,
    pointerInnerPadding: Dp = 6.dp,
    pointerEventBackgroundDisable: Color = ApplicationTheme.colors.cardBackgroundDark, //todo переделать токен цвет)
    backgroundColor: Color = ApplicationTheme.colors.tooltipAreaBackground,
    iconTint: Color = ApplicationTheme.colors.mainIconsColor,
    onClick: () -> Unit = {},
) {
    val onPointerEventIsActive = remember(PointerEventType) { mutableStateOf(false) }
    val cardBackground = if (onPointerEventIsActive.value) {
        Color(0xFF4D4D50) //todo переделать токен цвет)
    } else {
        pointerEventBackgroundDisable
    }

    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.padding(10.dp).shadow(4.dp).onClick { onClick.invoke() },
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
        delayMillis = 400, // in milliseconds
        tooltipPlacement = TooltipPlacement.CursorPoint(
            offset = DpOffset(16.dp, 8.dp)
        )
    ) {
        Card(
            modifier = modifier.onClick(onClick = onClick)
                .onPointerEvent(PointerEventType.Enter) {
                    if (showPointerEvent) {
                        onPointerEventIsActive.value = true
                    }
                }
                .onPointerEvent(PointerEventType.Exit) {
                    if (showPointerEvent ) {
                        onPointerEventIsActive.value = false
                    }
                },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(cardBackground)
        ) {
            Image(
                painter = painterResource(drawableResName),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconTint),
                modifier = imageModifier
                    .padding(pointerInnerPadding)
                    .size(iconSize)
            )
        }
    }
}
