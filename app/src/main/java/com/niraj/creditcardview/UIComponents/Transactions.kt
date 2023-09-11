package com.niraj.creditcardview.UIComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.niraj.creditcardview.data.Cost
import com.niraj.creditcardview.data.Transaction

@Composable
fun SingleTransaction(transaction: Transaction = Transaction("Food", Cost(2.99, "$"), "Chai Days")) {
    Row(
        modifier = Modifier.padding(top = 10.dp, bottom = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = transaction.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = Color.Gray
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = transaction.category,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
        Spacer(Modifier.weight(0.1f))
        Text(
            text = "${transaction.cost.currency}${transaction.cost.amount}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color(0xFF646464)
        )
    }
}

@Composable
fun Transactions(transactions: List<Transaction> = emptyList()) {
    Surface (
        color = Color(242, 240, 244),
        modifier = Modifier.layoutId("transactionDetails")
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            items(transactions.size) {
                SingleTransaction(transactions[it])
            }
        }
    }
}