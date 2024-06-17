package io.github.znak99.androidfirebasechatex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordViewModel {

    // Debug
    private val TAG = "ResetPassword"

    // Observable data
    var email: String by mutableStateOf("")
    var warningMessage: String by mutableStateOf("")
    var isSubmitButtonDisabled: Boolean by mutableStateOf(false)
    var isShowEmailSentAlert: Boolean by mutableStateOf(false)

    fun sendResetPasswordEmail() {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")

                    isShowEmailSentAlert = true
                } else {
                    Log.d(TAG, "Error ${task.exception?.message}")
                }
            }
    }
}