package com.plcoding.contactscomposemultiplatform.core.util

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.InputStream

@Composable
actual fun rememberBitmapFromBytes(photoBytes: ByteArray?): ImageBitmap? {
    return remember(photoBytes) {
        if (photoBytes != null) {
            BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size).asImageBitmap()
        } else {
            null
        }
    }
}

object AndroidBitmapUtils {
    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): android.graphics.Bitmap? {
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(uri)
            val s = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            return s
        } catch (e: Exception) {
            e.printStackTrace()
            println("getBitmapFromUri Exception: ${e.message}")
            println("getBitmapFromUri Exception: ${e.localizedMessage}")
            return null
        }
    }
}


