package com.plcoding.contactscomposemultiplatform.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image

@Composable
actual fun rememberBitmapFromBytes(photoBytes: ByteArray?): ImageBitmap? {
    return remember(photoBytes) {
        if (photoBytes != null) {
            Bitmap.makeFromImage(Image.makeFromEncoded(photoBytes)).asComposeImageBitmap()
        } else {
            null
        }
    }
}