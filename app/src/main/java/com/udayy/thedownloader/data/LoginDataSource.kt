package com.udayy.thedownloader.data

/**
 * Class that handles authentication w/ login credentials.
 */
class LoginDataSource {

    fun login(phoneNumber: String, otp: String): Result<Int> {
        // handle otp authentication
        return if (otp == "1234") {
            Result.Success(1)
        } else {
            Result.Error(0)
        }
    }
}