package io.github.znak99.androidfirebasechatex.ui.screen.authentication

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppDefaultBackground
import io.github.znak99.androidfirebasechatex.ui.theme.AppGray
import io.github.znak99.androidfirebasechatex.ui.theme.AppRed
import io.github.znak99.androidfirebasechatex.viewmodel.SignInViewModel

class SignInActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // NOTE: SignInActivity UI
        setContent {
            AndroidFirebaseChatExTheme {
                SignInScreen ()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SignInScreen(viewModel: SignInViewModel = SignInViewModel()) {

    // View context
    val context = (LocalContext.current as Activity)

    // System dismiss action overriding
    val backHandlingEnabled by remember { mutableStateOf(true) }
    var backPressedTime = 0L
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

    // View
    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppDefaultBackground)
                .padding(innerPadding)
        ) {

        }
    }
}