package io.github.znak99.androidfirebasechatex.viewmodel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.app.DEFAULT_THUMBNAIL_IMAGE
import io.github.znak99.androidfirebasechatex.app.FIRESTORE_STORAGE_URL
import io.github.znak99.androidfirebasechatex.app.REALTIME_DATABASE_URL
import io.github.znak99.androidfirebasechatex.model.firebase.User

class SignUpViewModel: ViewModel() {

    // Debug
    private val TAG = "SignUp"

    // Firebase
    private val _auth = Firebase.auth
    private val _database = FirebaseDatabase.getInstance(REALTIME_DATABASE_URL).reference

    // Observable data
    var email: String by mutableStateOf("")
    var username: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var passwordCheck: String by mutableStateOf("")
    var warningMessage: String by mutableStateOf("")
    var isSubmitButtonDisabled: Boolean by mutableStateOf(false)
    var isShowSignedUpAlert: Boolean by mutableStateOf(false)

    fun signUp(context: Activity) {
        Log.d(TAG, "Try to sign up > email: $email")
        Log.d(TAG, "Try to sign up > username: $username")
        Log.d(TAG, "Try to sign up > password: $password")
        Log.d(TAG, "Try to sign up > passwordCheck: $passwordCheck")

        // Initialize warning message
        warningMessage = ""

        // Disable the button to prevent duplicate submits
        isSubmitButtonDisabled = true

        // Check no blanked email field
        if (email.isBlank()) {
            warningMessage = "Email field can not be blanked"
            isSubmitButtonDisabled = false
            password = ""
            passwordCheck = ""
            return
        }

        // Check no blanked username field
        if (username.isBlank()) {
            warningMessage = "Username field can not be blanked"
            isSubmitButtonDisabled = false
            password = ""
            passwordCheck = ""
            return
        }

        // Check validation of username field
        if (username.length !in 4..16) {
            warningMessage = "Username must be more than 4 characters and less than 16 characters"
            isSubmitButtonDisabled = false
            password = ""
            passwordCheck = ""
            return
        }

        // Check no blanked password field
        if (password.isBlank()) {
            warningMessage = "Password field can not be blanked"
            isSubmitButtonDisabled = false
            password = ""
            passwordCheck = ""
            return
        }

        // Check no blanked password check field
        if (passwordCheck.isBlank()) {
            warningMessage = "Password check field can not be blanked"
            isSubmitButtonDisabled = false
            password = ""
            passwordCheck = ""
            return
        }

        // Check password field is equal to password check field
        if (passwordCheck != password) {
            warningMessage = "Password must be equal to password check"
            isSubmitButtonDisabled = false
            password = ""
            passwordCheck = ""
            return
        }

        // Request sign up to firebase
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { result ->
                if (result.isSuccessful) {
                    // Success
                    Log.d(TAG, "Successfully signed up with email and password.")

                    val signedUpUser = result.result.user

                    val usersRef = _database.child("users")
                    usersRef.child(signedUpUser?.uid.toString())
                        .push().setValue(
                            User(
                                uid = signedUpUser?.uid ?: "",
                                email = signedUpUser?.email ?: "",
                                username = username,
                                thumbnailPath = FIRESTORE_STORAGE_URL + DEFAULT_THUMBNAIL_IMAGE,
                                friendsId = listOf("")
                            )
                        )

                    isShowSignedUpAlert = true
                } else {
                    // Failure
                    Log.d(TAG, "Failed to signed in with email and password.")

                    warningMessage = result.exception?.message.toString()

                    password = ""
                    passwordCheck = ""

                    isSubmitButtonDisabled = false
                }
            }
    }

    fun completeSignUp(completion: () -> Unit) {
        // Initialize fields
        email = ""
        username = ""
        password = ""
        passwordCheck = ""

        isShowSignedUpAlert = false

        isSubmitButtonDisabled = false

        checkEmailVerification()

        _auth.signOut()

        completion()
    }

    private fun checkEmailVerification() {
        val user = _auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "${user.email} verified!")
                }
            }
    }
}