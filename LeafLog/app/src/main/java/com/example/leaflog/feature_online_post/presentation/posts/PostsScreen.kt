package com.example.leaflog.feature_online_post.presentation.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leaflog.feature_log.presentation.component.LogSimple
import kotlinx.coroutines.flow.collectLatest
import com.example.leaflog.feature_online_post.data.model.GetPostModel
import com.example.leaflog.core.presentation.component.AppBar
import com.example.leaflog.core.presentation.component.AppDrawer
import com.example.leaflog.core.presentation.component.CustomButton
import com.example.leaflog.feature_authentication.data.remote.AuthService
import kotlinx.coroutines.launch

@Composable
fun PostsScreen(
    viewModel: PostsViewModel = viewModel(),
    goToLoginScreen: () -> Unit,
    onPostClicked: (GetPostModel) -> Unit
) {
    val background = Color(0xFFFFF8F5)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is PostsViewModel.UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                }
            }
        }
    }

    val state = viewModel.state
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
                    label = "Explore",
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
            containerColor = background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .consumeWindowInsets(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(
                    modifier = Modifier.padding(vertical = 20.dp),
                    label = "Refresh",
                    leadingIcon = Icons.Default.Refresh
                ) {
                    viewModel.getData()
                }
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(top = 55.dp)
                    ) {
                        items(state.posts) { post ->
                            LogSimple(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        onPostClicked(post)
                                    },
                                title = post.title,
                                picture = post.picture,
                                created = post.created,
                                author = post.name
                            )
                        }
                    }
                }
            }
        }
    }
}