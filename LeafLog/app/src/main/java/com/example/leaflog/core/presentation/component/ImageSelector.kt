package com.example.leaflog.core.presentation.component

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.example.leaflog.R
import com.example.leaflog.ui.theme.LeafLogTheme
import com.example.leaflog.util.createImageFile
import com.example.leaflog.util.getUriForFile
import java.io.File

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    value: String? = null,
    isLoading: Boolean = false,
    onSave: (String) -> Unit,
) {
    val context = LocalContext.current
    var currentFile by remember { mutableStateOf<File?>(null) }
    var hasPermission by remember { mutableStateOf(context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            onSave(currentFile!!.toUri().toString())
        }
    }
    val getPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) {
        hasPermission = it
    }

    Box(modifier = modifier
        .clickable {
            if (!isLoading) {
                if (hasPermission) {
                    currentFile = context.createImageFile()
                    cameraLauncher.launch(context.getUriForFile(currentFile!!))
                } else {
                    getPermission.launch(Manifest.permission.CAMERA)
                }
            }
        }
    ) {
        if (value == null) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                painter = painterResource(R.drawable.image_placeholder),
                contentDescription = ""
            )
        } else {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                model = value,
                contentDescription = ""
            )
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewImageSelector() {
    LeafLogTheme {
        ImageSelector(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(10.dp))
                .size(200.dp)
        ) {}
    }
}