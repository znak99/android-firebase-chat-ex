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
import io.github.znak99.androidfirebasechatex.ui.theme.AppBlack
import io.github.znak99.androidfirebasechatex.ui.theme.AppRed

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
                    color = AppBlack
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Edit Profile",
                    tint = AppBlack
                )
            },
            onClick = { editProfileCompletion() }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = "Add Friend",
                    color = AppBlack
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Friend",
                    tint = AppBlack
                )
            },
            onClick = { addFriendCompletion() }
        )

        Divider()

        DropdownMenuItem(
            text = {
                Text(
                    text = "Sign Out",
                    color = AppRed
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Sign Out",
                    tint = AppRed
                )
            },
            onClick = { signOutCompletion() }
        )
    }
}