package com.plcoding.contactscomposemultiplatform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.plcoding.contactscomposemultiplatform.contacts.presentation.ContactListEvent
import com.plcoding.contactscomposemultiplatform.contacts.presentation.ContactListScreen
import com.plcoding.contactscomposemultiplatform.contacts.presentation.ContactListViewModel
import com.plcoding.contactscomposemultiplatform.core.presentation.ContactsTheme
import com.plcoding.contactscomposemultiplatform.core.presentation.PermissionCallback
import com.plcoding.contactscomposemultiplatform.core.presentation.PermissionStatus
import com.plcoding.contactscomposemultiplatform.core.presentation.PermissionType
import com.plcoding.contactscomposemultiplatform.core.presentation.SharedImage
import com.plcoding.contactscomposemultiplatform.core.presentation.createPermissionsManager
import com.plcoding.contactscomposemultiplatform.core.presentation.rememberCameraManager
import com.plcoding.contactscomposemultiplatform.core.presentation.rememberGalleryManager
import com.plcoding.contactscomposemultiplatform.core.util.AlertMessageDialog
import com.plcoding.contactscomposemultiplatform.core.util.ImageSourceOptionDialog
import com.plcoding.contactscomposemultiplatform.di.AppModule
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    appModule: AppModule,
) {
    val viewModel = getViewModel(
        key = "contactListScreen",
        factory = viewModelFactory {
            ContactListViewModel(appModule.contactDataSource)
        },
    )

    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var image by remember { mutableStateOf<SharedImage?>(null) }
    var imageSourceOptionDialog by remember { mutableStateOf(value = false) }
    var launchCamera by remember { mutableStateOf(value = false) }
    var launchGallery by remember { mutableStateOf(value = false) }
    var launchSetting by remember { mutableStateOf(value = false) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }

    LaunchedEffect(image) {
        withContext(Dispatchers.Default) {
            image?.toByteArray()?.let {
                viewModel.onEvent(ContactListEvent.OnPhotoPicked(it))
            }
        }
    }

    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> launchCamera = true
                        PermissionType.GALLERY -> launchGallery = true
                    }
                }

                else -> {
                    permissionRationalDialog = true
                }
            }
        }


    })

    val cameraManager = rememberCameraManager { image = it }

    val galleryManager = rememberGalleryManager { image = it }
    if (imageSourceOptionDialog) {
        ImageSourceOptionDialog(onDismissRequest = {
            imageSourceOptionDialog = false
        }, onGalleryRequest = {
            imageSourceOptionDialog = false
            launchGallery = true
        }, onCameraRequest = {
            imageSourceOptionDialog = false
            launchCamera = true
        })
    }
    if (launchGallery) {
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }
    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }
    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }
    if (permissionRationalDialog) {
        AlertMessageDialog(title = "Permission Required",
            message = "To set your profile picture, please grant this permission. You can manage permissions in your device settings.",
            positiveButtonText = "Settings",
            negativeButtonText = "Cancel",
            onPositiveClick = {
                permissionRationalDialog = false
                launchSetting = true
            },
            onNegativeClick = {
                permissionRationalDialog = false
            })

    }
    ContactsTheme(
        darkTheme,
        dynamicColor,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            ContactListScreen(
                state = state,
                newContact = viewModel.newContact,
                onEvent = viewModel::onEvent,
                onPickImageClicked = { imageSourceOptionDialog = true }
            )
        }
    }
}
