package com.plcoding.contactscomposemultiplatform.core.presentation

import androidx.compose.ui.graphics.ImageBitmap
import platform.UIKit.UIImage

actual class SharedImage(image: UIImage) {
    actual fun toByteArray(): ByteArray? {
        TODO("Not yet implemented")
    }

    actual fun toImageBitmap(): ImageBitmap? {
        TODO("Not yet implemented")
    }
}