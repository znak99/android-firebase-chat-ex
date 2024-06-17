package io.github.znak99.androidfirebasechatex.ui.screen.authentication

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthField
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthHeader
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthSubmitButton
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthWarningMessage
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppPrimary
import io.github.znak99.androidfirebasechatex.viewmodel.SignUpViewModel

class SignUpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // NOTE: SignUpActivity UI
        setContent {
            AndroidFirebaseChatExTheme {
                SignUpScreen ()
            }
        }
    }
}

@Composable
private fun SignUpScreen(viewModel: SignUpViewModel = SignUpViewModel()) {

    // View context
    val context = (LocalContext.current as Activity)

    // View
    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppPrimary)
                .padding(innerPadding)
        ) {
            // Header
            AuthHeader(title = "Sign Up")

            // Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
                    .padding(horizontal = 12.dp)
            ) {
                // Email field
                AuthField(
                    title = "Email",
                    icon = Icons.Filled.Email,
                    isSecure = false,
                    value =  viewModel.email,
                    onValueChange = { text ->
                        if (!text.contains(" ")) {
                            viewModel.email = text
                        }
                    },
                    onTrailingIconClicked = {
                        viewModel.email = ""
                    }
                )
                // Username field
                AuthField(
                    title = "Username",
                    icon = Icons.Filled.Person,
                    isSecure = false,
                    value =  viewModel.username,
                    onValueChange = { text ->
                        if (!text.contains(" ")) {
                            viewModel.username = text
                        }
                    },
                    onTrailingIconClicked = {
                        viewModel.username = ""
                    }
                )
                // Password field
                AuthField(
                    title = "Password",
                    icon = Icons.Filled.Lock,
                    isSecure = true,
                    value = viewModel.password,
                    onValueChange = { text ->
                        if (!text.contains(" ")) {
                            viewModel.password = text
                        }
                    },
                    onTrailingIconClicked = {
                        viewModel.password = ""
                    }
                )
                // Password check field
                AuthField(
                    title = "Password check",
                    icon = Icons.Filled.CheckCircle,
                    isSecure = true,
                    value = viewModel.passwordCheck,
                    onValueChange = { text ->
                        if (!text.contains(" ")) {
                            viewModel.passwordCheck = text
                        }
                    },
                    onTrailingIconClicked = {
                        viewModel.passwordCheck = ""
                    }
                )
            }

            // Warning message
            AuthWarningMessage(message = viewModel.warningMessage)

            // Submit button
            AuthSubmitButton(text = "Sign Up", viewModel.isSubmitButtonDisabled) {
                viewModel.signUp(context)
            }
        }

        if (viewModel.isShowSignedUpAlert) {
            AlertDialog(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = "Person")
                        Text(text = "Authentication system")
                    }
                },
                text = {
                    Text(
                        text = "Signed up successfully!\nYou can sign in after verify your email.",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onDismissRequest = {},
                confirmButton = {
                    Button(onClick = { viewModel.completeSignUp { context.finish() } }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
