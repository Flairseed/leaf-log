package com.example.leaflog.feature_charts.presentation.analytics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaflog.core.data.local.LocalDataBase
import com.example.leaflog.feature_authentication.data.remote.AuthService
import com.example.leaflog.util.Services
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val db: LocalDataBase = Services.localDb
) : ViewModel() {
    var state by mutableStateOf(AnalyticsState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }

    fun changeJournalIndex(value: Int) {
        state = state.copy(currentJournalIndex = value)
        getLogs()
    }

    fun goBackPage() {
        if (state.currentLogsPage > 0) {
            state = state.copy(
                currentLogsPage = state.currentLogsPage - 1
            )
        }
    }

    fun goNextPage() {
        if (state.currentLogsPage < state.logPages.lastIndex) {
            state = state.copy(currentLogsPage = state.currentLogsPage + 1)
        }
    }


    init {
        getJournals()
    }

    fun getJournals() {
        if (state.isLoading) {
            return
        }
        var loadLogs = false

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val journals = if (AuthService.isLoggedIn())
                    db.journalService().getAllJournals()
                else
                    db.journalService().getAllJournalsWithoutAssociatedUser()
                state = state.copy(
                    journals = journals,
                    currentJournalIndex = 0
                )
                if (journals.isNotEmpty()) {
                    loadLogs = true
                }
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while loading journal data"))
            } finally {
                state = state.copy(isLoading = false)
                if (loadLogs) {
                    getLogs()
                }
            }
        }
    }

    fun getLogs() {
        if (state.isLoading) {
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val logs = db.logService().getAllLogsByJournalId(state.journals[state.currentJournalIndex].id)
                val logPages = logs.chunked(7)
                state = state.copy(
                    logPages = logPages,
                    currentLogsPage = logPages.lastIndex
                )
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while loading log data"))
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}