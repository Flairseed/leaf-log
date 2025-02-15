package com.example.leaflog.feature_online_post.presentation.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leaflog.feature_log.presentation.component.LogSimple
import kotlinx.coroutines.flow.collectLatest
import com.example.leaflog.feature_online_post.data.model.GetPostModel
import com.example.leaflog.core.presentation.component.CustomButton

@Composable
fun PostsScreen(
    viewModel: PostsViewModel = viewModel(),
    onPostClicked: (GetPostModel) -> Unit,
    padding: PaddingValues,
    snackBarHostState: SnackbarHostState
) {


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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding),
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