package com.example.leaflog.feature_log.presentation.log_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaflog.core.data.local.LocalDataBase
import com.example.leaflog.util.Services
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LogDetailsViewModel(
    private val db: LocalDataBase = Services.localDb,
    private val logId: Int
) : ViewModel() {
    var state by mutableStateOf(LogDetailsState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent() {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object Deleted: UiEvent()
    }

    init {
        getData()
    }

    fun getData() {
        if (state.isLoading) {
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                state = state.copy(
                    log = db.logService().getLogById(logId)[0]
                )
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while loading the log"))
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun onDelete() {
        if (state.isLoading || state.log == null) {
            return
        }

        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                db.logService().deleteLog(
                    state.log!!
                )
                _eventFlow.emit(UiEvent.Deleted)
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while deleting"))
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}