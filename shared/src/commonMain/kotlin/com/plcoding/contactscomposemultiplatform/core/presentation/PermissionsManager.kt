package com.plcoding.contactscomposemultiplatform.core.presentation

import androidx.compose.runtime.Composable

expect class PermissionsManager(callback: PermissionCallback) : PermissionHandler
@Composable
expect fun createPermissionsManager(callback: PermissionCallback): PermissionsManager
interface PermissionCallback {
    fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus)
}

enum class PermissionType {
    CAMERA,
    GALLERY
}
enum class PermissionStatus {
    GRANTED,
    DENIED,
    SHOW_RATIONAL
}