package io.github.znak99.androidfirebasechatex.viewmodel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInViewModel : ViewModel() {

    // Debug
    private val TAG = "Authentication"

    // Firebase
    private val _auth = Firebase.auth

    // Observable data
    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var warningMessage: String by mutableStateOf("")
    var isSubmitButtonDisabled: Boolean by mutableStateOf(false)

    fun signIn(context: Activity, signedInCompletion: () -> Unit) {
        // Disable the button to prevent duplicate submits
        isSubmitButtonDisabled = true

        // Check no blanked email field
        if (email.isBlank()) {
            warningMessage = "Email field can not be blanked"
            isSubmitButtonDisabled = false
            return
        }

        // Check no blanked password field
        if (password.isBlank()) {
            warningMessage = "Password field can not be blanked"
            isSubmitButtonDisabled = false
            return
        }

        // Request sign in to firebase
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { result ->
                if (result.isSuccessful) {
                    // Success
                    Log.d(TAG, "Successfully signed in with email and password.")

                    signedInCompletion()

                    isSubmitButtonDisabled = false
                } else {
                    // Failure
                    Log.e(TAG, "Failed to sign in with email and password.")
                    Log.e(TAG, "Error message: ${result.exception?.message}")

                    isSubmitButtonDisabled = false
                }
            }
    }
}