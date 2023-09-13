package com.niraj.creditcardview.UIComponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.niraj.creditcardview.data.Card
import com.niraj.creditcardview.data.ViewState

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
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            userScrollEnabled = (viewState == ViewState.CardState),
            modifier = Modifier,
            pageCount = cards.size
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