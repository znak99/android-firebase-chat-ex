package io.github.znak99.androidfirebasechatex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.ui.screen.authentication.SignInActivity
import io.github.znak99.androidfirebasechatex.ui.screen.chat.ListActivity
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme

class MainActivity : ComponentActivity() {

    private val TAG = "Main";

    // Firebase authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize
        auth = Firebase.auth

        // NOTE: MainActivity UI
        setContent {
            AndroidFirebaseChatExTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) {
                    var intent: Intent

                    if (auth.currentUser == null) { // No user session
                        Log.d(TAG, "No user session - navigate to SignInActivity")
                        intent = Intent(this, SignInActivity::class.java)
                    } else { // User session exists
                        Log.d(TAG, "User session exists - navigate to ListActivity")
                        intent = Intent(this, ListActivity::class.java)
                    }

                    startActivity(intent)

                    // To prevent navigate from return to this activity
                    finish()
                }
            }
        }
    }
}