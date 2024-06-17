package io.github.znak99.androidfirebasechatex.ui.component.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.znak99.androidfirebasechatex.ui.theme.Purple40

@Composable
fun AuthenticationHeader(
    title: String,
    icon: Painter,
) {
    Column {
        Text(
            text = "Firebase Chat",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Purple40,
                modifier = Modifier
                    .padding(end = 8.dp)
            )

            Image(
                painter = icon,
                contentDescription = "$title Icon",
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp)
            )
        }
    }
}