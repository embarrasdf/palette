package com.embarrasdf.palette.theme.semantic.motion.transition

import com.embarrasdf.palette.theme.semantic.motion.Transition
import com.embarrasdf.palette.theme.semantic.motion.TransitionEffect

/** Transition presets for navigation choreography, keyed by [TransitionToken]. */
data class TransitionScheme(
    val enter: Transition = listOf(TransitionEffect.Fade(), TransitionEffect.Translate()),
    val exit: Transition = listOf(TransitionEffect.Fade(), TransitionEffect.Translate()),
    val predictiveExit: Transition = listOf(TransitionEffect.Fade(), TransitionEffect.Translate()),
)

val PaletteTransitionScheme = TransitionScheme()

fun TransitionScheme.copy(
    token: TransitionToken,
    transition: Transition,
) = this.copy(
    enter = if (token == TransitionToken.Enter) transition else this.enter,
    exit = if (token == TransitionToken.Exit) transition else this.exit,
    predictiveExit = if (token == TransitionToken.PredictiveExit) transition else this.predictiveExit,
)
