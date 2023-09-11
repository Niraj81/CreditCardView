package com.niraj.creditcardview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.*
import java.util.*

data class StateSet(
    val startState: Any,
    val endState: Any,
    val progress: Progress
){
    interface Progress{
        @Composable
        fun resolveValue() = when(this){
            is Value-> value
            is State-> state.value
            is Animation-> animateRange(
                from = from,
                to = to,
                key = key,
                animationSpec = animationSpec,
                onFinishedListener = onFinishedListener
            ).value
            else->0f
        }
    }
    class Value(val value: Float): Progress
    class State(val state: androidx.compose.runtime.State<Float>): Progress
    class Animation(
        val from: Float = 0f,
        val to: Float = 1f,
        val duration: Int = 500,
        val animationSpec: AnimationSpec<Float> = tween(duration),
        val key: Any,
        val onFinishedListener: (()->Unit)? = null
    ): Progress
}

@Composable
fun animateRange(
    from: Float,
    to: Float,
    animationSpec: AnimationSpec<Float> = tween(),
    key: Any,
    onFinishedListener: (()->Unit)? = null
): State<Float> {
    val anim = remember(from,to,key) { Animatable(from) }
    LaunchedEffect(from, to, key){
        anim.animateTo(to,animationSpec)
        onFinishedListener?.invoke()
    }
    return anim.asState()
}

@OptIn(ExperimentalMotionApi::class)
@Composable
inline fun MultiStateMotionLayout(
    modifier: Modifier = Modifier,
    stateSet: StateSet,
    stateConstraints: Map<Any,ConstraintSet>,
    transition: Transition? = null,
    debug: EnumSet<MotionLayoutDebugFlags> = EnumSet.of(MotionLayoutDebugFlags.NONE),
    crossinline content: @Composable MotionLayoutScope.() -> Unit
){
    MotionLayout(
        start = stateConstraints[stateSet.startState]!!,
        end = stateConstraints[stateSet.endState]!!,
        progress = stateSet.progress.resolveValue(),
        content = content,
        modifier = modifier,
        transition = transition,
        debug = debug
    )
}