package com.embarrasdf.palette.theme.semantic.motion

/**
 * The motion (enter/exit choreography) tokens, one per navigation slot. Each is a [Transition]
 * (pattern + referenced animation preset):
 *
 * - [enter] — forward navigation.
 * - [exit] — backward navigation.
 * - [predictiveExit] — the predictive-back gesture.
 */
data class MotionScheme(
    val enter: Transition,
    val exit: Transition,
    val predictiveExit: Transition,
)

val PaletteMotionScheme = MotionScheme(
    enter = Transition(MotionPattern.SharedAxis()),
    exit = Transition(MotionPattern.SharedAxis()),
    predictiveExit = Transition(MotionPattern.SharedAxis()),
)

fun MotionScheme.copy(
    token: MotionToken,
    transition: Transition,
) = this.copy(
    enter = if (token == MotionToken.Enter) transition else this.enter,
    exit = if (token == MotionToken.Exit) transition else this.exit,
    predictiveExit = if (token == MotionToken.PredictiveExit) transition else this.predictiveExit,
)
