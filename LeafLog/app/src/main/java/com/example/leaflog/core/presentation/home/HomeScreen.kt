package com.example.leaflog.core.presentation.home

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.leaflog.R
import com.example.leaflog.core.presentation.component.AppBar
import com.example.leaflog.core.presentation.component.AppDrawer
import com.example.leaflog.feature_authentication.data.remote.AuthService
import com.example.leaflog.feature_journal.data.model.Journal
import com.example.leaflog.feature_journal.presentation.journals.JournalScreen
import com.example.leaflog.feature_online_post.data.model.GetPostModel
import com.example.leaflog.feature_online_post.presentation.posts.PostsScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    goToLoginScreen: () -> Unit,
    onFABClicked: () -> Unit,
    onJournalClicked: (Journal) -> Unit,
    onPostClicked: (GetPostModel) -> Unit
) {
    val background = Color(0xFFFFF8F5)
    val surfaceContainer = Color(0xFFF3EDE9)
    val onSurface = Color(0xFF1D1B19)
    val onSurfaceVariant = Color(0xFF4D453E)
    val secondaryContainer = Color(0xFFB7DBC9)
    val onSecondaryContainer = Color(0xFF244537)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val label = when (selectedIndex) {
        0 -> "Journals"
        1 -> "Explore"
        2 -> "Analytics"
        else -> ""
    }

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                modifier = Modifier.width(300.dp)
            ) {
                if (AuthService.isLoggedIn()) {
                    AuthService.logOut()
                }
                goToLoginScreen()
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
            topBar = {
                AppBar(
                    label = label,
                    onProfileClicked = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                if (selectedIndex == 0) {
                    LargeFloatingActionButton(
                        containerColor = secondaryContainer,
                        contentColor = onSecondaryContainer,
                        onClick = onFABClicked
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = ""
                        )
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = surfaceContainer
                ) {
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors().copy(
                            unselectedIconColor = onSurfaceVariant,
                            unselectedTextColor = onSurfaceVariant,
                            selectedIndicatorColor = secondaryContainer,
                            selectedIconColor = onSecondaryContainer,
                            selectedTextColor = onSurface,
                        ),
                        selected = selectedIndex == 0,
                        onClick = {
                            selectedIndex = 0
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_book_24),
                                contentDescription = ""
                            )
                        },
                        label = {
                            Text("Journals")
                        }
                    )
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors().copy(
                            unselectedIconColor = onSurfaceVariant,
                            unselectedTextColor = onSurfaceVariant,
                            selectedIndicatorColor = secondaryContainer,
                            selectedIconColor = onSecondaryContainer,
                            selectedTextColor = onSurface,
                        ),
                        selected = selectedIndex == 1,
                        onClick = {
                            selectedIndex = 1
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_explore_24),
                                contentDescription = ""
                            )
                        },
                        label = {
                            Text("Explore")
                        }
                    )
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors().copy(
                            unselectedIconColor = onSurfaceVariant,
                            unselectedTextColor = onSurfaceVariant,
                            selectedIndicatorColor = secondaryContainer,
                            selectedIconColor = onSecondaryContainer,
                            selectedTextColor = onSurface,
                        ),
                        selected = selectedIndex == 2,
                        onClick = {
                            selectedIndex = 2
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_analytics_24),
                                contentDescription = ""
                            )
                        },
                        label = {
                            Text("Analytics")
                        }
                    )
                }
            },
            containerColor = background
        ) {
            when (selectedIndex) {
                0 -> JournalScreen(
                    onJournalClicked = onJournalClicked,
                    snackBarHostState = snackBarHostState,
                    padding = it
                )
                1 -> PostsScreen(
                    onPostClicked = onPostClicked,
                    snackBarHostState = snackBarHostState,
                    padding = it
                )
            }
        }
    }
}