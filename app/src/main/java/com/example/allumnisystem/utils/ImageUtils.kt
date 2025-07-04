package com.example.allumnisystem.utils

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory

fun uriToBitmap(context: Context, uri: Uri): ImageBitmap? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap?.asImageBitmap()
        }
    } catch (e: Exception) {
        // Handle any IOException or SecurityException that might occur
        e.printStackTrace()
        null
    }
}
