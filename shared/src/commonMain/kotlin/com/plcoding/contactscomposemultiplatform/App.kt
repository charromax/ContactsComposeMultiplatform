package com.plcoding.contactscomposemultiplatform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.plcoding.contactscomposemultiplatform.contacts.presentation.ContactListScreen
import com.plcoding.contactscomposemultiplatform.contacts.presentation.ContactsViewModel
import com.plcoding.contactscomposemultiplatform.core.presentation.ContactsTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    val viewModel = getViewModel(
        key = "contactListScreen",
        factory = viewModelFactory {
            ContactsViewModel()
        },
    )

    val state by viewModel.state.collectAsState()

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
                newContact = null,
                onEvent = viewModel::onEvent,
            )
        }
    }
}
