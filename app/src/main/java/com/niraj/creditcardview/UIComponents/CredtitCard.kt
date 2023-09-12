package com.niraj.creditcardview.UIComponents

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import java.lang.Float.max
import java.lang.Float.min

@Composable
fun CreditCard(
    color: Color = Color.Cyan,
    borderColor: Color = Color.White,
    progress : Float,
    onDragStarted: (Int) -> Unit,
    onDrag : (Float) -> Unit,
    onDragEnded : () -> Unit,
    index: Int,
    alpha : Float
) {
    val currentProgress by animateFloatAsState(
        targetValue = progress,
        label = "",
        animationSpec = TweenSpec (durationMillis = 150, easing = LinearEasing)
    )

    val rotation by animateFloatAsState(
        targetValue = min(1f, progress) * 90f,
        label = "",
        animationSpec = TweenSpec (durationMillis = 150, easing = FastOutSlowInEasing)
    )

    // Takes care of the input (only Vertical) - Horizontal is taken care by horizontal pager
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {
                        onDragStarted(index)
                    },
                    onDragEnd = onDragEnded
                ) { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount)
                }
            }
    ) {
        Card (
            modifier = Modifier
                .alpha(alpha)
                .graphicsLayer(
                    rotationZ = rotation
                )
                .fillMaxHeight(0.8f + min(1f, max(0f, progress - 0.5f)) * 0.2f)
                .aspectRatio(27 / 43f),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
            border = BorderStroke(3.dp, borderColor),
            shape = RoundedCornerShape(12.dp + 2.dp * (1 - currentProgress)),
            colors = CardDefaults.cardColors(containerColor = color)
        ) {
        }
    }
}