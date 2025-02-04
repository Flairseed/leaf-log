package com.example.leaflog.feature_journal.presentation.set_journal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaflog.core.data.local.LocalDataBase
import com.example.leaflog.feature_journal.data.model.Journal
import com.example.leaflog.util.Services
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

class SetJournalViewModel(
    private val db: LocalDataBase = Services.localDb
) : ViewModel() {
    var state by mutableStateOf(SetJournalState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent() {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object Posted: UiEvent()
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

    fun onSubmit() {
        if (state.isLoading) {
            return
        }

        val requiredError = "This field is required"
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

        if (!hasErrors) {
            viewModelScope.launch {
                state = state.copy(isLoading = true)
                try {
                    db.journalService().createJournal(
                        Journal(
                            id = 0,
                            title = state.title,
                            description = state.description,
                            picture = state.picture!!,
                            created = Date.from(Instant.now())
                        )
                    )
                    _eventFlow.emit(UiEvent.Posted)
                } catch (_: Exception) {
                    _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error"))
                }
                finally {
                    state = state.copy(isLoading = false)
                }
            }
        }
    }
}