package com.niraj.creditcardview.data

data class Card(
    val card_border_color: String,
    val colour: String,
    val name: String,
    val transactions: List<Transaction>
)