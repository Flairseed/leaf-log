package com.example.leaflog.feature_charts.presentation.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leaflog.core.presentation.component.CustomButton
import com.example.leaflog.feature_charts.presentation.component.BarChart
import com.example.leaflog.feature_charts.presentation.component.DropDownEntry
import com.example.leaflog.feature_charts.presentation.component.LineChart
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = viewModel(),
    snackBarHostState: SnackbarHostState,
    padding: PaddingValues
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is AnalyticsViewModel.UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                }
            }
        }
    }

    val state = viewModel.state

    val canGoBack = state.currentLogsPage > 0
    val canGoNext = state.currentLogsPage < state.logPages.lastIndex

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if(state.journals.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("There are currently no journals")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .padding(horizontal = 10.dp, vertical = 30.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            DropDownEntry(
                valuesList = state.journals.map {
                    it.title
                },
                value = state.currentJournalIndex,
                label = "Journal"
            ) {
                viewModel.changeJournalIndex(it)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomButton(
                    modifier = Modifier.width(180.dp),
                    label = "Refresh Journals",
                    leadingIcon = Icons.Default.Refresh
                ) {
                    viewModel.getJournals()
                }
                CustomButton(
                    modifier = Modifier.width(180.dp),
                    label = "Refresh Logs",
                    leadingIcon = Icons.Default.Refresh
                ) {
                    viewModel.getLogs()
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.goBackPage()
                    },
                    enabled = canGoBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
                Text(text = "Page: ${state.currentLogsPage + 1}")
                IconButton(
                    onClick = {
                        viewModel.goNextPage()
                    },
                    enabled = canGoNext
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = ""
                    )
                }
            }
            if (state.logPages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("There are currently no logs in this journal")
                }
            } else {
                BarChart(
                    values = state.logPages[state.currentLogsPage].map {
                        it.water.toFloat()
                    },
                    title = "Water given over time",
                    horizontalAxis = "Log",
                    verticalAxis = "Water (ml)"
                )

                LineChart(
                    values = state.logPages[state.currentLogsPage].map {
                        it.height
                    },
                    title = "Height of plant over time",
                    horizontalAxis = "Log",
                    verticalAxis = "Height (cm)"
                )
            }
        }
    }
}