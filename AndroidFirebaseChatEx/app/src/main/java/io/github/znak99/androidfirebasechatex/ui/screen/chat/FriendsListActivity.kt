package io.github.znak99.androidfirebasechatex.ui.screen.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import io.github.znak99.androidfirebasechatex.app.REALTIME_DB_URL
import io.github.znak99.androidfirebasechatex.component.chat.FriendsListHeaderMenu
import io.github.znak99.androidfirebasechatex.dto.UserDTO
import io.github.znak99.androidfirebasechatex.ui.screen.authentication.SignInActivity
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppRed

class FriendsListActivity : ComponentActivity() {

    // Firebase authentication
    private lateinit var auth: FirebaseAuth

    // Firebase realtime database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance(REALTIME_DB_URL).reference

        // NOTE: ListActivity UI
        setContent {
            AndroidFirebaseChatExTheme {
                ListScreen(
                    auth = auth,
                    database = database
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreen(auth: FirebaseAuth, database: DatabaseReference) {

    val TAG = "List"

    val context = (LocalContext.current as Activity)

    val currentUser = auth.currentUser

    var username by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) } // Header menu

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            val userRef = database.child("users").child(user.uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { data ->
                        val userDTO = data.getValue(UserDTO::class.java)

                        Log.d(TAG, "Username: ${userDTO?.username}")
                        if (userDTO?.uid.equals(currentUser.uid)) {
                            username = userDTO?.username ?: "Unknown"
                            return
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "----Firebase realtime database error")
                    Log.e(TAG, error.message)
                }
            })
        }
    }

    // System dismiss action overriding
    var backHandlingEnabled by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    BackHandler(backHandlingEnabled) {
        if (System.currentTimeMillis() - backPressedTime <= 400L) { // EXIT
            context.finish()
        } else { // SHOW TOAST
            Toast.makeText(context, "Press again fast to exit\n Or sign out at header menu", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "My Friends",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(
                            imageVector = if (menuExpanded) Icons.Filled.Close else Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }

                    FriendsListHeaderMenu(
                        expanded = menuExpanded,
                        setExpanded = { flag -> menuExpanded = flag},
                        editProfileCompletion = { /*TODO*/ },
                        addFriendCompletion = { /*TODO*/ },
                        signOutCompletion = {
                            auth.signOut()

                            val intent = Intent(context, SignInActivity::class.java)
                            context.startActivity(intent)

                            context.finish()
                        }
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        val range = 1..100
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        ) {
            items(range.count()) { index ->
                Text(text = "- List item number ${index + 1}")
            }
        }
    }
}