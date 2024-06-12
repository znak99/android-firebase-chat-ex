package io.github.znak99.androidfirebasechatex.component.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.znak99.androidfirebasechatex.ui.theme.AppRed

@Composable
fun AuthenticationWarningMessage(
    message: String
) {
    Text(
        text = message,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = AppRed,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    )
}