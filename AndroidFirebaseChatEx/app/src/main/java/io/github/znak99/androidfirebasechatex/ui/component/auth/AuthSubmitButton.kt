package io.github.znak99.androidfirebasechatex.ui.component.auth

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.znak99.androidfirebasechatex.ui.screen.chat.FriendsListActivity

@Composable
fun AuthSubmitButton(text: String, isSubmitButtonDisabled: Boolean, completion: () -> Unit) {
    ElevatedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        onClick = { completion() }
    ) {
        if (!isSubmitButtonDisabled) {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}