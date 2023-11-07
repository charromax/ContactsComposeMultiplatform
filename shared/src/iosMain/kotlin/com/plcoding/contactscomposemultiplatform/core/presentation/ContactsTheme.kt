package com.plcoding.contactscomposemultiplatform.core.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.plcoding.contactscomposemultiplatform.ui.theme.DarkColors
import com.plcoding.contactscomposemultiplatform.ui.theme.LightColors
import com.plcoding.contactscomposemultiplatform.ui.theme.Typography

@Composable
actual fun ContactsTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}