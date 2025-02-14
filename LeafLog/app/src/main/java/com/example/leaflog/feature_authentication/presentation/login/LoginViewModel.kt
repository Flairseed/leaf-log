package com.example.leaflog.feature_authentication.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaflog.feature_authentication.data.remote.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent() {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object LoggedIn: UiEvent()
    }

    fun onNameChange(value: String) {
        if (Regex("^.{0,256}\$").matches(value)) {
            state = state.copy(name = value)
        }
    }

    fun onPasswordChange(value: String) {
        if (Regex("^.{0,256}\$").matches(value)) {
            state = state.copy(password = value)
        }
    }

    fun onSubmit() {
        if (state.isLoading) {
            return
        }

        val requiredError = "This field is required"
        val lengthError = "This field cannot be more than 256 characters"
        var hasErrors = false

        state = if (state.name.isEmpty()) {
            hasErrors = true
            state.copy(nameError = requiredError)
        } else if (state.name.length > 256) {
            hasErrors = true
            state.copy(nameError = lengthError)
        } else {
            state.copy(nameError = null)
        }

        state = if (state.password.isEmpty()) {
            hasErrors = true
            state.copy(passwordError = requiredError)
        } else if (state.name.length > 256) {
            hasErrors = true
            state.copy(passwordError = lengthError)
        } else {
            state.copy(passwordError = null)
        }

        if (!hasErrors) {
            viewModelScope.launch(Dispatchers.IO) {
                state = state.copy(isLoading = true)
                try {
                    val loginResponse = AuthService.login(
                        name = state.name,
                        password = state.password
                    )
                    if (loginResponse != null) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(loginResponse))
                    } else {
                        _eventFlow.emit(UiEvent.LoggedIn)
                    }
                } catch (_: Exception) {
                    _eventFlow.emit(UiEvent.ShowSnackbar("There has been an error while logging in"))
                } finally {
                    state = state.copy(isLoading = false)
                }
            }
        }
    }
}