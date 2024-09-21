package com.example.allumnisystem.utils

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory

fun uriToBitmap(context: Context, uri: Uri): ImageBitmap? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap?.asImageBitmap()
}
