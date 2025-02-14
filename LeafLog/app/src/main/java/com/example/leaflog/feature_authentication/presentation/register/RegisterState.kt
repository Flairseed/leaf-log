package com.example.leaflog.feature_authentication.presentation.register

data class RegisterState(
    val name: String = "",
    val nameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false
)
