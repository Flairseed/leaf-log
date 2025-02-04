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
    private val db: LocalDataBase = Services.localDb,
    private val postId: Int?,
) : ViewModel() {
    var state by mutableStateOf(SetJournalState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _currentPost: Journal? = null

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object Posted: UiEvent()
        data object Deleted: UiEvent()
    }

    init {
        if (postId != null) {
            getData(postId)
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

    private fun getData(id: Int) {
        if (state.isLoading) {
            return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val journals = db.journalService().getJournalById(id)
                if (journals.isEmpty()) {
                    _eventFlow.emit(UiEvent.ShowSnackbar("Post of id $id does not exist"))
                } else {
                    _currentPost = journals[0]
                    state = state.copy(
                        title = journals[0].title,
                        description = journals[0].description,
                        picture = journals[0].picture,
                    )
                }
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while loading this post"))
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun onDelete() {
        if (state.isLoading || postId == null) {
            return
        }

        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                db.journalService().deleteJournal(
                    _currentPost!!
                )
                _eventFlow.emit(UiEvent.Deleted)
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while deleting"))
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
                    val journal = Journal(
                        id = postId ?: 0,
                        title = state.title,
                        description = state.description,
                        picture = state.picture!!,
                        created = Date.from(Instant.now())
                    )
                    if (postId == null) {
                        db.journalService().createJournal(journal)
                    } else {
                        db.journalService().updateJournal(journal)
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