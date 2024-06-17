package io.github.znak99.androidfirebasechatex.ui.component.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.znak99.androidfirebasechatex.ui.theme.AppBlack

@Composable
fun AuthNavigationButton(text: String, title: String, navigate: () -> Unit) {
    Row() {
        Spacer(modifier = Modifier)
        ElevatedButton(
            onClick = { navigate() },
            modifier = Modifier
                .padding(top = 20.dp)
        ) {
            Row {
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = AppBlack,
                    modifier = Modifier
                        .padding(end = 4.dp)
                )
                Text(
                    text = title,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = AppBlack,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }

}