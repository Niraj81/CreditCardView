package com.niraj.creditcardview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.Visibility
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.size.Size
import com.niraj.creditcardview.UIComponents.BalanceView
import com.niraj.creditcardview.UIComponents.CardName
import com.niraj.creditcardview.UIComponents.CreditCard
import com.niraj.creditcardview.UIComponents.TopBar
import com.niraj.creditcardview.UIComponents.Transactions
import com.niraj.creditcardview.data.Balance
import com.niraj.creditcardview.data.Card
import com.niraj.creditcardview.data.Transaction
import com.niraj.creditcardview.data.ViewState
import com.niraj.creditcardview.ui.theme.CreditCardViewTheme
import com.niraj.creditcardview.viewModel.CreditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Float.max
import java.lang.Float.min
import kotlin.contracts.contract

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreditCardViewTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

// Main screen where I want to add all the animations using MutliStateMotionLayout and stuff
@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeScreen() {
    val viewModel : CreditViewModel = viewModel()
    val credData by viewModel.CreditInfo.collectAsState()
    var selectedCard by remember { mutableStateOf(-1) }
    val transactionList = viewModel.transactions
    val cards = credData.cards
    var viewState = remember {
        mutableStateOf(ViewState.CardState)
    }
    val selectdCardName by remember {
        derivedStateOf {
            if (selectedCard == -1) "Nothing"
            else credData.cards[selectedCard].name
        }
    }
    val maxDrag = -400f
    var dragDistance by remember {
        mutableStateOf(0f)
    }
    val progress by remember {
        derivedStateOf {
            (dragDistance/maxDrag)
        }
    }

    fun onDragStarted(cardNo : Int) {
        selectedCard = cardNo
        viewModel.transactions = credData.cards[selectedCard].transactions
    }
    fun onDrag(dragAmount: Float) {
        dragDistance += dragAmount
        if(viewState.value == ViewState.CardState) {
            dragDistance = min(0f, dragDistance)
            dragDistance = max(maxDrag, dragDistance)
        } else if(viewState.value == ViewState.ExpandedState) {
            dragDistance = min(0f, dragDistance)
            dragDistance = max(2*maxDrag, dragDistance)
        } else {
            dragDistance = min(maxDrag, dragDistance)
            dragDistance = max(2*maxDrag, dragDistance)
        }
    }
    fun onDragEnded() {
        when(viewState.value) {
            ViewState.CardState -> {
                dragDistance = if(dragDistance > maxDrag/2) {
                    0f
                } else {
                    maxDrag
                }
            }
            ViewState.ExpandedState -> {
                dragDistance = if(dragDistance > maxDrag/2) {
                    0f
                } else if(dragDistance > 3*(maxDrag/2)) {
                    maxDrag
                } else {
                    2*maxDrag
                }
            }
            ViewState.FullyExpandedState -> {
                dragDistance = if(dragDistance < 3*(maxDrag/2)) {
                    -800f
                } else {
                    -400f
                }
            }
        }

        viewState.value = when (progress) {
            0f -> {
                ViewState.CardState
            }
            1f -> {
                ViewState.ExpandedState
            }
            else -> {
                ViewState.FullyExpandedState
            }
        }
        if(viewState.value == ViewState.CardState) {
            selectedCard = -1
        }

    }
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
            width = Dimension.percent(1f)
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
            top.linkTo(parent.top, defaultMargin)
            start.linkTo(parent.absoluteLeft)
            end.linkTo(cardName.start)
            width = Dimension.percent(0.25f)
            height = Dimension.ratio("43:27")
        }
        constrain(cardName) {
            top.linkTo(parent.top, defaultMargin)
            start.linkTo(cardPager.end)
            end.linkTo(parent.absoluteRight)
            width = Dimension.fillToConstraints
        }
        constrain(transactionDetails) {
            top.linkTo(cardName.bottom, defaultMargin)
        }
    }

    val startState by remember {
        derivedStateOf {
            if(progress <= 1) {
                cardState
            } else {
                halfExpandedState
            }
        }
    }
    val endState by remember {
        derivedStateOf {
            if(progress <= 1) {
                halfExpandedState
            } else {
                fullyExpandedState
            }
        }
    }

//    val animProgress by animateFloatAsState(
//        targetValue = if(progress <= 1) progress else progress - 1 ,
//        label = "",
//        animationSpec = TweenSpec (
//            durationMillis = 100
//        )
//    )
    val animProgress by remember {
        derivedStateOf {
            if(progress <= 1) progress else progress - 1
        }
    }

    if(credData.cards.isNotEmpty()) {
        MotionLayout(
            start = startState,
            end = endState,
            progress = animProgress
        ) {
            CardRow(
                modifier = Modifier.layoutId("cardPager"),
                selectedCard = selectedCard,
                progress = progress,
                viewState = viewState.value,
                cards = cards,
                onDragStarted = {
                    onDragStarted(it)
                },
                onDrag = {
                    if(viewState.value != ViewState.FullyExpandedState) onDrag(it)
                },
                onDragEnded = {
                    onDragEnded()
                }
            )
            TopBar(
                Modifier.layoutId("topBar"),
                credData.avatar
            )
            CardName(
                modifier = Modifier.layoutId("cardName"),
                cardName = selectdCardName
            )
            BalanceView(
                modifier = Modifier.layoutId("balance"),
                balance = credData.balance
            )
            Transactions(
                modifier = Modifier.layoutId("transactionDetails"),
                viewState = viewState,
                progress = progress,
                transactions = transactionList,
                onDrag = {onDrag(it)},
                onDragEnded = { onDragEnded() }
            )
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardRow(
    modifier: Modifier,
    selectedCard: Int,
    progress: Float,
    viewState: ViewState,
    cards: List<Card>,
    onDragStarted: (Int) -> Unit,
    onDrag: (Float) -> Unit,
    onDragEnded : () -> Unit
) {
    Box (
        modifier = modifier
//            .background(Color.DarkGray),
        ,
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            userScrollEnabled = viewState == ViewState.CardState,
            modifier = modifier
                .fillMaxSize()
//                .background(Color.Yellow)
                .padding(vertical = 0.dp),
            pageCount = cards.size,
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {i ->

                CreditCard(
                    Color(cards[i].colour.toColorInt()),
                    Color.White,
                    progress = progress,
                    onDragStarted = onDragStarted,
                    onDrag = onDrag,
                    onDragEnded = onDragEnded,
                    index = i,
                    alpha = if (selectedCard == -1 || selectedCard == i) 1f else 0f
                )

        }
    }
}