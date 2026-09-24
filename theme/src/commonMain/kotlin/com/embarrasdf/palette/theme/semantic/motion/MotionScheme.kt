package com.embarrasdf.palette.theme.semantic.motion

data class MotionScheme(
    val enter: Transition,
    val exit: Transition,
    val predictiveExit: Transition,
)

val PaletteMotionScheme = MotionScheme(
    enter = Transition.SharedAxis(),
    exit = Transition.SharedAxis(),
    predictiveExit = Transition.SharedAxis(),
)

fun MotionScheme.copy(
    token: MotionToken,
    transition: Transition,
) = this.copy(
    enter = if (token == MotionToken.Enter) transition else this.enter,
    exit = if (token == MotionToken.Exit) transition else this.exit,
    predictiveExit = if (token == MotionToken.PredictiveExit) transition else this.predictiveExit,
)
