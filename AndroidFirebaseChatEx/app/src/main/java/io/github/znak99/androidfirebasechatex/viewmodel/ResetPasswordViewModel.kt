package io.github.znak99.androidfirebasechatex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ResetPasswordViewModel {

    // Debug
    private val TAG = "ResetPassword"

    // Observable data
    var email: String by mutableStateOf("")
    var warningMessage: String by mutableStateOf("")
    var isSubmitButtonDisabled: Boolean by mutableStateOf(false)
    var isShowEmailSendAlert: Boolean by mutableStateOf(false)

    fun sendResetPasswordEmail() {

    }
}