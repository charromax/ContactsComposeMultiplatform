package com.plcoding.contactscomposemultiplatform.core.util

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

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
