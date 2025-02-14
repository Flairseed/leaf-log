package com.example.leaflog.feature_authentication.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leaflog.R
import com.example.leaflog.core.presentation.component.CustomButton
import com.example.leaflog.feature_authentication.presentation.component.AuthenticationEntry
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel()
) {
    val background = Color(0xFFFFF8F5)
    val primary = Color(0xFF2E5B00)
    var showPassword by remember {
        mutableStateOf(false)
    }
    val snackBarHostState = remember { SnackbarHostState() }

    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is RegisterViewModel.UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message
                    )
                } is RegisterViewModel.UiEvent.Registered -> {
                snackBarHostState.showSnackbar(
                    message = "Registered"
                )
            }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = background
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .consumeWindowInsets(it)
            .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            bottom = 10.dp
                        ),
                    painter = painterResource(R.drawable.leaf_logo),
                    contentDescription = "App logo"
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 25.dp),
                    text = "Register",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                AuthenticationEntry(
                    modifier = Modifier
                        .padding(bottom = 15.dp),
                    label = "Name",
                    value = state.name,
                    error = state.nameError,
                    isLoading = state.isLoading
                ) { value ->
                    viewModel.onNameChange(value)
                }
                AuthenticationEntry(
                    modifier = Modifier
                        .padding(bottom = 15.dp),
                    label = "Password",
                    value = state.password,
                    error = state.passwordError,
                    isLoading = state.isLoading,
                    isPassword = true,
                    showPassword = showPassword,
                    onVisibilityPressed = {
                        showPassword = !showPassword
                    }
                ) { value ->
                    viewModel.onPasswordChange(value)
                }
                AuthenticationEntry(
                    label = "Confirm Password",
                    value = state.confirmPassword,
                    error = state.confirmPasswordError,
                    isLoading = state.isLoading,
                    isPassword = true,
                    showPassword = showPassword,
                    onVisibilityPressed = {
                        showPassword = !showPassword
                    }
                ) { value ->
                    viewModel.onConfirmPasswordChange(value)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    label = "Register",
                    leadingIcon = Icons.AutoMirrored.Default.ArrowForward
                ) {
                    if (!state.isLoading) {
                        viewModel.onSubmit()
                    }
                }
                TextButton(
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        text = "Already Registered? Login",
                        color = primary,
                    )
                }
                TextButton(
                    onClick = {

                    }
                ) {
                    Text(
                        text = "Offline Mode",
                        color = primary,
                    )
                }
            }
        }
    }
}