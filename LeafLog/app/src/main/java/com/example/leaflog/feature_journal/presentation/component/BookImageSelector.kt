package com.example.leaflog.feature_journal.presentation.component

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.leaflog.R
import com.example.leaflog.core.presentation.component.ImageSelector
import com.example.leaflog.ui.theme.LeafLogTheme
import com.example.leaflog.util.createImageFile
import com.example.leaflog.util.getUriForFile
import java.io.File

@Composable
fun BookImageSelector(
    modifier: Modifier = Modifier,
    value: String? = null,
    error: String? = null,
    isLoading: Boolean = false,
    onSave: (String) -> Unit,
) {
    val background = Color(0xFF881A1A)

    val hasError = error != null
    Column(modifier = modifier) {
        Box(modifier = Modifier
            .background(background, RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .padding(20.dp)
        ) {
            ImageSelector(
                modifier = Modifier
                .background(Color.White, RoundedCornerShape(10.dp))
                .fillMaxSize(),
                value = value,
                isLoading = isLoading,
                onSave = onSave
            )
        }
        if (hasError) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = error!!,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBookImageSelector() {
    LeafLogTheme {
        BookImageSelector(Modifier.width(250.dp)) {
            
        }
    }
}