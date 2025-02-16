package com.example.leaflog.feature_log.presentation.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leaflog.feature_log.data.model.Log
import com.example.leaflog.feature_log.presentation.component.LogSimple
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LogsScreen(
    journalId: Int,
    goBack: () -> Unit,
    onJournalClicked: (log: Log) -> Unit,
    onFABClicked: () -> Unit,
) {
    val background = Color(0xFFFFF8F5)
    val secondaryContainer = Color(0xFFB7DBC9)
    val onSecondaryContainer = Color(0xFF244537)
    val primary = Color(0xFF2E5B00)
    val snackBarHostState = remember { SnackbarHostState() }

    val viewModel = viewModel {
        LogsViewModel(journalId = journalId)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is LogsViewModel.UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                }
            }
        }
    }

    val state = viewModel.state
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .background(color = primary)
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = goBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Entries",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = Color.White
                )
            }
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
                contentPadding = PaddingValues(top = 55.dp, bottom = 110.dp)
            ) {
                items(state.logs) { log ->
                    LogSimple(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                onJournalClicked(log)
                            }
                        ,
                        title = log.title,
                        picture = log.picture,
                        created = log.created
                    )
                }
            }
        }
    }
}