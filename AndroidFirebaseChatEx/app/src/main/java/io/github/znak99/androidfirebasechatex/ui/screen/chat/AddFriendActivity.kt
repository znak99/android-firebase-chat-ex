package io.github.znak99.androidfirebasechatex.ui.screen.chat

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
import io.github.znak99.androidfirebasechatex.model.firebase.User
import io.github.znak99.androidfirebasechatex.ui.theme.AndroidFirebaseChatExTheme

class AddFriendActivity : ComponentActivity() {

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

        setContent {
            AndroidFirebaseChatExTheme {
                AddFriendScreen(
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
private fun AddFriendScreen(auth: FirebaseAuth, database: DatabaseReference, storage: StorageReference) {

    val TAG = "AddFriend"

    val context = (LocalContext.current as Activity)

    val currentUser = auth.currentUser

    var searchKeyword by remember { mutableStateOf("") }
    var isShowResult by remember { mutableStateOf(false) }
    var foundUser by remember { mutableStateOf<User?>(null) }
    var foundUserProfileImage by remember { mutableStateOf<String?>(null) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
                        text = "Add friend",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        context.finish()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Menu"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        ) {
            item {
                Text(
                    text = "Search user by email",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                OutlinedTextField(
                    value = searchKeyword,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .padding(top = 4.dp),
                    label = { Text("Enter email") },
                    onValueChange = { value ->
                        if (!value.contains(" ")) {
                            searchKeyword = value
                        }
                    }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        searchEmail(
                            email = searchKeyword,
                            database = database,
                            completion = { user ->
                                isShowResult = true
                                Log.d(TAG, "result exist")
                                foundUser = user

                                storage.child(DEFAULT_THUMBNAIL_IMAGE).downloadUrl.addOnSuccessListener { uri ->
                                    foundUserProfileImage = uri.toString()
                                }
                                searchKeyword = ""
                            }
                        )
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Button")

                        Text(
                            text = "Search",
                            modifier = Modifier
                                .padding(start = 4.dp)
                        )
                    }
                }

                if (isShowResult) {
                    Text(
                        text = "Result",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    if (foundUser != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                                .padding(horizontal = 8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = rememberImagePainter(data = foundUserProfileImage),
                                contentDescription = "User Profile Image",
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(120.dp)
                                    .clip(CircleShape)
                                    .fillMaxWidth(),
                                alignment = Alignment.Center,
                                contentScale = ContentScale.FillBounds
                            )
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = foundUser?.username.toString(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = foundUser?.email.toString(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                            Button(onClick = { addFriend(currentUser = currentUser, database = database, targetUser = foundUser) }) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Filled.Add, contentDescription = "ADD")
                                    Text(text = "Add to friends list")
                                }
                            }
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                        ) {
                            Text(
                                text = "Email Not Found",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )

                            Text(
                                text = "Please check email and try again",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun searchEmail(email: String, database: DatabaseReference, completion: (user: User?) -> Unit) {
    val usersRef = database.child("users")
    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { uidSnapshot ->
                uidSnapshot.children.forEach { userSnapshot ->
                    val user = userSnapshot.getValue(User::class.java)
                    Log.d("AddFriend", user?.email.toString())

                    if (email == user?.email.toString()) {
                        completion(user)
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}

private fun addFriend(currentUser: FirebaseUser?, database: DatabaseReference, targetUser: User?) {

}