package io.github.znak99.androidfirebasechatex.ui.screen.authentication

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.R
import io.github.znak99.androidfirebasechatex.app.REALTIME_DB_URL
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationField
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationHeader
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationSubmit
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationWarningMessage
import io.github.znak99.androidfirebasechatex.dto.UserDTO
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme

class SignUpActivity : ComponentActivity() {

    private val TAG = "SignIn"

    // Firebase authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize
        auth = Firebase.auth

        // NOTE: SignUpActivity UI
        setContent {
            AndroidFirebaseChatExTheme {
                SignUpScreen (
                    auth = auth,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.systemBars.asPaddingValues())
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun SignUpScreen(auth: FirebaseAuth, modifier: Modifier = Modifier) {

    val TAG = "SignUp"

    val context = (LocalContext.current as Activity)
    val clipboardManager = LocalClipboardManager.current

    val signUpIcon: Painter = painterResource(id = R.drawable.baseline_person_add_alt_1_24)
    val emailIcon: Painter = painterResource(id = R.drawable.baseline_email_24)
    val usernameIcon: Painter = painterResource(id = R.drawable.baseline_assignment_ind_24)
    val passwordIcon: Painter = painterResource(id = R.drawable.baseline_lock_24)
    val passwordCheckIcon: Painter = painterResource(id = R.drawable.baseline_check_box_24)

    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordCheck by rememberSaveable { mutableStateOf("") }
    var warningMessage by rememberSaveable { mutableStateOf("") }
    var signedUpEmail by rememberSaveable { mutableStateOf("") }

    var isShowAlert by rememberSaveable { mutableStateOf(false) }
    var isButtonDisabled by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier
    ) {
        Column {
            // Header
            AuthenticationHeader(
                title = "Sign Up",
                icon = signUpIcon
            )

            // Fields
            AuthenticationField(
                text = email,
                label = "Email",
                icon = emailIcon,
                isSecureField = false
            ) { value ->
                if (!value.contains(" ")) {
                    email = value
                }
            }

            AuthenticationField(
                text = username,
                label = "Username",
                icon = usernameIcon,
                isSecureField = false
            ) { value ->
                if (!value.contains(" ")) {
                    username = value
                }
            }

            AuthenticationField(
                text = password,
                label = "Password",
                icon = passwordIcon,
                isSecureField = true
            ) { value ->
                if (!value.contains(" ")) {
                    password = value
                }
            }

            AuthenticationField(
                text = passwordCheck,
                label = "Password check",
                icon = passwordCheckIcon,
                isSecureField = true
            ) { value ->
                if (!value.contains(" ")) {
                    passwordCheck = value
                }
            }

            // Warning message
            AuthenticationWarningMessage(
                message = warningMessage
            )

            // Submit button
            AuthenticationSubmit(
                text = "Sign Up"
            ) {
                if (!isButtonDisabled) {
                    signUp(
                        email = email,
                        username = username,
                        password = password,
                        passwordCheck = passwordCheck,
                        auth = auth,
                        context = context,
                        tag = TAG,
                        setPassword = { text -> password = text},
                        setPasswordCheck = { text -> passwordCheck = text},
                        setWarningMessage = { text -> warningMessage = text},
                        setSignedUpEmail = { text -> signedUpEmail = text },
                        setIsShowAlert = { flag -> isShowAlert = flag },
                        setIsButtonDisabled = { flag -> isButtonDisabled = flag }
                    )
                }
            }

            if (isShowAlert) {
                AlertDialog(
                    title = { Text(text = "Authentication system") },
                    text = {
                        Column {
                            Text("Account created successfully!\nYou can sign in with")
                            TextButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(signedUpEmail))
                                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(signedUpEmail)
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                email = ""
                                username = ""
                                password = ""
                                passwordCheck = ""

                                isShowAlert = false

                                // Dismiss to sign in activity
                                context.finish()

                                isButtonDisabled = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    onDismissRequest = {}
                )
            }
        }
    }
}

private fun signUp(
    email: String,
    username: String,
    password: String,
    passwordCheck: String,
    auth: FirebaseAuth,
    context: Activity,
    tag: String,
    setPassword: (String) -> Unit,
    setPasswordCheck: (String) -> Unit,
    setWarningMessage: (String) -> Unit,
    setSignedUpEmail: (String) -> Unit,
    setIsShowAlert: (Boolean) -> Unit,
    setIsButtonDisabled: (Boolean) -> Unit
) {
    setWarningMessage("")
    setIsButtonDisabled(true)

    if (email.isBlank() || username.isBlank() ||
        password.isBlank() || passwordCheck.isBlank()) { // Check blanked fields
        setWarningMessage("※ One or more blanked fields exist")
        setPassword("")
        setPasswordCheck("")
        setIsButtonDisabled(false)
    } else if (password != passwordCheck) { // Check mismatch password field and password check field
        setWarningMessage("※ Mismatch password and password check")
        setPassword("")
        setPasswordCheck("")
        setIsButtonDisabled(false)
    } else {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as SignUpActivity) { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "createUserWithEmail:success")

                    val database = Firebase.database(REALTIME_DB_URL)
                    val users = database.getReference("users")

                    users.push().setValue(
                        UserDTO(
                            uid = task.result.user?.uid,
                            email = task.result.user?.email,
                            username = username,
                            thumbnailPath = "",
                            friendsId = listOf("")
                        )
                    )

                    task.result.user?.email?.let { setSignedUpEmail(it) }

                    setIsShowAlert(true)
                } else {
                    Log.w(tag, "createUserWithEmail:failure", task.exception)
                    setWarningMessage("※ ${task.exception?.message}")
                    setPassword("")
                    setPasswordCheck("")
                    setIsButtonDisabled(false)
                }
            }
    }
}