package com.plcoding.contactscomposemultiplatform.core.presentation

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.plcoding.contactscomposemultiplatform.core.util.AndroidBitmapUtils

@Composable
actual fun rememberCameraManager(onResult: (SharedImage?) -> Unit ): CameraManager {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult=  { success->
            if (success) {
                onResult(SharedImage(AndroidBitmapUtils.getBitmapFromUri(tempPhotoUri, contentResolver)))
            }
        }
    )
    return remember {
        CameraManager(
            onLaunch = {
                tempPhotoUri = ComposeFileProvider.getImageUri(context)
                cameraLauncher.launch(tempPhotoUri)
            }
        )
    }
}

actual class CameraManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() { onLaunch() }
}