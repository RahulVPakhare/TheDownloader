package com.udayy.thedownloader.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udayy.thedownloader.R
import com.udayy.thedownloader.data.LoginRepository
import com.udayy.thedownloader.data.Result

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(phoneNumber: String, otp: Int) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(phoneNumber, otp)

        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = R.string.login_successful)
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(phoneNumber: String, otp: String) {
        if (!isPhoneNumberValid(phoneNumber)) {
            _loginForm.value = LoginFormState(phoneNumberError = R.string.invalid_phone_number)
        } else if (!isOTPValid(otp)) {
            _loginForm.value = LoginFormState(OTPError = R.string.invalid_otp)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder phone number validation check
    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return Patterns.PHONE.matcher(phoneNumber).matches()
    }

    // A placeholder otp validation check
    private fun isOTPValid(otp: String): Boolean {
        return otp.length == 4
    }
}