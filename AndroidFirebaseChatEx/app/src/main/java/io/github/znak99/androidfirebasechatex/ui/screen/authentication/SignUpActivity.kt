package io.github.znak99.androidfirebasechatex.ui.screen.authentication

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.R
import io.github.znak99.androidfirebasechatex.app.REALTIME_DB_URL
import io.github.znak99.androidfirebasechatex.dto.UserDTO
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppRed
import io.github.znak99.androidfirebasechatex.ui.theme.Purple40

class SignUpActivity : ComponentActivity() {

    private val TAG = "SignIn"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        setContent {
            AndroidFirebaseChatExTheme {
                SignUpScreen (
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

@Composable
private fun SignUpScreen(modifier: Modifier = Modifier, auth: FirebaseAuth, TAG: String) {

    val context = LocalContext.current

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

    var isShowAlert by rememberSaveable { mutableStateOf(false) }

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
                    text = "Sign Up",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = Purple40,
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .padding(end = 8.dp)
                )
                Image(
                    painter = signUpIcon,
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
                    painter = usernameIcon,
                    contentDescription = "Username Icon",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                )

                TextField(
                    value = username,
                    onValueChange = { value ->
                        if (!value.contains(" ")) {
                            username = value
                        }
                    },
                    label = { Text("Username") },
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Image(
                    painter = passwordCheckIcon,
                    contentDescription = "Password Check Icon",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                )

                TextField(
                    value = passwordCheck,
                    onValueChange = { value ->
                        if (!value.contains(" ")) {
                            passwordCheck = value
                        }
                    },
                    label = { Text("Password Check") },
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
                    if (email.isBlank() || username.isBlank() ||
                        password.isBlank() || passwordCheck.isBlank()) {
                        warningMessage = "※ One or more blanked fields exist"
                        password = ""
                        passwordCheck = ""
                    } else if (password != passwordCheck) {
                        warningMessage = "※ Mismatch password and password check"
                        password = ""
                        passwordCheck = ""
                    } else {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(context as SignUpActivity) { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "createUserWithEmail:success")

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

                                    isShowAlert = true
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    warningMessage = "※ ${task.exception?.message}"
                                    password = ""
                                    passwordCheck = ""
                                }
                            }
                    }
                }
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 20.sp
                )
            }

            if (isShowAlert) {
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                email = ""
                                username = ""
                                password = ""
                                passwordCheck = ""

                                Firebase.auth.signOut()

                                context as SignUpActivity
                                context.finish()

                                isShowAlert = false
                            }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    title = { Text(text = "Signed up successfully!") },
                    text = { Text(text = "Sign in and enjoy chat!") }
                )
            }
        }
    }
}