package com.example.leaflog.feature_journal.presentation.journals

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

class JournalsViewModel(
    private val db: LocalDataBase = Services.localDb
) : ViewModel() {
    var state by mutableStateOf(JournalsState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent() {
        data class ShowSnackbar(val message: String) : UiEvent()
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
                    journals = db.journalService().getAllJournals()
                )
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while loading journals"))
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}