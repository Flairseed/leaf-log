package com.example.leaflog.feature_log.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leaflog.ui.theme.LeafLogTheme
import com.example.leaflog.ui.theme.schoolBellFamily

@Composable
fun LogEntry(
    modifier: Modifier = Modifier,
    placeholder: String,
    value: String,
    fontSize: TextUnit = 16.sp,
    height: Dp = Dp.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    error: String? = null,
    isLoading: Boolean = false,
    multiline: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    val background = Color(0x33717171)
    val line = Color(0xFF000000)
    val placeholderColor = Color(0xFF828282)
    val textColor = Color(0xFF000000)

    val hasError = error != null
    Column(modifier = modifier) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = background,
                        shape = if (multiline) MaterialTheme.shapes.extraSmall else RectangleShape
                    )
                    .border(
                        width = if (multiline) 1.dp else Dp.Unspecified,
                        color = if (hasError) MaterialTheme.colorScheme.error else line,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .height(height)

            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    value = value,
                    onValueChange = onValueChange,
                    enabled = !isLoading,
                    singleLine = !multiline,
                    minLines = if (multiline) 3 else 1,
                    maxLines = 10,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = if (multiline) TextAlign.Start else TextAlign.Center,
                        fontSize = fontSize,
                        fontFamily = schoolBellFamily,
                        fontWeight = if (multiline) FontWeight.Normal else FontWeight.Medium,
                        color = textColor,
                        lineHeight = lineHeight
                    ),
                )
                if (!multiline) {
                    HorizontalDivider(
                        modifier = Modifier
                            .align(Alignment.BottomCenter),
                        color = if (hasError) MaterialTheme.colorScheme.error else line
                    )
                }
                if (value.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                        ,
                        text = placeholder,
                        textAlign = TextAlign.Center,
                        fontFamily = schoolBellFamily,
                        fontSize = fontSize,
                        fontWeight = if (multiline) FontWeight.Normal else FontWeight.Medium,
                        color = placeholderColor
                    )
                }
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        if (hasError) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                fontFamily = schoolBellFamily
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLogEntry() {
    LeafLogTheme {
        LogEntry(
            modifier = Modifier.width(280.dp),
            value = "",
            placeholder = "Title",
            multiline = false,
            error = "error"
        ) {

        }
    }
}