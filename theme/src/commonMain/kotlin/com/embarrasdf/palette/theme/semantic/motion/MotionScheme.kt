package com.embarrasdf.palette.theme.semantic.motion

data class MotionScheme(
    val enter: Transition,
    val exit: Transition,
    val predictiveExit: Transition,
)

val PaletteMotionScheme = MotionScheme(
    enter = listOf(TransitionEffect.Fade(), TransitionEffect.Translate()),
    exit = listOf(TransitionEffect.Fade(), TransitionEffect.Translate()),
    predictiveExit = listOf(TransitionEffect.Fade(), TransitionEffect.Translate()),
)

fun MotionScheme.copy(
    token: MotionToken,
    transition: Transition,
) = this.copy(
    enter = if (token == MotionToken.Enter) transition else this.enter,
    exit = if (token == MotionToken.Exit) transition else this.exit,
    predictiveExit = if (token == MotionToken.PredictiveExit) transition else this.predictiveExit,
)
