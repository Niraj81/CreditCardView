package com.niraj.creditcardview.UIComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage

@Composable
fun TopBar(avatar: String = "https://i.pravatar.cc/300") {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Cards",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.weight(5f),
            color = Color.DarkGray
        )
        AsyncImage(
            model = avatar,
            contentDescription = "User Avatar",
            modifier = Modifier
                .weight(0.7f)
                .clip(CircleShape)
        )
    }
}
