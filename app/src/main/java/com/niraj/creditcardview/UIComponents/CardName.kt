package com.niraj.creditcardview.UIComponents

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CardName(cardName: String = "American Express Platinum") {
    Text(
        text = cardName,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Black,
        color = Color.DarkGray
    )
}