package io.github.znak99.androidfirebasechatex.ui.screen.chat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.znak99.androidfirebasechatex.app.DEFAULT_THUMBNAIL_IMAGE
import io.github.znak99.androidfirebasechatex.app.FIRESTORE_STORAGE_URL
import io.github.znak99.androidfirebasechatex.app.REALTIME_DB_URL
import io.github.znak99.androidfirebasechatex.component.chat.FriendsListHeaderMenu
import io.github.znak99.androidfirebasechatex.dto.UserDTO
import io.github.znak99.androidfirebasechatex.ui.screen.authentication.SignInActivity
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme
import io.github.znak99.androidfirebasechatex.ui.theme.AppBlack
import io.github.znak99.androidfirebasechatex.ui.theme.AppDarkGray
import io.github.znak99.androidfirebasechatex.ui.theme.AppGray
import io.github.znak99.androidfirebasechatex.ui.theme.Pink80
import io.github.znak99.androidfirebasechatex.ui.theme.PurpleGrey40

class FriendsListActivity : ComponentActivity() {

    // Firebase authentication
    private lateinit var auth: FirebaseAuth

    // Firebase realtime database
    private lateinit var database: DatabaseReference

    // Firebase firestore
    private lateinit var storage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance(REALTIME_DB_URL).reference
        storage = FirebaseStorage.getInstance(FIRESTORE_STORAGE_URL).reference

        // NOTE: ListActivity UI
        setContent {
            AndroidFirebaseChatExTheme {
                ListScreen(
                    auth = auth,
                    database = database,
                    storage = storage
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreen(auth: FirebaseAuth, database: DatabaseReference, storage: StorageReference) {

    val TAG = "FriendsList"

    val context = (LocalContext.current as Activity)

    val currentUser = auth.currentUser

    var username by remember { mutableStateOf("") }
    var userProfileImage by remember { mutableStateOf<String?>(null) }
    var friendsList by remember { mutableStateOf<List<UserDTO>>(emptyList()) }
    var menuExpanded by remember { mutableStateOf(false) } // Header menu

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            val currentUserRef = database.child("users").child(user.uid)
            currentUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { data ->
                        // Fetch signed in user data
                        val userDTO = data.getValue(UserDTO::class.java)
                        Log.d(TAG, "Username: ${userDTO?.username}")
                        username = userDTO?.username ?: "Unknown"

                        // Fetch signed in user's friends data
                        userDTO?.friendsId?.map { id ->
                            val friendRef = database.child("users").child(id)
                            friendRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.children.forEach { friendData ->
                                        val friendDTO = friendData.getValue(UserDTO::class.java)

                                        if (friendDTO != null) {
                                            friendsList = friendsList.toMutableList().apply { add(friendDTO) }
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
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

    LaunchedEffect(storage.child(DEFAULT_THUMBNAIL_IMAGE)) {
        storage.child(DEFAULT_THUMBNAIL_IMAGE).downloadUrl.addOnSuccessListener { uri ->
            userProfileImage = uri.toString()
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
                        addFriendCompletion = {
                            val intent = Intent(context, AddFriendActivity::class.java)
                            context.startActivity(intent)
                        },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        ) {
            item {
                Text(
                    text = "My profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(color = AppGray)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        }
                ) {
                    Image(
                        painter = rememberImagePainter(data = userProfileImage),
                        contentDescription = "User Profile Image",
                        modifier = Modifier
                            .padding(12.dp)
                            .width(48.dp)
                            .height(48.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = AppGray,
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.FillBounds
                    )
                    Column {
                        Text(
                            text = currentUser?.email!!,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = AppDarkGray,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                        Text(
                            text = username,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
                Divider()
                Text(
                    text = "My friends",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(color = AppGray)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth()
                )
            }

            items(friendsList) { friend ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        }
                ) {
                    Image(
                        painter = rememberImagePainter(data = userProfileImage),
                        contentDescription = "User Profile Image",
                        modifier = Modifier
                            .padding(12.dp)
                            .width(48.dp)
                            .height(48.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = AppGray,
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.FillBounds
                    )
                    Column {
                        Text(
                            text = friend.email!!,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = AppDarkGray,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                        Text(
                            text = friend.username!!,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
