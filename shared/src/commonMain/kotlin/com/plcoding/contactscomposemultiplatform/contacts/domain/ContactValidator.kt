package com.plcoding.contactscomposemultiplatform.contacts.domain

object ContactValidator {
    fun validateContact(contact: Contact): ValidationResult {
        var result = ValidationResult()
        if (contact.firstName.isBlank()) {
            result = result.copy(firstNameError = "First name cannot be empty!")
        }
        if (contact.lastName.isBlank()) {
            result = result.copy(lastNameError = "Last name cannot be empty!")
        }
        if (!isEmailValid(contact.email) || contact.email.isBlank()) {
            result = result.copy(emailError = "This is not a valid email")
        }
        if (contact.phoneNumber.isBlank()) {
            result = result.copy(phoneNumberError = "Phone number cannot be empty!")
        }
        return result
    }

    fun isEmailValid(email: String): Boolean {
        val pattern = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").toRegex()
        return pattern.matches(email)
    }

    data class ValidationResult(
        val firstNameError: String? = null,
        val lastNameError: String? = null,
        val emailError: String? = null,
        val phoneNumberError: String? = null,
    )
}