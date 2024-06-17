package io.github.znak99.androidfirebasechatex.ui.screen.authentication

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthField
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthHeader
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthSubmitButton
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthWarningMessage
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppPrimary
import io.github.znak99.androidfirebasechatex.ui.theme.AppWarning
import io.github.znak99.androidfirebasechatex.viewmodel.ResetPasswordViewModel
import io.github.znak99.androidfirebasechatex.viewmodel.SignInViewModel

class ResetPasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // NOTE: ResetPasswordActivity UI
        setContent {
            AndroidFirebaseChatExTheme {
                ResetPasswordScreen()
            }
        }
    }
}

@Composable
private fun ResetPasswordScreen(viewModel: ResetPasswordViewModel = ResetPasswordViewModel()) {

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
            AuthHeader(title = "Authenticate email")

            Text(
                text = "Enter your email to reset your password",
                color = AppWarning,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 24.dp)
            )
            // Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
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
            }

            // Warning message
            AuthWarningMessage(message = viewModel.warningMessage)

            // Submit button
            AuthSubmitButton(text = "Send email", viewModel.isSubmitButtonDisabled) {
                viewModel.sendResetPasswordEmail()
            }
        }
    }
}