package com.plcoding.contactscomposemultiplatform.contacts.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.plcoding.contactscomposemultiplatform.contacts.domain.Contact
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel : ViewModel() {
    fun onEvent(contactListEvent: ContactListEvent) {
    }

    private val _state = MutableStateFlow(ContactListState(contactList = contactList))
    val state = _state.asStateFlow()

    var newContact: Contact? by mutableStateOf(null)
        private set
}

val contactList = listOf(
    Contact(1, "Alice", "Wonderland", "alice@wonderland.com", "555-1234", null),
    Contact(2, "Bob", "Marley", "bob@marley.com", "555-5678", null),
    Contact(3, "Charlie", "Chaplin", "charlie@chaplin.com", "555-4321", null),
    Contact(4, "Diana", "Prince", "diana@amazon.com", "555-8765", null),
    Contact(5, "Elvis", "Presley", "elvis@rocknroll.com", "555-9876", null),
)
