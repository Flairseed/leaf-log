package com.example.leaflog.feature_journal.presentation.journals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
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
import com.example.leaflog.core.presentation.component.AppBar
import com.example.leaflog.core.presentation.component.AppDrawer
import com.example.leaflog.feature_authentication.data.remote.AuthService
import com.example.leaflog.feature_journal.data.model.Journal
import com.example.leaflog.feature_journal.presentation.component.Book
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun JournalScreen(
    viewModel: JournalsViewModel = viewModel(),
    onJournalClicked: (journalId: Journal) -> Unit,
    onFABClicked: () -> Unit,
    goToLoginScreen: () -> Unit
) {
    val background = Color(0xFFFFF8F5)
    val secondaryContainer = Color(0xFFB7DBC9)
    val onSecondaryContainer = Color(0xFF244537)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is JournalsViewModel.UiEvent.ShowSnackbar -> {
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
                    label = "Journals",
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
            },
            containerColor = background
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .consumeWindowInsets(it),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(top = 55.dp)
                ) {
                    items(state.journals) { book ->
                        Book(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    onJournalClicked(book)
                                },
                            title = book.title,
                            description = book.description,
                            image = book.picture
                        )
                    }
                }
            }
        }
    }
}