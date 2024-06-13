package io.github.znak99.androidfirebasechatex.ui.screen.chat

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppRed

class ListActivity : ComponentActivity() {

    private val TAG = "ChatList"

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
                ListScreen(
                    auth = auth,
                    modifier = Modifier
                        .padding(WindowInsets.systemBars.asPaddingValues())
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun ListScreen(auth: FirebaseAuth, modifier: Modifier = Modifier) {

    val TAG = "ChatList"

    val context = (LocalContext.current as Activity)

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
        Button(onClick = {
            Firebase.auth.signOut()
            context.finish();
        }) {
            Text(text = "sign out")
        }

    }
}