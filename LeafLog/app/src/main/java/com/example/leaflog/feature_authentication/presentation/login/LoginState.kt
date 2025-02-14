package com.example.leaflog.feature_authentication.presentation.login

data class LoginState(
    val name: String = "",
    val nameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false
)
