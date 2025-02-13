package com.example.leaflog.feature_log.presentation.component

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.leaflog.core.presentation.component.ImageSelector
import com.example.leaflog.ui.theme.LeafLogTheme
import com.example.leaflog.ui.theme.schoolBellFamily
import java.util.Date

@Composable
fun LogImageSelector(
    modifier: Modifier = Modifier,
    value: String? = null,
    created: Date,
    error: String? = null,
    isLoading: Boolean = false,
    onSave: (String) -> Unit,
) {
    val hasError = error != null
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = if (hasError) MaterialTheme.colorScheme.error else Color.Black
                )
                .padding(
                    top = 13.dp,
                    start = 13.dp,
                    end = 13.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageSelector(
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(width = 1.dp, color = Color.Black),
                value = value,
                isLoading = isLoading,
                onSave = onSave
            )
            Text(
                text = DateFormat.format("dd/MM/yyyy", created).toString(),
                fontSize = 20.sp,
                fontFamily = schoolBellFamily
            )
        }
        if (hasError) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = error!!,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
                fontFamily = schoolBellFamily
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLogImageSelector() {
    LeafLogTheme {
        LogImageSelector(
            modifier = Modifier.width(136.dp),
            created = Date(),
        ) {

        }
    }
}