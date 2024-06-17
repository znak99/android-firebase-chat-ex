package io.github.znak99.androidfirebasechatex.ui.component.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.znak99.androidfirebasechatex.ui.theme.AppWarning

@Composable
fun AuthWarningMessage(message: String) {
    Text(
        text = message,
        color = AppWarning,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .padding(horizontal = 12.dp)
    )
}