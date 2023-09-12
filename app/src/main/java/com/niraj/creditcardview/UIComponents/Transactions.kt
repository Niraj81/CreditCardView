package com.niraj.creditcardview.UIComponents

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.niraj.creditcardview.data.Cost
import com.niraj.creditcardview.data.Transaction
import com.niraj.creditcardview.data.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.temporal.TemporalAmount

@Composable
fun SingleTransaction(
    transaction: Transaction = Transaction("Food", Cost(2.99, "$"), "Chai Days")
) {
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
fun Transactions(
    modifier: Modifier = Modifier,
    viewState: MutableState<ViewState>,
    progress: Float,
    transactions: List<Transaction>,
    onDrag: (Float) -> Unit,
    onDragEnded: () -> Unit
) {
    val transact = transactions
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val firstItemVisible by remember {derivedStateOf {
        state.firstVisibleItemIndex
    }}
    fun scrollItem(amount: Float) {
        scope.launch {
            state.animateScrollBy(
                amount,
                animationSpec = TweenSpec (
                    durationMillis = 300,
                    easing = LinearEasing
                )
            )
        }
    }
    Surface (
        color = Color(242, 240, 244),
        modifier = modifier.clip(RoundedCornerShape(20.dp)).height(800.dp)
    ) {
        LazyColumn(
            state = state,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            if (viewState.value != ViewState.FullyExpandedState) {
                                onDrag(dragAmount)
                            } else {
                                if (dragAmount > 0f) {
                                    if (firstItemVisible == 0) {
                                        onDrag(dragAmount)
                                    } else {
                                        scrollItem(dragAmount*-50)
                                    }
                                } else {
                                    scrollItem(dragAmount*-50)
                                }
                            }
                        },
                        onDragEnd = onDragEnded
                    )
                },
            userScrollEnabled = (viewState.value == ViewState.FullyExpandedState && firstItemVisible != 0)
        ) {
            items(transact.size) {
                SingleTransaction(transact[it])
            }
        }
    }
}