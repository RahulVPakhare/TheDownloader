package com.udayy.thedownloader.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success(val data: Int) : Result<Nothing>()
    data class Error(val data: Int) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Error -> "Error"
        }
    }
}