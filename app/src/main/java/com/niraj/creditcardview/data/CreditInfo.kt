package com.niraj.creditcardview.data

data class CreditInfo(
    val avatar: String = "",
    val balance: Balance = Balance(),
    val cards: List<Card> = emptyList()
)