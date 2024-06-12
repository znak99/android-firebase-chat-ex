package io.github.znak99.androidfirebasechatex.component.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthenticationField(
    text: String,
    label: String,
    icon: Painter,
    isSecureField: Boolean,
    onValueChange: (value: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = "Email Icon",
            modifier = Modifier
                .padding(12.dp)
                .size(36.dp)
        )

        TextField(
            value = text,
            onValueChange = { value -> onValueChange(value) },
            label = { Text(text = label) },
            singleLine = true,
            visualTransformation = (
                if (isSecureField) PasswordVisualTransformation()
                else VisualTransformation.None
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
