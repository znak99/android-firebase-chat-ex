package io.github.znak99.androidfirebasechatex

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppBlack
import io.github.znak99.androidfirebasechatex.ui.theme.AppBlue
import io.github.znak99.androidfirebasechatex.ui.theme.AppRed
import io.github.znak99.androidfirebasechatex.ui.theme.Purple40

class SignInActivity : ComponentActivity() {

    private val TAG = "SignIn"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        setContent {
            AndroidFirebaseChatExTheme {
                if (Firebase.auth.currentUser != null) {
                    val intent = Intent(this, ChatListActivity::class.java)
                    startActivity(intent)
                } else {
                    SignInScreen (
                        modifier = Modifier
                            .padding(WindowInsets.systemBars.asPaddingValues())
                            .padding(horizontal = 12.dp),
                        auth = auth,
                        TAG = TAG
                    )
                }
            }
        }
    }
}

@Composable
private fun SignInScreen(modifier: Modifier = Modifier, auth: FirebaseAuth, TAG: String) {

    val context = LocalContext.current

    val signInIcon: Painter = painterResource(id = R.drawable.baseline_person_pin_24)
    val emailIcon: Painter = painterResource(id = R.drawable.baseline_email_24)
    val passwordIcon: Painter = painterResource(id = R.drawable.baseline_lock_24)

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var warningMessage by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = modifier
    ) {
        Column {
            Text(
                text = "Firebase Chat",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = Purple40,
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .padding(end = 8.dp)
                )
                Image(
                    painter = signInIcon,
                    contentDescription = "Sign Up Icon",
                    modifier = Modifier
                        .padding(4.dp)
                        .size(48.dp)
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Image(
                    painter = emailIcon,
                    contentDescription = "Email Icon",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                )

                TextField(
                    value = email,
                    onValueChange = { value ->
                        if (!value.contains(" ")) {
                            email = value
                        }
                    },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Image(
                    painter = passwordIcon,
                    contentDescription = "Password Icon",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                )

                TextField(
                    value = password,
                    onValueChange = { value ->
                        if (!value.contains(" ")) {
                            password = value
                        }
                    },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = warningMessage,
                color = AppRed,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                onClick = {
                    warningMessage = ""
                    if (email.isBlank() || password.isBlank()) {
                        warningMessage = "※ One or more blanked fields exist"
                    } else {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(context as SignInActivity) { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "signInWithEmail:success")

                                    email = ""
                                    password = ""

                                    val intent = Intent(context, ChatListActivity::class.java)
                                    context.startActivity(intent)

                                } else {
                                    Log.d(TAG, "signInWithEmail:failure")
                                    warningMessage = "※ ${task.exception?.message}"
                                }
                            }
                    }
                }
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 20.sp
                )
            }

            TextButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 32.dp),
                onClick = {
                    val intent = Intent(context, SignUpActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "New to Firebase Chat?",
                        color = AppBlack,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Sign up now!",
                        color = AppBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }

            TextButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp),
                onClick = {
                    val intent = Intent(context, ResetPasswordActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Forgot password?",
                        color = AppBlack,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Reset password!",
                        color = AppBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }
        }
    }
}