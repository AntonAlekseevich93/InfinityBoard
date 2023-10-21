package navigation_drawer

import ApplicationTheme
import Drawable
import Strings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.Divider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import tooltip_area.TooltipIconArea


@Composable
fun PlatformRightDrawerContent(
    platform: Platform,
    isCanClose: Boolean,
    isFullscreen: State<Boolean>,
    selectedItem: SelectedRightDrawerItem = SelectedRightDrawerItem.NONE,
    closeSidebarListener: () -> Unit,
    closeWindow: () -> Unit,
    expandOrCollapseListener: () -> Unit,
) {
    when (platform) {
        Platform.DESKTOP -> {
            DismissibleDrawerSheet(
                drawerContainerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
            ) {
                RightDrawerContent(
                    isCanClose = isCanClose,
                    isFullscreen = isFullscreen,
                    selectedItem = selectedItem,
                    closeSidebarListener = closeSidebarListener,
                    expandOrCollapseListener = expandOrCollapseListener,
                    closeWindow = closeWindow
                )
            }
        }

        else -> {
            ModalDrawerSheet {

            }
        }
    }
}

@Composable
fun RightDrawerContent(
    isCanClose: Boolean,
    isFullscreen: State<Boolean>,
    selectedItem: SelectedRightDrawerItem,
    closeSidebarListener: () -> Unit,
    closeWindow: () -> Unit,
    expandOrCollapseListener: () -> Unit,
) {

    Box(modifier = Modifier.widthIn(max = 300.dp)) {
        Column {
            Row(
                modifier = Modifier.padding(top = 10.dp, bottom = 14.dp, end = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TooltipIconArea(
                    text = Strings.structure,
                    drawableResName = Drawable.drawable_ic_structure,
                    showPointerEvent = true,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    setSelected = selectedItem == SelectedRightDrawerItem.STRUCTURE,
                    pointerIsActiveCardColor =
                    if (selectedItem == SelectedRightDrawerItem.STRUCTURE)
                        ApplicationTheme.colors.pointerIsActiveCardColorDark
                    else
                        ApplicationTheme.colors.pointerIsActiveCardColor,
                    onClick = closeSidebarListener,
                    iconTint = ApplicationTheme.colors.searchIconColor,
                )

                Spacer(modifier = Modifier.weight(1f, fill = true))

                TooltipIconArea(
                    text = Strings.sidebar,
                    drawableResName = Drawable.drawable_ic_note_sidebar,
                    showPointerEvent = true,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = closeSidebarListener,
                )
                if (isCanClose) {
                    Divider(
                        modifier = Modifier.width(1.dp).height(24.dp),
                        color = ApplicationTheme.colors.searchDividerColor
                    )
                    if (isFullscreen.value) {
                        TooltipIconArea(
                            text = Strings.collapse,
                            drawableResName = Drawable.drawable_ic_collapse,
                            showPointerEvent = true,
                            modifier = Modifier.padding(start = 10.dp),
                            tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
                            iconSize = 18.dp,
                            pointerInnerPadding = 4.dp,
                            onClick = expandOrCollapseListener
                        )
                    } else {
                        TooltipIconArea(
                            text = Strings.expand,
                            drawableResName = Drawable.drawable_ic_expand,
                            showPointerEvent = true,
                            modifier = Modifier.padding(start = 10.dp),
                            tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
                            iconSize = 18.dp,
                            pointerInnerPadding = 4.dp,
                            onClick = expandOrCollapseListener
                        )
                    }
                    TooltipIconArea(
                        text = Strings.close,
                        drawableResName = Drawable.drawable_ic_close,
                        showPointerEvent = true,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
                        iconSize = 18.dp,
                        pointerInnerPadding = 4.dp,
                        onClick = closeWindow
                    )
                }
            }
            Divider(
                modifier = Modifier.fillMaxWidth().height(1.dp),
                color = ApplicationTheme.colors.divider
            )
        }
    }
}

enum class SelectedRightDrawerItem {
    NONE,
    STRUCTURE,
}
