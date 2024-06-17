package io.github.znak99.androidfirebasechatex.ui.component.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FriendsListHeaderMenu(
    expanded: Boolean,
    setExpanded: (flag: Boolean) -> Unit,
    editProfileCompletion: () -> Unit,
    addFriendCompletion: () -> Unit,
    signOutCompletion: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            setExpanded(false)
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Edit Profile",
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Edit Profile",
                )
            },
            onClick = { editProfileCompletion() }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = "Add Friend",
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Friend",
                )
            },
            onClick = { addFriendCompletion() }
        )

        Divider()

        DropdownMenuItem(
            text = {
                Text(
                    text = "Sign Out",
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Sign Out",
                )
            },
            onClick = { signOutCompletion() }
        )
    }
}