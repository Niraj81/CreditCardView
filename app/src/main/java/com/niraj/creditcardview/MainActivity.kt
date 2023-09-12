package com.niraj.creditcardview

import android.graphics.Paint.Align
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsAnimation.Bounds
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.runtime.derivedStateOf
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
import com.niraj.creditcardview.UIComponents.SingleTransaction
import com.niraj.creditcardview.UIComponents.Transactions
import com.niraj.creditcardview.data.Balance
import com.niraj.creditcardview.data.Card
import com.niraj.creditcardview.data.Cost
import com.niraj.creditcardview.data.Transaction
import com.niraj.creditcardview.data.ViewState
import com.niraj.creditcardview.ui.theme.CreditCardViewTheme
import com.niraj.creditcardview.viewModel.CreditViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Float.max
import java.lang.Float.min

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
//                    CardRow(modifier = Modifier, swipeProgress = 1f, scale = 1f)
                    HomeScreen()
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
fun HomeScreen() {
    val viewModel : CreditViewModel = viewModel()
    val credData by viewModel.CreditInfo.collectAsState()

    val maxDrag = -400f
    var viewState by remember {
        mutableStateOf(ViewState.CardState)
    }
    var dragDistance by remember {
        mutableStateOf(0f)
    }

    val progress by remember {
        derivedStateOf {
            dragDistance/maxDrag
        }
    }

    val animProgress by animateFloatAsState(
        targetValue = progress
    )

    fun dragEnded() {
        dragDistance = if(dragDistance > maxDrag/2) {
            0f
        } else if(dragDistance > 3*(maxDrag/2)) {
            -400f
        } else {
            -800f
        }
        viewState = if(progress == 0f) {
            ViewState.CardState
        } else if(progress == 1f) {
            ViewState.ExpandedState
        } else {
            ViewState.FullyExpandedState
        }
    }
    fun onDrag(dragAmount: Float) {
        dragDistance += dragAmount
        dragDistance = min(0f, dragAmount)
        dragDistance = max(maxDrag*2, dragAmount)
    }

    val cardState = ConstraintSet {
        val cardPager = createRefFor("cardPager")
        val transactionDetails = createRefFor("transactionDetails")
        constrain(transactionDetails) {
            top.linkTo(cardPager.bottom)
        }
    }
    val halfExpandedState = ConstraintSet {
        val cardPager = createRefFor("cardPager")
        val transactionDetails = createRefFor("transactionDetails")
        constrain(transactionDetails) {
            top.linkTo(cardPager.bottom)
        }
    }
    val fullyExpandedState = ConstraintSet {
        val cardPager = createRefFor("cardPager")
        constrain(cardPager) {
            top.linkTo(parent.top, 15.dp)
            start.linkTo(parent.start, 15.dp)
        }
    }

    var currentProgress by remember { mutableStateOf(0f)}
//    val animProgress by animateFloatAsState(
//        targetValue = currentProgress,
//        animationSpec = TweenSpec(
//            durationMillis = 100,
//            easing = LinearEasing
//        ), label = ""
//    )
    MotionLayout(
        start = cardState,
        end = halfExpandedState,
        progress = animProgress
    ) {
        CardRow(
            modifier = Modifier,
            dragProgress = {
                currentProgress = it
            }
        )
        Transactions(transactions = if(credData.cards.size > 0) credData.cards[0].transactions else emptyList())
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardRow(
    modifier: Modifier,
    dragProgress: (Float) -> Unit
) {
    var enabledCard by remember { (mutableStateOf(-1)) }
    Box (
        modifier = Modifier
            .layoutId("cardPager")
            .wrapContentSize()
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
                    Color.White,
                    onSwipeDone = { progress, ind ->
                        enabledCard = if(progress != 1f) -1
                        else ind
                    },
                    onSwipeStarted = {
                        enabledCard = it
                    },
                    currentProgress = dragProgress,
                    index = i
                )
            }
        }
    }
}


//@Preview
@Composable
fun CreditCard(
    color: Color = Color.Cyan,
    borderColor: Color = Color.White,
    onSwipeDone: (Float, Int) -> Unit,
    onSwipeStarted : (Int) -> Unit,
    currentProgress: (Float) -> Unit,
    index: Int,
) {

    var dragDistance by remember { mutableStateOf(0f) }
    val progress by remember { derivedStateOf { dragDistance/-400f }}
    val rotation by animateFloatAsState(
        targetValue = progress * 90f,
        label = "",
        animationSpec = TweenSpec (durationMillis = 150, easing = FastOutLinearInEasing)
    )
    val maxDrag = -400f


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
                        if (dragDistance < maxDrag/2)
                            dragDistance = maxDrag
                        else dragDistance = 0f
                        onSwipeDone(progress, index)
                    }
                ) { change, dragAmount ->
                    change.consume()
                    dragDistance += dragAmount
                    if (dragDistance > 0f) dragDistance = 0f
                    else if (dragDistance < maxDrag) dragDistance = maxDrag
                    currentProgress(progress)
                }
            }
    ) {
        Card (
            modifier = Modifier
                .rotate(rotation)
                .fillMaxHeight(0.43f + (1 - progress) * 0.20f)
                .fillMaxWidth(0.60f + (1 - progress) * 0.23f),
            border = BorderStroke(3.dp, borderColor),
            shape = RoundedCornerShape(29.dp),
            colors = CardDefaults.cardColors(containerColor = color)
        ) {
        }
    }
}