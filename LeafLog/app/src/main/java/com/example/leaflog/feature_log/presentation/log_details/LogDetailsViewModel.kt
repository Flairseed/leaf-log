package com.example.leaflog.feature_log.presentation.log_details

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaflog.core.data.local.LocalDataBase
import com.example.leaflog.core.data.remote.HttpHandler
import com.example.leaflog.feature_authentication.data.remote.AuthService
import com.example.leaflog.feature_online_post.data.model.SetPostModel
import com.example.leaflog.feature_online_post.data.remote.OnlinePostService
import com.example.leaflog.util.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

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
        data object SetOnline: UiEvent()
        data object DeleteOnline: UiEvent()
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

    fun setLogOnline(context: Context) {
        if (state.isLoading || state.log == null || !AuthService.isLoggedIn()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            var success = false
            state = state.copy(isLoading = true)
            try {
                val post = SetPostModel(
                    userId = AuthService.userId!!,
                    title = state.log!!.title,
                    picture = state.log!!.picture,
                    description = state.log!!.description,
                    height = state.log!!.height,
                    water = state.log!!.water,
                    relativeHumidity = state.log!!.relativeHumidity,
                    temperature = state.log!!.relativeHumidity,
                    lightLevel = state.log!!.lightLevel,
                    created = state.log!!.created
                )
                when (val response = OnlinePostService.setPost(post, state.log!!.postId, context)) {
                    null -> {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Internal server error"))

                    }
                    HttpHandler.FORBIDDEN -> {
                        _eventFlow.emit(UiEvent.ShowSnackbar("You are not allowed to modify this file"))
                    }
                    else -> {
                        if (state.log!!.postId == null) {
                            db.logService().updateLog(
                                state.log!!.copy(
                                    postId = (JSONObject(response)["body"] as JSONObject)["id"] as Int
                                )
                            )
                        }
                        _eventFlow.emit(UiEvent.SetOnline)
                        success = true
                    }
                }
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while uploading"))
            } finally {
                state = state.copy(isLoading = false)
                if (success) getData()
            }
        }
    }

    fun deleteLogOnline() {
        if (state.isLoading || AuthService.userId == null || state.log?.postId == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            var success = false
            state = state.copy(isLoading = true)
            try {
                when (OnlinePostService.deletePost(AuthService.userId!!, state.log!!.postId!!)) {
                    null -> {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Internal server error"))

                    }
                    HttpHandler.FORBIDDEN -> {
                        _eventFlow.emit(UiEvent.ShowSnackbar("You are not allowed to delete this file"))
                    }
                    else -> {
                        db.logService().updateLog(
                            state.log!!.copy(
                                postId = null
                            )
                        )
                        _eventFlow.emit(UiEvent.DeleteOnline)
                        success = true
                    }
                }
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while deleting online post"))
            } finally {
                state = state.copy(isLoading = false)
                if (success) getData()
            }
        }
    }
}