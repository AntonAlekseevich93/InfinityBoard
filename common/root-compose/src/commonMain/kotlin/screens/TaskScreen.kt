package screens

import ApplicationUiState
import ApplicationViewModel
import CustomDockedSearchBar
import StuffListUI
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import menu_bar.LeftMenuBar
import navigation_drawer.Platform
import navigation_drawer.PlatformLeftDrawerContent
import navigation_drawer.PlatformNavigationDrawer
import sub_app_bar.SubAppBar


@Composable
fun TaskScreen(
    uiState: ApplicationUiState,
    showLeftDrawer: MutableState<Boolean>,
    showNote: MutableState<Boolean>,
    showSearch: MutableState<Boolean>,
    leftDrawerState: DrawerState,
    viewModel: ApplicationViewModel,
) {
    val scope = rememberCoroutineScope()
    Row {
        LeftMenuBar(
            searchListener = {
                showSearch.value = true
            }
        )

        PlatformNavigationDrawer(
            platform = Platform.DESKTOP, //todo,
            leftDrawerContent = {
                PlatformLeftDrawerContent(
                    Platform.DESKTOP, //todo,
                    closeSidebarListener = {
                        scope.launch {
                            if (!showLeftDrawer.value) {
                                showLeftDrawer.value = true
                                leftDrawerState.open()
                            } else {
                                showLeftDrawer.value = false
                                leftDrawerState.close()
                            }
                        }
                    }
                )
            },
            leftDrawerState = leftDrawerState,
            showLeftDrawer = showLeftDrawer
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = if (leftDrawerState.isClosed) 0.dp else 0.dp)
                        .fillMaxSize()
                        .background(ApplicationTheme.colors.mainBackgroundColor),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubAppBar(
                        modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                        projectName = "Мой личный проект",
                        selectedViewsTypes = uiState.selectedViewTypes,
                        isCheckedTypes = uiState.checkedViewTypes,
                        isOpenedType = uiState.openedViewType.value,
                        openViewType = viewModel::openViewType,
                        isOpenedSidebar = showLeftDrawer,
                        switchViewTypesListener = viewModel::switchViewTypesListener,
                        closeViewsTypeDropdown = viewModel::changeViewsTypes,
                        openSidebarListener = {
                            scope.launch {
                                if (!showLeftDrawer.value) {
                                    showLeftDrawer.value = true
                                    leftDrawerState.open()
                                } else {
                                    showLeftDrawer.value = false
                                    leftDrawerState.close()
                                }
                            }
                        }
                    )
                    StuffListUI(
                        openCard = {
                            showNote.value = true
                        }
                    )
                }

                CustomDockedSearchBar(
                    showSearch = showSearch,
                    closeSearch = {
                        showSearch.value = false
                    },
                )
            }
        }
    }
}
