package io.github.znak99.androidfirebasechatex.ui.screen.authentication

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
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
    
}