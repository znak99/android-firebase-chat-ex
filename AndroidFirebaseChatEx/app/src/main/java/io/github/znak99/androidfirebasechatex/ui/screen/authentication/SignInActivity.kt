package io.github.znak99.androidfirebasechatex.ui.screen.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.R
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationLink
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationField
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationHeader
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationSubmit
import io.github.znak99.androidfirebasechatex.component.auth.AuthenticationWarningMessage
import io.github.znak99.androidfirebasechatex.ui.screen.chat.FriendsListActivity
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme

class SignInActivity : ComponentActivity() {

    // Firebase authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize
        auth = Firebase.auth

        // NOTE: SignInActivity UI
        setContent {
            AndroidFirebaseChatExTheme {
                SignInScreen (
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
private fun SignInScreen(auth: FirebaseAuth, modifier: Modifier = Modifier) {

    val TAG = "SignIn"

    val context = (LocalContext.current as Activity)

    val signInIcon: Painter = painterResource(id = R.drawable.baseline_person_pin_24)
    val emailIcon: Painter = painterResource(id = R.drawable.baseline_email_24)
    val passwordIcon: Painter = painterResource(id = R.drawable.baseline_lock_24)

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var warningMessage by rememberSaveable { mutableStateOf("") }
    var isButtonDisabled by rememberSaveable { mutableStateOf(false) }

    // System dismiss action overriding
    var backHandlingEnabled by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    BackHandler(backHandlingEnabled) {
        if (System.currentTimeMillis() - backPressedTime <= 400L) { // EXIT
            context.finish()
        } else { // SHOW TOAST
            Toast.makeText(context, "Press again fast to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    Surface(
        modifier = modifier
    ) {
        Column {
            // Header
            AuthenticationHeader(
                title = "Sign In",
                icon = signInIcon
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
                text = password,
                label = "Password",
                icon = passwordIcon,
                isSecureField = true
            ) { value ->
                if (!value.contains(" ")) {
                    password = value
                }
            }

            // Warning message
            AuthenticationWarningMessage(
                message = warningMessage
            )

            // Submit button
            AuthenticationSubmit(
                text = "Sign In"
            ) {
                if (!isButtonDisabled) {
                    signIn(
                        email = email,
                        password = password,
                        auth = auth,
                        context = context,
                        tag = TAG,
                        setEmail = { text -> email = text},
                        setPassword = { text -> password = text},
                        setWarningMessage = { text -> warningMessage = text},
                        setIsButtonDisabled = { flag -> isButtonDisabled = flag }
                    )
                } else {
                    Toast.makeText(context, "Already requested to server. Wait a minute.", Toast.LENGTH_SHORT).show()
                }
            }

            // Actions
            Column(
                modifier = Modifier
                    .padding(top = 72.dp)
            ) {
                AuthenticationLink(
                    description = "New to Firebase Chat?",
                    linkText = "Sign up now!"
                ) {
                    val intent = Intent(context, SignUpActivity::class.java)
                    context.startActivity(intent)
                }

                AuthenticationLink(
                    description = "Forgot password?",
                    linkText = "Reset password!"
                ) {
                    val intent = Intent(context, ResetPasswordActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }
}

private fun signIn(
    email: String,
    password: String,
    auth: FirebaseAuth,
    context: Activity,
    tag: String,
    setEmail: (String) -> Unit,
    setPassword: (String) -> Unit,
    setWarningMessage: (String) -> Unit,
    setIsButtonDisabled: (Boolean) -> Unit
) {
    setWarningMessage("")
    setIsButtonDisabled(true)

    if (email.isBlank() || password.isBlank()) { // Check blanked fields
        setWarningMessage("※ One or more blanked fields exist")
        setIsButtonDisabled(false)
    } else { // Submit
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "signInWithEmail:success")

                    setEmail("")
                    setPassword("")

                    val intent = Intent(context, FriendsListActivity::class.java)
                    context.startActivity(intent)

                    context.finish()

                    setIsButtonDisabled(false)
                } else {
                    Log.d(tag, "signInWithEmail:failure")
                    setWarningMessage("※ ${task.exception?.message}")
                    setIsButtonDisabled(false)
                }
            }
    }
}