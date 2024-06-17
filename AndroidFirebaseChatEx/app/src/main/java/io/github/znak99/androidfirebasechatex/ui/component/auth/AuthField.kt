package io.github.znak99.androidfirebasechatex.ui.component.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.znak99.androidfirebasechatex.ui.theme.AppBlack
import io.github.znak99.androidfirebasechatex.ui.theme.AppDefaultBackground
import io.github.znak99.androidfirebasechatex.ui.theme.AppWhite

@Composable
fun AuthField(
    title: String,
    icon: ImageVector,
    isSecure: Boolean,
    value: String,
    onValueChange: (text: String) -> Unit,
    onTrailingIconClicked: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = { text -> onValueChange(text) },
        singleLine = true,
        label = { Text(text = title) },
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        ),
        colors = TextFieldDefaults.colors(
            cursorColor = AppWhite,
            focusedContainerColor = AppWhite,
            unfocusedContainerColor = AppDefaultBackground,
            focusedLabelColor = AppBlack,
            unfocusedLabelColor = AppBlack,
            focusedTextColor = AppBlack,
            unfocusedTextColor = AppBlack
        ),
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = AppBlack,
                modifier = Modifier
                    .size(32.dp)
            )
        },
        trailingIcon = {
            if (value != "") {
                IconButton(onClick = { onTrailingIconClicked() }) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        visualTransformation = (
            if (isSecure) PasswordVisualTransformation()
            else VisualTransformation.None
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}