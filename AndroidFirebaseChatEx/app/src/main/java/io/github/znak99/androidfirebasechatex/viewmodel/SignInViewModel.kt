package io.github.znak99.androidfirebasechatex.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInViewModel : ViewModel() {

    // Debug
    private val TAG = "SignIn"

    // Shared preference
    private val SHARED_PREFS_NAME = "Authentication"
    private val SAVE_EMAIL_KEY = "Email"

    // Firebase
    private val _auth = Firebase.auth

    // Observable data
    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var warningMessage: String by mutableStateOf("Email field can not be blanked")
    var isSaveEmailChecked: Boolean by mutableStateOf(true)
    var isSubmitButtonDisabled: Boolean by mutableStateOf(false)

    fun signIn(context: Activity, signedInCompletion: () -> Unit) {

        Log.d(TAG, "Try to sign in > email: $email")
        Log.d(TAG, "Try to sign in > password: $password")

        // Initialize warning message
        warningMessage = ""

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

                    saveEmail(context)

                    signedInCompletion()

                    isSubmitButtonDisabled = false
                } else {
                    // Failure
                    Log.e(TAG, "Failed to sign in with email and password.")
                    Log.e(TAG, "Error message: ${result.exception?.message}")

                    warningMessage = result.exception?.message.toString()

                    isSubmitButtonDisabled = false
                }
            }
    }

    private fun saveEmail(context: Activity) {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit {
            putString(SAVE_EMAIL_KEY, email)
            apply()
        }
    }

    fun checkSharedPreference(context: Activity) {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        email = sharedPrefs.getString(SAVE_EMAIL_KEY, "") ?: ""
    }
}