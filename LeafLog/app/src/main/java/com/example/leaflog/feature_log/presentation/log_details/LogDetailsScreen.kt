package com.example.leaflog.feature_log.presentation.log_details

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.leaflog.core.presentation.component.CustomButton
import com.example.leaflog.feature_authentication.data.remote.AuthService
import com.example.leaflog.feature_log.presentation.component.LogPage
import com.example.leaflog.ui.theme.schoolBellFamily
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LogDetailsScreen(
    logId: Int,
    goBack: () -> Unit,
    onDelete: () -> Unit,
    goToUpdateScreen: (Int) -> Unit
) {
    val surface = Color(0xFFFFF8F5)
    val error = Color(0xFFBA1A1A)
    val onError = Color(0xFFFFFFFF)
    val surfaceContainer = Color(0xFFF3EDE9)
    val onSurface = Color(0xFF1D1B19)
    val primary = Color(0xFF2E5B00)

    val context = LocalContext.current

    val columnScroll = rememberScrollState()
    val titleScroll = rememberScrollState()
    val descriptionScroll = rememberScrollState()
    var showDialog by remember {
        mutableStateOf(false)
    }

    val viewModel = viewModel {
        LogDetailsViewModel(logId = logId)
    }

    val state = viewModel.state
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is LogDetailsViewModel.UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                }
                is LogDetailsViewModel.UiEvent.Deleted -> {
                    onDelete()
                }
                is LogDetailsViewModel.UiEvent.SetOnline -> {
                    snackBarHostState.showSnackbar(
                        message = "Successfully set log online"
                    )
                }
                is LogDetailsViewModel.UiEvent.DeleteOnline -> {
                    snackBarHostState.showSnackbar(
                        message = "Successfully deleted log online"
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = goBack
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "")
                }
                if (AuthService.isLoggedIn()) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            if (!state.isLoading) {
                                showDialog = true
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "")
                    }
                }
            }
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                CustomButton(
                    modifier = Modifier.width(150.dp),
                    label = "Edit",
                    leadingIcon = Icons.Default.Create
                ) {
                    if (!state.isLoading) {
                        goToUpdateScreen(logId)
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
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = surface
    ) {
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = surfaceContainer,
                        contentColor = onSurface
                    )
                ) {
                    Column {
                        Text(
                            text = if (state.log?.postId == null)
                                "This log has not been posted online yet."
                            else
                                "This log has been posted online.",
                            modifier = Modifier.padding(16.dp),
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            if (state.log?.postId == null) {
                                TextButton(
                                    onClick = {
                                        showDialog = false
                                        viewModel.setLogOnline(context)
                                    },
                                    modifier = Modifier.padding(8.dp),
                                ) {
                                    Text(
                                        text= "Post",
                                        color = primary
                                    )
                                }
                            } else {
                                TextButton(
                                    onClick = {
                                        showDialog = false
                                        viewModel.setLogOnline(context)
                                    },
                                    modifier = Modifier.padding(8.dp),
                                ) {
                                    Text(
                                        text = "Update",
                                        color = primary
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        showDialog = false
                                        viewModel.deleteLogOnline()
                                    },
                                    modifier = Modifier.padding(8.dp),
                                ) {
                                    Text(
                                        text = "Delete",
                                        color = primary
                                    )
                                }
                            }
                            TextButton(
                                onClick = { showDialog = false },
                                modifier = Modifier.padding(8.dp),
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = primary
                                )
                            }
                        }
                    }
                }
            }
        }
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.log != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .consumeWindowInsets(it)
                    .verticalScroll(columnScroll),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogPage {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 49.dp,
                                top = 17.dp
                            )
                            .horizontalScroll(titleScroll),
                        text = state.log.title,
                        fontSize = 36.sp,
                        fontFamily = schoolBellFamily,
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .padding(
                                top = 80.dp,
                                start = 116.dp
                            )
                            .width(136.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black
                                )
                                .padding(
                                    top = 13.dp,
                                    start = 13.dp,
                                    end = 13.dp
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .fillMaxWidth()
                                    .background(Color.Black),
                                model = state.log.picture,
                                contentDescription = ""
                            )
                            Text(
                                text = DateFormat.format("dd/MM/yyyy", state.log.created).toString(),
                                fontSize = 20.sp,
                                fontFamily = schoolBellFamily
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(
                                top = 247.dp,
                                start = 55.dp
                            )
                    ) {
                        Text(
                            text = "Height: ${state.log.height} cm",
                            fontFamily = schoolBellFamily,
                            fontSize = 15.sp
                        )
                        Text(
                            modifier = Modifier.padding(top = 28.dp),
                            text = "Water: ${state.log.water} ml",
                            fontFamily = schoolBellFamily,
                            fontSize = 15.sp
                        )
                        Text(
                            modifier = Modifier.padding(top = 28.dp),
                            text = "Light: ${state.log.lightLevel ?: "?"} lx",
                            fontFamily = schoolBellFamily,
                            fontSize = 15.sp
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(
                                top = 247.dp,
                                end = 16.dp
                            ),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Temperature: ${state.log.temperature ?: "?"} °C",
                            fontFamily = schoolBellFamily,
                            fontSize = 15.sp
                        )
                        Text(
                            modifier = Modifier.padding(top = 28.dp),
                            text = "R. Humidity: ${state.log.relativeHumidity ?: "?"} %",
                            fontFamily = schoolBellFamily,
                            fontSize = 15.sp
                        )
                    }
                }

                LogPage(
                    modifier = Modifier.padding(
                        vertical = 30.dp
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 17.dp, start = 35.dp),
                        text = "Log",
                        fontFamily = schoolBellFamily,
                        fontSize = 36.sp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .padding(
                                top = 85.dp,
                                start = 54.dp,
                                end = 6.dp
                            )
                            .verticalScroll(descriptionScroll),
                        text = state.log.description,
                        lineHeight = 26.sp,
                        fontFamily = schoolBellFamily,
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "There has been an error while loading the data",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}