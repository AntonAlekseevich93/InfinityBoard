import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import di.Inject
import kotlinx.coroutines.launch
import menu_bar.LeftMenuBar
import navigation_drawer.Platform
import navigation_drawer.PlatformDrawerContent
import navigation_drawer.PlatformNavigationDrawer
import sub_app_bar.SubAppBar

@Composable
fun Application() {
    val viewModel = remember { Inject.instance<ApplicationViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showDrawer = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val showSearch = remember { mutableStateOf(false) }

    AppTheme {
        Row() {
            LeftMenuBar(
                searchListener = {
                    showSearch.value = true
                }
            )

            PlatformNavigationDrawer(
                platform = Platform.DESKTOP, //todo,
                drawerContent = {
                    PlatformDrawerContent(
                        Platform.DESKTOP, //todo,
                        closeSidebarListener = {
                            scope.launch {
                                if (!showDrawer.value) {
                                    showDrawer.value = true
                                    drawerState.open()
                                } else {
                                    showDrawer.value = false
                                    drawerState.close()
                                }
                            }
                        }
                    )
                },
                drawerState = drawerState,
                showDrawer = showDrawer
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = if (drawerState.isClosed) 0.dp else 0.dp)
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
                            isOpenedSidebar = showDrawer,
                            switchViewTypesListener = viewModel::switchViewTypesListener,
                            closeViewsTypeDropdown = viewModel::changeViewsTypes,
                            openSidebarListener = {
                                scope.launch {
                                    if (!showDrawer.value) {
                                        showDrawer.value = true
                                        drawerState.open()
                                    } else {
                                        showDrawer.value = false
                                        drawerState.close()
                                    }
                                }
                            }
                        )
                        StuffListUI()
                    }

                    CustomDockedSearchBar(showSearch, closeSearch = {
                        showSearch.value = false
                    })
                }
            }
        }
    }
}

