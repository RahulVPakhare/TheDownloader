package com.udayy.thedownloader.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val phoneNumberError: Int? = null,
    val OTPError: Int? = null,
    val isDataValid: Boolean = false
)