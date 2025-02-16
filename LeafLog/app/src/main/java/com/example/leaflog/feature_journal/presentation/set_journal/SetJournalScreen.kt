package com.example.leaflog.feature_journal.presentation.set_journal

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.leaflog.core.presentation.component.CustomButton
import com.example.leaflog.feature_journal.presentation.component.BookEntry
import com.example.leaflog.feature_journal.presentation.component.BookImageSelector
import com.example.leaflog.ui.theme.LeafLogTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SetJournalScreen(
    postId: Int? = null,
    goBack: () -> Unit,
    onPost: () -> Unit,
    onDelete: (() -> Unit)? = null,
) {
    val background = Color(0xFFF77171)
    val error = Color(0xFFBA1A1A)
    val onError = Color(0xFFFFFFFF)

    val viewModel = viewModel {
        SetJournalViewModel(postId = postId)
    }
    val state = viewModel.state
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is SetJournalViewModel.UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                } is SetJournalViewModel.UiEvent.Posted -> {
                    onPost()
                } is SetJournalViewModel.UiEvent.Deleted -> {
                    if (onDelete != null) {
                        onDelete()
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Box(modifier = Modifier.height(80.dp)) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = goBack
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "")
                }
            }
        },
        containerColor = background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(it)
                .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .verticalScroll(scrollState)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BookImageSelector(
                    modifier = Modifier
                        .padding(bottom = 40.dp)
                        .width(250.dp),
                    value = state.picture,
                    error = state.pictureError,
                    isLoading = state.isLoading,
                ) { value ->
                    viewModel.onPictureChange(value)
                }
                BookEntry(
                    modifier = Modifier.padding(bottom = 20.dp),
                    placeholder = "Title",
                    value = state.title,
                    error = state.titleError,
                    isLoading = state.isLoading,
                ) { value ->
                    viewModel.onTitleChange(value)
                }
                BookEntry(
                    placeholder = "Description",
                    value = state.description,
                    error = state.descriptionError,
                    isLoading = state.isLoading,
                    multiline = true
                ) { value ->
                    viewModel.onDescriptionChange(value)
                }
            }
            if (postId == null) {
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp),
                    label = "Confirm",
                    leadingIcon = Icons.Outlined.Create,
                ) {
                    if (!state.isLoading) {
                        viewModel.onSubmit()
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    CustomButton(
                        modifier = Modifier.width(150.dp),
                        label = "Confirm",
                        leadingIcon = Icons.Default.Create
                    ) {
                        if (!state.isLoading) {
                            viewModel.onSubmit()
                        }
                    }
                    CustomButton(
                        modifier = Modifier.width(150.dp),
                        label = "Delete",
                        leadingIcon = Icons.Outlined.Delete,
                        color = error,
                        foreground = onError
                    ) {
                        if (!state.isLoading) {
                            viewModel.onDelete()
                        }
                    }
                }
            }
        }
    }
}