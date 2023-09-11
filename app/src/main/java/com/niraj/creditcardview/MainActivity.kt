package com.niraj.creditcardview

import android.graphics.Paint.Align
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsAnimation.Bounds
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.Transition
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.niraj.creditcardview.data.Balance
import com.niraj.creditcardview.data.Card
import com.niraj.creditcardview.data.Cost
import com.niraj.creditcardview.data.Transaction
import com.niraj.creditcardview.ui.theme.CreditCardViewTheme
import com.niraj.creditcardview.viewModel.CreditViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                    CardRow(modifier = Modifier, swipeProgress = 1f, scale = 1f)
//                    Column (
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ){
//                        var stateSet by remember {
//                            mutableStateOf(StateSet(1, 0, StateSet.Animation(key = "10")))
//                        }
//                        Button(
//                            onClick = {
//                                val start = ((stateSet.startState as Int) + 1) % 2
//                                val end = ((stateSet.endState as Int) + 1) % 2
//                                stateSet = StateSet(
//                                    startState = start,
//                                    endState = end,
//                                    StateSet.Animation(key = "${start}${end}")
//                                )
//                            }
//                        ) {
//                            Text("Animate")
//                        }
//                        HomeScreen(stateSet)
//                    }
                }
            }
        }
    }
}

// Normal preview composable for looking at all the composables
//@Composable
//fun HomeScreen() {
//    val viewModel : CreditViewModel = viewModel()
//    val credData by viewModel.CreditInfo.collectAsState()
//    Column (
//        modifier = Modifier.background(Color(241, 241, 243)).padding(horizontal = 20.dp)
//    ) {
//        TopBar(credData.avatar)
//        BalanceView(credData.balance)
//        CardName("American Express Platinum")
//        if(credData.cards.isNotEmpty()) {
//            Transactions(transactions = credData.cards[0].transactions)
//        }
//    }
//}


// Main screen where I want to add all the animations using MutliStateMotionLayout and stuff
@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeScreen(stateSet: StateSet) {
    val viewModel : CreditViewModel = viewModel()
    val credData by viewModel.CreditInfo.collectAsState()

    val cardState = ConstraintSet {

    }
    val halfExpandedState = ConstraintSet {

    }
    val fullyExpandedState = ConstraintSet {

    }

    val currentProgress by remember { mutableStateOf(0f)}

    MotionLayout(
        start = cardState,
        end = halfExpandedState,
        progress = currentProgress
    ) {
        CardRow(modifier = Modifier.layoutId("credit_cards"), swipeProgress = 1f, scale = 1f)
        Transactions(transactions = if(credData.cards.size > 0) credData.cards[0].transactions else emptyList())
    }

//    MultiStateMotionLayout(
//        modifier = Modifier.fillMaxSize(),
//        stateSet = stateSet,
//        stateConstraints = mapOf(
//            0 to ConstraintSet {
//                val transactions = createRefFor("transactions")
//                val creditCards = createRefFor("credit_cards")
//                constrain(transactions) {
//                    top.linkTo(creditCards.bottom)
//                }
//
//            },
//            1 to ConstraintSet {
//                val creditCards = createRefFor("credit_cards")
//                val transactions = createRefFor("transactions")
//
//                constrain(transactions) {
//                    top.linkTo(creditCards.bottom)
//                }
//            }
//        )
//    ) {
//        CardRow(modifier = Modifier.layoutId("credit_cards"), swipeProgress = 1f, scale = 1f)
//        Transactions(transactions = if(credData.cards.size > 0) credData.cards[0].transactions else emptyList())
//    }
}

@Preview
@Composable
fun CardName(cardName: String = "American Express Platinum") {
    Text(
        text = cardName,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Black,
        color = Color.DarkGray
    )
}

@Composable
fun Transactions(transactions: List<Transaction> = emptyList()) {
    Surface (
        color = Color(242, 240, 244),
        modifier = Modifier.layoutId("transactions")
    ) {
        LazyColumn() {
            items(transactions.size) {
                SingleTransaction(transactions[it])
            }
        }
    }
}

@Preview
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
            color = Color(0xFF646464),

        )
    }
}

@Preview
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

@Preview
@Composable
fun BalanceView(balance: Balance = Balance(12000.0, "$")) {
    Column (
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Balance",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${balance.currency}${balance.amount}",
            style = MaterialTheme.typography.displaySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Black
        )
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardRow(
    modifier: Modifier,
    swipeProgress: Float,
    scale: Float
) {
    var enabledCard by remember { (mutableStateOf(-1)) }
    Box (
        modifier = Modifier.wrapContentSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            modifier = modifier
                .background(Color.LightGray)
                .wrapContentSize()
                .padding(vertical = 20.dp),
            pageCount = 3,
            contentPadding = PaddingValues(horizontal = 3.dp)
        ) {i ->
            if(enabledCard == -1 || enabledCard == i) {
                CreditCard(
                    Color.Black,
                    onSwipeDone = { progress, ind ->
                        enabledCard = if(progress != 1f) -1
                        else ind
                    },
                    onSwipeStarted = {
                        enabledCard = it
                    },
                    index = i
                )
            }
        }
    }
}


//@Preview
@Composable
fun CreditCard(color: Color = Color.Cyan, onSwipeDone: (Float, Int) -> Unit, onSwipeStarted : (Int) -> Unit, index : Int ) {

    var dragDistance by remember { mutableStateOf(0f) }
    val progress by animateFloatAsState(targetValue = dragDistance/-400f)
    val rotation by animateFloatAsState(
        targetValue = progress * 90f,
        label = "",
        animationSpec = TweenSpec (durationMillis = 200)
    )


    // Takes care of the input (only Vertical) - Horizontal is taken care by horizontal pager
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {
                        onSwipeStarted(index)
                    },
                    onDragEnd = {
                        if (dragDistance < -200)
                            dragDistance = -400f
                        else dragDistance = 0f
                        onSwipeDone(dragDistance / -400f, index)
                    }
                ) { change, dragAmount ->
                    change.consume()
                    dragDistance += dragAmount
                    if (dragDistance > 0f) dragDistance = 0f
                    else if (dragDistance < -400f) dragDistance = -400f
                }
            }
    ) {
        Card (
            modifier = Modifier
                .rotate(rotation)
                .fillMaxHeight(0.43f + (1 - progress) * 0.20f)
                .fillMaxWidth(0.60f + (1 - progress) * 0.23f),
            border = BorderStroke(3.dp, Color.White),
            shape = RoundedCornerShape(29.dp),
            colors = CardDefaults.cardColors(containerColor = Color(248, 195, 63))
        ) {

        }
    }
}