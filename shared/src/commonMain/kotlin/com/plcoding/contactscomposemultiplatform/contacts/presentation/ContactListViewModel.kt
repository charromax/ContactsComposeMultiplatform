package com.plcoding.contactscomposemultiplatform.contacts.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.plcoding.contactscomposemultiplatform.contacts.domain.Contact
import com.plcoding.contactscomposemultiplatform.contacts.domain.ContactDataSource
import com.plcoding.contactscomposemultiplatform.contacts.domain.ContactValidator
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactListViewModel(
    private val contactDataSource: ContactDataSource
) : ViewModel() {
    fun onEvent(contactListEvent: ContactListEvent) {
        when (contactListEvent) {
            ContactListEvent.DeleteContact -> onDeleteContact()
            ContactListEvent.DismissContact -> onDismissContact()
            is ContactListEvent.EditContact -> onEditContact(contactListEvent.contact)
            ContactListEvent.OnAddNewContactClicked -> onAddNewContact()
            is ContactListEvent.OnEmailChanged -> {
                newContact = newContact?.copy(email = contactListEvent.value)
            }

            is ContactListEvent.OnFirstNameChanged -> {
                newContact = newContact?.copy(firstName = contactListEvent.value)
            }

            is ContactListEvent.OnLastNameChanged -> {
                newContact = newContact?.copy(lastName = contactListEvent.value)
            }

            is ContactListEvent.OnPhoneNumberChanged -> {
                newContact = newContact?.copy(phoneNumber = contactListEvent.value)
            }

            is ContactListEvent.OnPhotoPicked -> {
                newContact = newContact?.copy(photoBytes = contactListEvent.bytes)
            }

            ContactListEvent.SaveContact -> {
                onSaveContact()
            }

            is ContactListEvent.SelectContact -> {
                _state.update {
                    it.copy(
                        selectedContact = contactListEvent.contact,
                        isSelectedContactSheetOpen = true,
                        isAddContactSheetOpen = false
                    )
                }
            }

            else -> Unit
        }
    }

    private fun onSaveContact() {
        newContact?.let { contact: Contact ->
            val result = ContactValidator.validateContact(contact)
            val errors = listOfNotNull(
                result.firstNameError,
                result.lastNameError,
                result.emailError,
                result.phoneNumberError,
            )
            if (errors.isEmpty()) {
                _state.update {
                    it.copy(
                        isAddContactSheetOpen = false,
                        firstNameError = null,
                        lastNameError = null,
                        emailError = null,
                        phoneNumberError = null,
                    )
                }
                viewModelScope.launch {
                    contactDataSource.insertContact(contact)
                    delay(300) //Animation delay
                    newContact = null
                }
            } else {
                _state.update {
                    it.copy(
                        firstNameError = result.firstNameError,
                        lastNameError = result.lastNameError,
                        emailError = result.emailError,
                        phoneNumberError = result.firstNameError,
                    )
                }
            }
        }
    }

    private fun onAddNewContact() {
        _state.update {
            it.copy(
                isAddContactSheetOpen = true
            )
        }
        newContact = Contact(
            id = null,
            firstName = "",
            lastName = "",
            email = "",
            phoneNumber = "",
            photoBytes = null
        )
    }

    private fun onEditContact(contact: Contact) {
        _state.update {
            it.copy(
                selectedContact = null,
                isAddContactSheetOpen = true,
                isSelectedContactSheetOpen = false
            )
        }
        newContact = contact
    }

    private fun onDeleteContact() {
        viewModelScope.launch {
            _state.value.selectedContact?.let {
                _state.update { it.copy(isSelectedContactSheetOpen = false) }
                contactDataSource.deleteContact(it.id!!)
                delay(300) // animation delay
                _state.update { it.copy(selectedContact = null) }
            }
        }
    }

    private fun onDismissContact() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSelectedContactSheetOpen = false,
                    isAddContactSheetOpen = false,
                    firstNameError = null,
                    lastNameError = null,
                    emailError = null,
                    phoneNumberError = null
                )
            }
            delay(300)
            newContact = null
            _state.update { it.copy(selectedContact = null) }
        }
    }

    private val _state = MutableStateFlow(ContactListState())
    val state = combine(
        _state,
        contactDataSource.getContacts(),
        contactDataSource.getRecentContacts(10)
    ) { state, contacts, recentContacts ->
        state.copy(
            contactList = contacts,
            recentlyAddedContacts = recentContacts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ContactListState())


    var newContact: Contact? by mutableStateOf(null)
        private set


}
