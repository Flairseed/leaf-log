package com.example.leaflog.feature_log.presentation.set_log

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.leaflog.core.presentation.component.CustomButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GetDataScreen(
    viewModel: SetLogViewModel,
    goBack: () -> Unit
) {
    val surface = Color(0xFFFFF8F5)
    val buttonOneColor = Color(0xFFFF5B8C)
    val buttonTwoColor = Color(0xFFFFC165)

    val state = viewModel.state

    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val lightLevelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    var hasPermission by remember { mutableStateOf(context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) }
    val getPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        hasPermission = it
    }

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is SetLogViewModel.UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                }
                is SetLogViewModel.UiEvent.Posted -> {}
                is SetLogViewModel.UiEvent.Deleted -> {}
            }
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState)},
        topBar = {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = goBack
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "")
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Get Data",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = Color.Black
                )
            }
        },
        containerColor = surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(it)
                .padding(horizontal = 60.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Get Temperature and Relative Humidity\n" +
                        "(Requires WI-FI and GPS)",
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier.padding(top = 30.dp),
                text = "Temperature : ${state.temperature ?: "?"} °C"
            )
            Text(
                modifier = Modifier.padding(top = 30.dp),
                text = "R. Humidity : ${state.relativeHumidity ?: "?"} %"
            )
            CustomButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 30.dp),
                label = "Get Data",
                leadingIcon = Icons.Outlined.LocationOn,
                color = buttonOneColor
            ) {
                if (!state.isLoading) {
                    if (hasPermission) {
                        viewModel.getWeatherData(context)
                    } else {
                        getPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                }
            }
            Text(
                modifier = Modifier
                    .padding(top = 70.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Get Light Intensity\n" +
                        "(requires light sensor)",
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier.padding(top = 30.dp),
                text = "Light : ${state.lightLevel ?: "?"} lx"
            )
            CustomButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 30.dp),
                label = "Get Data",
                leadingIcon = Icons.Outlined.Star,
                color = buttonTwoColor,
                foreground = Color.Black
            ) {
                if (!state.isLoading) {
                    viewModel.getLightData(
                        sensorManager = sensorManager,
                        sensor = lightLevelSensor
                    )
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 30.dp)
                )
            }
        }
    }
}