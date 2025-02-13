package com.example.leaflog.feature_log.presentation.set_log

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leaflog.core.presentation.component.CustomButton
import com.example.leaflog.feature_log.presentation.component.LogEntry
import com.example.leaflog.feature_log.presentation.component.LogImageSelector
import com.example.leaflog.feature_log.presentation.component.LogPage
import com.example.leaflog.ui.theme.schoolBellFamily
import kotlinx.coroutines.flow.collectLatest
import java.util.Date

@Composable
fun SetLogScreen(
    viewModel: SetLogViewModel,
    onGetData: () -> Unit,
    onPost: () -> Unit,
    onDelete: (() -> Unit)? = null,
    goBack: () -> Unit
) {
    val surface = Color(0xFFFFF8F5)
    val buttonBorder = Color(0xFFACBDA8)
    val buttonBackground = Color(0xFFFFFDC8)
    val buttonForeground = Color(0xFF000000)

    val columnScroll = rememberScrollState()

    val state = viewModel.state
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is SetLogViewModel.UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                } is SetLogViewModel.UiEvent.Posted -> {
                    onPost()
                } is SetLogViewModel.UiEvent.Deleted -> {
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
        floatingActionButton = {
            CustomButton(
                modifier = Modifier.width(250.dp),
                label = "Confirm",
                leadingIcon = Icons.Default.Create
            ) {
                if (!state.isLoading) {
                    viewModel.onSubmit()
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(it)
                .verticalScroll(columnScroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogPage {
                LogEntry(
                    modifier = Modifier
                        .padding(start = 49.dp),
                    placeholder = "Title",
                    value = state.title,
                    fontSize = 36.sp,
                    height = 63.dp,
                    error = state.titleError,
                    isLoading = state.isLoading
                ) { value ->
                    viewModel.onTitleChange(value)
                }

                LogImageSelector(
                    modifier = Modifier
                        .padding(
                            top = 80.dp,
                            start = 116.dp
                        )
                        .width(136.dp),
                    value = state.picture,
                    created = Date(),
                    error = state.pictureError,
                    isLoading = state.isLoading
                ) {value ->
                    viewModel.onPictureChange(value)
                }

                Column(
                    modifier = Modifier
                        .padding(
                            top = 240.dp,
                            start = 55.dp
                        )
                ) {
                    Row {
                        Text(
                            text = "Height:",
                            fontFamily = schoolBellFamily,
                            fontSize = 16.sp
                        )
                        LogEntry(
                            modifier = Modifier
                                .width(41.dp),
                            placeholder = "",
                            value = state.height,
                            height = 26.dp,
                            isLoading = state.isLoading
                        ) {value ->
                            viewModel.onHeightChange(value)
                        }
                        Text(
                            text = "cm",
                            fontFamily = schoolBellFamily,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = state.heightError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontFamily = schoolBellFamily
                    )
                    Row(
                        modifier = Modifier.padding(top = 3.dp)
                    ) {
                        Text(
                            text = "Water:",
                            fontFamily = schoolBellFamily,
                            fontSize = 16.sp
                        )
                        LogEntry(
                            modifier = Modifier
                                .width(41.dp),
                            placeholder = "",
                            value = state.water,
                            height = 26.dp,
                            isLoading = state.isLoading
                        ) {value ->
                            viewModel.onWaterChange(value)
                        }
                        Text(
                            text = "ml",
                            fontFamily = schoolBellFamily,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = state.waterError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontFamily = schoolBellFamily
                    )
                    Text(
                        modifier = Modifier.padding(top = 3.dp),
                        text = "Light: ${state.lightLevel ?: "?"} lx",
                        fontFamily = schoolBellFamily,
                        fontSize = 16.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            top = 240.dp,
                            end = 16.dp
                        ),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Temperature: ${state.temperature ?: "?"} °C",
                        fontFamily = schoolBellFamily,
                        fontSize = 16.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 27.dp),
                        text = "R. Humidity: ${state.relativeHumidity ?: "?"} %",
                        fontFamily = schoolBellFamily,
                        fontSize = 16.sp
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 10.dp, end = 16.dp),
                    onClick = {
                        if (!state.isLoading) {
                            onGetData()
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 30.dp, vertical = 18.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = buttonBackground,
                        contentColor = buttonForeground
                    ),
                    border = BorderStroke(width = 2.dp, color = buttonBorder)
                ) {
                    Text(
                        text = "Get Data",
                        fontFamily = schoolBellFamily,
                        fontSize = 16.sp
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
                LogEntry(
                    modifier = Modifier
                        .padding(
                            top = 80.dp,
                            start = 54.dp,
                            end = 6.dp
                        ),
                    placeholder = "",
                    value = state.description,
                    multiline = true,
                    lineHeight = 26.sp,
                    error = state.descriptionError,
                    isLoading = state.isLoading
                ) {value ->
                    viewModel.onDescriptionChange(value)
                }
            }
        }
    }
}