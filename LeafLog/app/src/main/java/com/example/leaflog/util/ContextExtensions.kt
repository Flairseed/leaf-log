package com.example.leaflog.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

fun Context.createImageFile(): File {
    val timeStamp = System.currentTimeMillis().toString()
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    cacheDir
    return File.createTempFile("Image-$timeStamp", ".jpg", storageDir)
}

fun Context.getUriForFile(file: File): Uri {
    return FileProvider.getUriForFile(this, "${this.packageName}.provider", file)
}