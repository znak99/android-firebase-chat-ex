package io.github.znak99.androidfirebasechatex.ui.screen.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthField
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthHeader
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthNavigationButton
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthSubmitButton
import io.github.znak99.androidfirebasechatex.ui.component.auth.AuthWarningMessage
import io.github.znak99.androidfirebasechatex.ui.screen.chat.FriendsListActivity
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppBlack
import io.github.znak99.androidfirebasechatex.ui.theme.AppPrimary
import io.github.znak99.androidfirebasechatex.ui.theme.AppWhite
import io.github.znak99.androidfirebasechatex.viewmodel.SignInViewModel

class SignInActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // NOTE: SignInActivity UI
        setContent {
            AndroidFirebaseChatExTheme() {
                SignInScreen ()
            }
        }
    }
}

@Composable
private fun SignInScreen(viewModel: SignInViewModel = SignInViewModel()) {

    // View context
    val context = (LocalContext.current as Activity)

    // System dismiss
    val backHandlingEnabled by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    // System dismiss handler
    BackHandler(backHandlingEnabled) {
        if (System.currentTimeMillis() - backPressedTime <= 400L) {
            // Exit app
            context.finish()
        } else {
            // Show warning
            Toast.makeText(context, "Press again fast to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    // Check email is saved
    LaunchedEffect(Unit) {
        viewModel.checkSharedPreference(context)
    }

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
            AuthHeader(title = "Sign In")

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
            }

            // Check box
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp)
            ) {
                Checkbox(
                    checked = viewModel.isSaveEmailChecked,
                    onCheckedChange = { isChecked ->
                        viewModel.isSaveEmailChecked = isChecked
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppWhite,
                        uncheckedColor = AppWhite,
                        checkmarkColor = AppBlack
                    )
                )
                Text(
                    text = "Save my email for next time",
                    fontWeight = FontWeight.Black,
                    color = AppWhite,
                    modifier = Modifier
                        .clickable {
                            viewModel.isSaveEmailChecked = !viewModel.isSaveEmailChecked
                        }
                )
            }


            // Warning message
            AuthWarningMessage(message = viewModel.warningMessage)

            // Submit button
            AuthSubmitButton(text = "Sign In", viewModel.isSubmitButtonDisabled) {
                viewModel.signIn(context) {
                    val intent = Intent(context, FriendsListActivity::class.java)
                    context.startActivity(intent)

                    context.finish()
                }
            }

            // Navigate to sign up page
            AuthNavigationButton(text = "New to this app?", title = "Sign up now!") {
                val intent = Intent(context, SignUpActivity::class.java)
                context.startActivity(intent)
            }

            // Navigate to reset password page
            AuthNavigationButton(text = "Forgot password?", title = "Click here to reset password") {
                val intent = Intent(context, ResetPasswordActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}