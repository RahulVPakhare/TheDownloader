package com.udayy.thedownloader.ui.login

/**
 * Authentication result : success or error message.
 */
data class LoginResult(
    val success: Int? = null,
    val error: Int? = null
)