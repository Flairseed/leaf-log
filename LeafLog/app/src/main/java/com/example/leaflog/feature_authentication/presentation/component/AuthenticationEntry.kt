package com.example.leaflog.feature_authentication.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.leaflog.R
import com.example.leaflog.ui.theme.LeafLogTheme

@Composable
fun AuthenticationEntry(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    isLoading: Boolean = false,
    isPassword: Boolean = false,
    showPassword: Boolean = true,
    onVisibilityPressed: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    val background = Color(0xFFB7DBC9)
    val line = Color(0xFF1D1B19)
    val textColor = Color(0xFF244537)

    val hasError = error != null
    Box(modifier = modifier,) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = label)
            },
            value = value,
            isError = hasError,
            onValueChange = onValueChange,
            enabled = !isLoading,
            singleLine = true,
            supportingText = {
                if (hasError) {
                    Text(text = error!!)
                }
            },
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = {
                        if (onVisibilityPressed != null) {
                            onVisibilityPressed()
                        }
                    }) {
                        Icon(
                            painter = if (showPassword)
                                    painterResource(id = R.drawable.baseline_visibility_24)
                                else
                                painterResource(id = R.drawable.outline_visibility_24),
                            contentDescription = "Eye"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = background,
                focusedContainerColor = background,
                errorContainerColor = background,
                disabledContainerColor = background,
                unfocusedIndicatorColor = line,
                focusedIndicatorColor = line,
                disabledIndicatorColor = line,
                errorIndicatorColor = TextFieldDefaults.colors().errorIndicatorColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                errorTextColor = textColor,
                disabledTextColor = textColor,
                focusedLabelColor = line,
                unfocusedLabelColor = line,
                disabledLabelColor = line,
                unfocusedTrailingIconColor = line,
                focusedTrailingIconColor = line,
                disabledTrailingIconColor = line,
                cursorColor = line
            ),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation('*')
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAuthenticationEntry() {
    LeafLogTheme {
        AuthenticationEntry(
            label = "Name",
            value = "John Doe"
        ) {}
    }
}