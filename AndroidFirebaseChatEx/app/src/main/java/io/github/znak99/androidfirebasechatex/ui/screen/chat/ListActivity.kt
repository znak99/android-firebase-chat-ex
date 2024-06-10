package io.github.znak99.androidfirebasechatex.ui.screen.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme

class ListActivity : ComponentActivity() {

    private val TAG = "ChatList"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val callback = ListActivity().onBackPressedDispatcher.addCallback(this) {

        }.handleOnBackPressed()

        setContent {
            AndroidFirebaseChatExTheme {
                ChatListScreen(
                    modifier = Modifier
                        .padding(WindowInsets.systemBars.asPaddingValues())
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatListScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Surface(
        modifier = modifier
    ) {
        Button(onClick = {
            Firebase.auth.signOut()
            context as ListActivity
            context.finish();
        }) {
            Text(text = "sign out")
        }
    }
}