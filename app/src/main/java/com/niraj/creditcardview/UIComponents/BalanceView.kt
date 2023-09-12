package com.niraj.creditcardview.UIComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niraj.creditcardview.data.Balance

@Composable
fun BalanceView(
    modifier: Modifier = Modifier,
    balance: Balance
) {
    Column (
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Balance",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${currencyToSymbol(balance.currency)}${balance.amount}",
            color = Color.Gray,
            fontWeight = FontWeight.Black,
            fontSize = 27.sp
        )
    }
}