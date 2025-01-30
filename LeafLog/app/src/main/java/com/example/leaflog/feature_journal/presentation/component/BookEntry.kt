package com.example.leaflog.feature_journal.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookEntry(
    modifier: Modifier = Modifier,
    placeholder: String,
    value: String,
    error: String? = null,
    isLoading: Boolean = false,
    multiline: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    val background = Color(0xFF881A1A)
    val line = Color(0xFF000000)
    val placeholderColor = Color(0x80FFFFFF)
    val textColor = Color(0xFFFFFFFF)

    val hasError = error != null
    Box(modifier = modifier,) {
        TextField(
            value = value,
            isError = hasError,
            onValueChange = onValueChange,
            enabled = !isLoading,
            singleLine = !multiline,
            minLines = if (multiline) 3 else 1,
            placeholder = {Text(
                modifier = Modifier.fillMaxWidth(),
                text = placeholder,
                textAlign = TextAlign.Center,
                fontSize = if (multiline) 16.sp else 24.sp,
                fontWeight = if (multiline) FontWeight.Normal else FontWeight.Medium,
            )},
            supportingText = {
                if (hasError) {
                    Text(text = error!!)
                }
            },
            textStyle = LocalTextStyle.current.copy(
                textAlign = if (multiline) TextAlign.Start else TextAlign.Center,
                fontSize = if (multiline) 16.sp else 24.sp,
                fontWeight = if (multiline) FontWeight.Normal else FontWeight.Medium,
            ),
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = background,
                focusedContainerColor = background,
                errorContainerColor = background,
                disabledContainerColor = background,
                unfocusedIndicatorColor = if (multiline) Color.Transparent else line,
                focusedIndicatorColor = if (multiline) Color.Transparent else line,
                disabledIndicatorColor = if (multiline) Color.Transparent else line,
                errorIndicatorColor = if (multiline) Color.Transparent else TextFieldDefaults.colors().errorIndicatorColor,
                unfocusedPlaceholderColor = placeholderColor,
                focusedPlaceholderColor = placeholderColor,
                errorPlaceholderColor = placeholderColor,
                disabledPlaceholderColor = placeholderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                errorTextColor = textColor,
                disabledTextColor = textColor,
            ),
            shape = if (multiline) MaterialTheme.shapes.extraSmall else RectangleShape
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
fun BookEntryPreview() {
    BookEntry(
        modifier = Modifier.width(280.dp),
        value = "",
        placeholder = "Title",
        multiline = true,
    ) {

    }
}