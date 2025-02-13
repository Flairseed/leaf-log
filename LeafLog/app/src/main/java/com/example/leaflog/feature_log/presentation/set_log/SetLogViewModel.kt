package com.example.leaflog.feature_log.presentation.set_log

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaflog.core.data.local.LocalDataBase
import com.example.leaflog.core.data.remote.HttpHandler
import com.example.leaflog.feature_log.data.model.Log
import com.example.leaflog.util.CustomSensorEventListener
import com.example.leaflog.util.Services
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.util.Date
import kotlin.math.round

class SetLogViewModel(
    private val db: LocalDataBase = Services.localDb,
    private val locationClient: FusedLocationProviderClient = Services.fusedLocationProviderClient,
    private val journalId: Int,
    private val logId: Int?
) : ViewModel() {
    var state by mutableStateOf(SetLogState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _currentLog: Log? = null

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object Posted: UiEvent()
        data object Deleted: UiEvent()
    }

    init {
        if (logId != null) {
            getData(logId)
        }
    }

    fun onTitleChange(value: String) {
        state = state.copy(title = value)
    }

    fun onDescriptionChange(value: String) {
        state = state.copy(description = value)
    }

    fun onPictureChange(value: String) {
        state = state.copy(picture = value)
    }

    fun onHeightChange(value: String) {
        if (Regex("^\\d{1,4}(\\.\\d{0,2})?\$").matches(value) || value.isEmpty()) {
            state = state.copy(height = value)
        }
    }

    fun onWaterChange(value: String) {
        if (Regex("^\\d{1,5}\$").matches(value) || value.isEmpty()) {
            state = state.copy(water = value)
        }
    }

    fun onLightLevelChange(value: Float) {
        state = state.copy(lightLevel = value)
    }

    fun getLightData(
        sensorManager: SensorManager,
        sensor: Sensor?
    ) {
        if (state.isLoading) {
            return
        }

        viewModelScope.launch {
            if (sensor == null) {
                _eventFlow.emit(UiEvent.ShowSnackbar("You do not have a light sensor"))
                return@launch
            }
            state = state.copy(isLoading = true)
            try {
                val valuesList = mutableListOf<Float>()
                val listener = CustomSensorEventListener {
                    valuesList.add(it as Float)
                }
                sensorManager.registerListener(
                    listener,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
                delay(3000)
                sensorManager.unregisterListener(listener)

                onLightLevelChange(round(valuesList.sum()/valuesList.size * 100)/100)
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while getting light data"))
            } finally {
                state = state.copy(isLoading = false)
            }

        }
    }

    fun getWeatherData(context: Context) {
        if (state.isLoading) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isLoading = true)
            try {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val location = locationClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        null
                    ).await()
                    val weatherResponse = HttpHandler.getData("${HttpHandler.WEATHER_URL}/weather?lat=${location.latitude}&lon=${location.longitude}&units=metric&APPID=071d057f6ceeaed3b5f3cd8bf2d5a651")
                    if (weatherResponse != null) {
                        val weatherJson = JSONObject(weatherResponse)
                        state = state.copy(
                            temperature = round((weatherJson["main"] as JSONObject)["temp"] as Double).toInt()
                        )
                        state = state.copy(
                            relativeHumidity = (weatherJson["main"] as JSONObject)["humidity"] as Int
                        )
                    } else {
                        _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while communicating with the server"))
                    }
                } else {
                    _eventFlow.emit(UiEvent.ShowSnackbar("Please allow this app to access your location"))
                }
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while getting weather data"))
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    private fun getData(id: Int) {
        if (state.isLoading) {
            return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val logs = db.logService().getLogById(id)
                if (logs.isEmpty()) {
                    _eventFlow.emit(UiEvent.ShowSnackbar("Log of id $id does not exist"))
                } else {
                    _currentLog = logs[0]
                    state = state.copy(
                        title = logs[0].title,
                        description = logs[0].description,
                        picture = logs[0].picture,
                        height = logs[0].height.toString(),
                        water = logs[0].height.toString(),
                        lightLevel = logs[0].lightLevel,
                        relativeHumidity = logs[0].relativeHumidity,
                        temperature = logs[0].temperature
                    )
                }
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while loading this log"))
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun onSubmit() {
        if (state.isLoading) {
            return
        }

        val requiredError = "This field is required"
        val heightError = "Must be a valid number between 0 - 9999.99"
        val waterError = "Must be a valid number between 0 - 99999"
        var hasErrors = false

        state = if (state.title.isEmpty()) {
            hasErrors = true
            state.copy(titleError = requiredError)
        } else {
            state.copy(titleError = null)
        }

        state = if (state.description.isEmpty()) {
            hasErrors = true
            state.copy(descriptionError = requiredError)
        } else {
            state.copy(descriptionError = null)
        }

        state = if (state.picture.isNullOrEmpty()) {
            hasErrors = true
            state.copy(pictureError = requiredError)
        } else {
            state.copy(pictureError = null)
        }

        state = if (state.height.isEmpty()) {
            hasErrors = true
            state.copy(heightError = requiredError)
        } else if (!Regex("^\\d{1,4}(\\.\\d{1,2})?\$").matches(state.height)) {
            hasErrors = true
            state.copy(heightError = heightError)
        } else {
            state.copy(heightError = null)
        }

        state = if (state.water.isEmpty()) {
            hasErrors = true
            state.copy(waterError = requiredError)
        } else if (!Regex("^\\d{1,5}\$").matches(state.water)) {
            hasErrors = true
            state.copy(waterError = waterError)
        } else {
            state.copy(waterError = null)
        }

        if (!hasErrors) {
            viewModelScope.launch {
                state = state.copy(isLoading = true)
                try {
                    val log = Log(
                        id = logId ?: 0,
                        journalId = journalId,
                        title = state.title,
                        description = state.description,
                        picture = state.picture!!,
                        height = state.height.toFloat(),
                        water = state.water.toInt(),
                        lightLevel = state.lightLevel,
                        temperature = state.temperature,
                        relativeHumidity = state.relativeHumidity,
                        created = Date()
                    )
                    if (logId == null) {
                        db.logService().createLog(log)
                    } else {
                        db.logService().updateLog(log)
                    }
                    _eventFlow.emit(UiEvent.Posted)
                } catch (_: Exception) {
                    _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error"))
                } finally {
                    state = state.copy(isLoading = false)
                }
            }
        }
    }
}