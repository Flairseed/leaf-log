package com.example.leaflog.feature_online_post.presentation.posts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaflog.feature_online_post.data.remote.OnlinePostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {
    var state by mutableStateOf(PostsState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }

    init {
        getData()
    }

    fun getData() {
        if (state.isLoading) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isLoading = true)
            try {
                val posts = OnlinePostService.getPosts()
                if (posts == null) {
                    _eventFlow.emit(UiEvent.ShowSnackbar("Internal server error"))
                } else {
                    state = state.copy(
                        posts = posts
                    )
                }
            } catch (e: Exception) {
                println(e)
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while loading logs"))
            } finally {
                state = state.copy(isLoading = false, initialized = true)
            }
        }
    }
}