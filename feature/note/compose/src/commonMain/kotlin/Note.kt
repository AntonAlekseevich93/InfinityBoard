import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import di.Inject
import navigation_drawer.Platform
import navigation_drawer.PlatformNavigationDrawer
import navigation_drawer.PlatformRightDrawerContent
import navigation_drawer.SelectedRightDrawerItem
import tooltip_area.TooltipIconArea

@Composable
fun Note(
    platform: Platform,
    fullScreenNote: MutableState<Boolean>,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    openLeftDrawerListener: () -> Unit,
    openRightDrawerListener: () -> Unit,
    closeRightDrawerListener: () -> Unit,
    onClose: () -> Unit,
) {
    val viewModel = remember { Inject.instance<NoteViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    val targetVerticalPadding = if (fullScreenNote.value) 0.dp else 65.dp
    val targetHorizontalPadding =
        if (fullScreenNote.value) 0.dp else if (showRightDrawer.value) 100.dp else 220.dp
    val animatedVerticalPadding by animateDpAsState(
        targetValue = targetVerticalPadding,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 10,
            easing = FastOutSlowInEasing
        )
    )
    val animatedHorizontalPadding by animateDpAsState(
        targetValue = targetHorizontalPadding,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 10,
            easing = FastOutSlowInEasing
        )
    )
    val shapeInDp = if (fullScreenNote.value) 0.dp else 8.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onClose.invoke()
                    },
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(shapeInDp),
            colors = CardDefaults.cardColors(
                containerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = animatedVerticalPadding,
                    horizontal = animatedHorizontalPadding
                )

                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            /** это нужно чтобы перехватывать onPress
                             * на корневом Box который закрывает поиск*/
                        },
                    )
                },
        ) {
            PlatformNavigationDrawer(
                platform = platform,
                rightDrawerContent = {
                    AnimatedVisibility(visible = showRightDrawer.value) {
                        Row {
                            Divider(
                                modifier = Modifier.fillMaxHeight().width(1.dp),
                                thickness = 1.dp,
                                color = ApplicationTheme.colors.divider
                            )
                            PlatformRightDrawerContent(
                                platform = platform, //todo,
                                isFullscreen = fullScreenNote,
                                isCanClose = true,
                                selectedItem = SelectedRightDrawerItem.STRUCTURE,
                                closeSidebarListener = closeRightDrawerListener,
                                expandOrCollapseListener = {
                                    fullScreenNote.value = !fullScreenNote.value
                                },
                                closeWindow = onClose
                            )
                        }
                    }
                },
                background = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
                showRightDrawer = showRightDrawer
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    NoteBar(
                        showLeftDrawer = showLeftDrawer,
                        showRightDrawer = showRightDrawer,
                        onClose = onClose,
                        onFullscreen = { fullScreenNote.value = !fullScreenNote.value },
                        isFullscreen = fullScreenNote,
                        openLeftDrawerListener = openLeftDrawerListener,
                        openRightDrawerListener = openRightDrawerListener,
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth().height(1.dp),
                        color = ApplicationTheme.colors.divider
                    )
                    Box() {
                        Text(
                            modifier = Modifier.padding(start = 55.dp, end = 55.dp, top = 32.dp),
                            text = "",
                            style = ApplicationTheme.typography.bodyRegular,
                            color = ApplicationTheme.colors.mainPlaceholderTextColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteBar(
    isFullscreen: State<Boolean>,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: State<Boolean>,
    onFullscreen: () -> Unit,
    openLeftDrawerListener: () -> Unit,
    openRightDrawerListener: () -> Unit,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp, max = 50.dp).padding(start = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = isFullscreen.value && !showLeftDrawer.value,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut(spring(stiffness = Spring.StiffnessHigh))
        ) {
            Row {
                TooltipIconArea(
                    text = Strings.to_main,
                    drawableResName = Drawable.drawable_ic_home,
                    showPointerEvent = true,
                    modifier = Modifier.padding(start = 10.dp),
                    tooltipOffset = DpOffset(x = (-40).dp, y = (-70).dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp
                ) {
                    println("открыли меню")
                }

                TooltipIconArea(
                    text = Strings.menu,
                    drawableResName = Drawable.drawable_ic_sidebar,
                    showPointerEvent = true,
                    modifier = Modifier.padding(start = 10.dp),
                    tooltipOffset = DpOffset(x = (-40).dp, y = (-70).dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = openLeftDrawerListener
                )
            }
        }

        TooltipIconArea(
            text = Strings.tags,
            drawableResName = Drawable.drawable_ic_tag,
            showPointerEvent = true,
            modifier = Modifier.padding(start = 10.dp),
            tooltipOffset = DpOffset(x = (-40).dp, y = (-70).dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp
        ) {
            println("открыли меню")
        }
        TooltipIconArea(
            text = Strings.add_subtask,
            drawableResName = Drawable.drawable_ic_subtask,
            showPointerEvent = true,
            modifier = Modifier.padding(start = 10.dp),
            tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp
        ) {
            println("открыли меню")
        }
        TooltipIconArea(
            text = Strings.add_calendar,
            drawableResName = Drawable.drawable_ic_calendar,
            showPointerEvent = true,
            modifier = Modifier.padding(start = 10.dp),
            tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp
        ) {
            println("открыли меню")
        }
        TooltipIconArea(
            text = Strings.add_bookmark,
            drawableResName = Drawable.drawable_ic_bookmark,
            showPointerEvent = true,
            modifier = Modifier.padding(start = 10.dp),
            tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
            iconSize = 18.dp,
            pointerInnerPadding = 4.dp
        ) {
            println("открыли меню")
        }
        Spacer(modifier = Modifier.weight(1f, fill = true))
        AnimatedVisibility(
            visible = !showRightDrawer.value,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
            exit = fadeOut(spring(stiffness = Spring.StiffnessHigh))
        ) {
            Row {
                TooltipIconArea(
                    text = Strings.sidebar,
                    drawableResName = Drawable.drawable_ic_note_sidebar,
                    showPointerEvent = true,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = openRightDrawerListener,
                )
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
                        onClick = onFullscreen
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
                        onClick = onFullscreen
                    )
                }
                TooltipIconArea(
                    text = Strings.close,
                    drawableResName = Drawable.drawable_ic_close,
                    showPointerEvent = true,
                    modifier = Modifier.padding(start = 10.dp, end = 16.dp),
                    tooltipOffset = DpOffset(x = (-60).dp, y = (-70).dp),
                    iconSize = 18.dp,
                    pointerInnerPadding = 4.dp,
                    onClick = onClose
                )
            }
        }
    }
}