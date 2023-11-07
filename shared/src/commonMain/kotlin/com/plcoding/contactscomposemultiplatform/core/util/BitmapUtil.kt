package com.plcoding.contactscomposemultiplatform.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun rememberBitmapFromBytes(photoBytes: ByteArray?): ImageBitmap?
