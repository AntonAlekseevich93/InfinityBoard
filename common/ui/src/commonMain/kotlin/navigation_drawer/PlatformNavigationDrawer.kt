package navigation_drawer

import ApplicationTheme
import Drawable
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tooltip_area.TooltipIconArea

@Composable
fun PlatformNavigationDrawer(
    platform: Platform,
    drawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    showDrawer: MutableState<Boolean>,
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    when (platform) {
        Platform.DESKTOP -> {
            CustomDrawer(
                drawerContent = drawerContent,
                modifier = modifier,
                show = showDrawer.value,
                content = content
            )
        }

        else -> {
            ModalNavigationDrawer(
                drawerContent = drawerContent,
                modifier = modifier,
                drawerState = drawerState,
                gesturesEnabled = gesturesEnabled,
                content = content
            )
        }
    }
}

/**
 * We use this solution because DismissibleNavigationDrawer has a bug with offset
 * in compose (multiplatoform 1.5.3. version)
 */
@Composable
fun CustomDrawer(
    drawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    show: Boolean,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxHeight()
            .background(ApplicationTheme.colors.mainBackgroundWindowDarkColor)
    ) {
        Row() {
            AnimatedVisibility(visible = show) {
                drawerContent()
            }
            content()
        }
    }
}

@Composable
fun PlatformDrawerContent(
    platform: Platform,
    closeSidebarListener: () -> Unit
) {
    when (platform) {
        Platform.DESKTOP -> {
            DismissibleDrawerSheet(
                drawerContainerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
            ) {
                DrawerContent(closeSidebarListener)
            }
        }


        else -> {
            ModalDrawerSheet {

            }
        }
    }
}

@Composable
fun DrawerContent(closeSidebarListener: () -> Unit) {
    Box(modifier = Modifier.widthIn(max = 300.dp)) {
        Column {
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Проекты", //todo текст передается
                    modifier = Modifier.padding(start = 32.dp),
                    style = ApplicationTheme.typography.title3Bold,
                    color = ApplicationTheme.colors.mainTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f, fill = true))

                TooltipIconArea(
                    text = Strings.to_main,
                    drawableResName = Drawable.drawable_ic_home,
                    showPointerEvent = true,
                    modifier = Modifier.padding(start = 6.dp, end = 6.dp),

                    ) {
                    closeSidebarListener.invoke()
                }
                TooltipIconArea(
                    text = Strings.menu,
                    drawableResName = Drawable.drawable_ic_sidebar,
                    showPointerEvent = true,
                    modifier = Modifier.padding(end = 12.dp),

                    ) {
                    closeSidebarListener.invoke()
                }
            }
        }
    }
}


//todo переделать
enum class Platform {
    DESKTOP
}