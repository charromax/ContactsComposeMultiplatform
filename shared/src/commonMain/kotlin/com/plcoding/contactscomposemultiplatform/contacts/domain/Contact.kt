package com.plcoding.contactscomposemultiplatform.contacts.domain

data class Contact(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val photoBytes: ByteArray?,
) {
    val fullName = "$firstName $lastName"
}
