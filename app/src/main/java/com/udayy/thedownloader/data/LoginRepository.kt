package com.udayy.thedownloader.data

/**
 * Class that requests authentication from the remote data source.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    fun login(phoneNumber: String, otp: String): Result<Int> {
        // handle login
        return dataSource.login(phoneNumber, otp)
    }
}