package com.udayy.thedownloader.data

/**
 * Class that handles authentication w/ login credentials.
 */
class LoginDataSource {

    fun login(phoneNumber: String, otp: Int): Result<Int> {
        // handle loggedInUser authentication
        return if (otp == 1234) {
            Result.Success(1)
        } else {
            Result.Error(0)
        }
    }
}