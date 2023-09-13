package com.niraj.creditcardview

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility

val defaultMargin = 20.dp

val cardState = ConstraintSet {
    val cardPager = createRefFor("cardPager")
    val transactionDetails = createRefFor("transactionDetails")
    val topBar = createRefFor("topBar")
    val cardName = createRefFor("cardName")
    val balance = createRefFor("balance")

    constrain(topBar) {
        top.linkTo(parent.top, defaultMargin)
    }
    constrain(cardName) {
        top.linkTo(parent.top, defaultMargin)
        start.linkTo(parent.absoluteLeft, defaultMargin)
        width = Dimension.percent(0.7f)
        visibility = Visibility.Invisible
    }
    constrain(balance) {
        top.linkTo(topBar.bottom, defaultMargin)
        absoluteLeft.linkTo(parent.absoluteLeft)
    }
    constrain(transactionDetails) {
        top.linkTo(parent.bottom)
    }
    constrain(cardPager) {
        top.linkTo(balance.bottom, defaultMargin)
        bottom.linkTo(parent.bottom, defaultMargin)
        absoluteLeft.linkTo(parent.absoluteLeft)
        absoluteRight.linkTo(parent.absoluteRight)
        width = Dimension.fillToConstraints
        height = Dimension.ratio("27:43")
    }
}
val halfExpandedState = ConstraintSet {
    val cardPager = createRefFor("cardPager")
    val transactionDetails = createRefFor("transactionDetails")
    val topBar = createRefFor("topBar")
    val cardName = createRefFor("cardName")
    val balance = createRefFor("balance")

    constrain(topBar) {
        bottom.linkTo(parent.top)
    }
    constrain(cardName) {
        top.linkTo(parent.top, defaultMargin)
        start.linkTo(parent.absoluteLeft, defaultMargin)
        width = Dimension.percent(0.7f)
        visibility = Visibility.Visible
    }
    constrain(balance) {
        visibility = Visibility.Invisible
    }
    constrain(cardPager) {
        top.linkTo(cardName.bottom, defaultMargin)
        absoluteLeft.linkTo(parent.absoluteLeft)
        absoluteRight.linkTo(parent.absoluteRight)
        width = Dimension.fillToConstraints
        height = Dimension.value(300.dp)
    }
    constrain(transactionDetails) {
        top.linkTo(cardPager.bottom)
    }
}
val fullyExpandedState = ConstraintSet {
    val cardPager = createRefFor("cardPager")
    val transactionDetails = createRefFor("transactionDetails")
    val topBar = createRefFor("topBar")
    val cardName = createRefFor("cardName")
    val balance = createRefFor("balance")
    val card = createRefFor("card")
    constrain(topBar) {
        bottom.linkTo(parent.top)
    }
    constrain(card) {
        height = Dimension.value(60.dp)
        width = Dimension.value(60.dp)
    }
    constrain(balance) {
        visibility = Visibility.Invisible
    }
    constrain(cardPager) {
        top.linkTo(cardName.top)
        start.linkTo(parent.absoluteLeft)
        end.linkTo(cardName.start)
        bottom.linkTo(cardName.bottom)
        width = Dimension.percent(0.25f)
        height = Dimension.ratio("43:27")
    }
    constrain(cardName) {
        top.linkTo(parent.top, defaultMargin)
        start.linkTo(cardPager.end)
        end.linkTo(parent.absoluteRight)
        bottom.linkTo(transactionDetails.top,defaultMargin)
        width = Dimension.fillToConstraints
    }
    constrain(transactionDetails) {
        top.linkTo(cardName.bottom, defaultMargin)
    }
}