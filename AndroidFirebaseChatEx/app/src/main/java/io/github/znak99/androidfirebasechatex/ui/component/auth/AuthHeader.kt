package io.github.znak99.androidfirebasechatex.ui.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.znak99.androidfirebasechatex.ui.theme.AppBlack
import io.github.znak99.androidfirebasechatex.ui.theme.AppWhite
import io.github.znak99.androidfirebasechatex.ui.theme.RalewayFontFamily

@Composable
fun AuthHeader(title: String) {
    Column {// Authentication Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Person",
                tint = AppWhite,
                modifier = Modifier
                    .size(32.dp)
            )
            Text(
                text = "Authentication",
                fontSize = 20.sp,
                fontFamily = RalewayFontFamily,
                fontWeight = FontWeight.Bold,
                color = AppWhite
            )
        }
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontFamily = RalewayFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = AppWhite,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}