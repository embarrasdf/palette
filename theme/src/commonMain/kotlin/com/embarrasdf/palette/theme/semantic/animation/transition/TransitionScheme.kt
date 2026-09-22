package com.embarrasdf.palette.theme.semantic.animation.transition

import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec

/**
 * The animation specs that drive navigation transitions, one per [NavDisplay][androidx.navigation3.ui.NavDisplay]
 * slot:
 *
 * - [enter] — forward navigation (a screen being pushed on).
 * - [exit] — backward navigation (a screen being popped).
 * - [predictiveExit] — the predictive-back gesture.
 *
 * Each is a [AnimationSpec.Finite] so the transition always settles.
 */
data class TransitionScheme(
    val enter: AnimationSpec.Finite,
    val exit: AnimationSpec.Finite,
    val predictiveExit: AnimationSpec.Finite,
)

val PaletteTransitionScheme = TransitionScheme(
    enter = AnimationSpec.Tween(),
    exit = AnimationSpec.Tween(),
    predictiveExit = AnimationSpec.Tween(),
)

fun TransitionScheme.copy(
    token: TransitionToken,
    spec: AnimationSpec.Finite,
) = this.copy(
    enter = if (token == TransitionToken.Enter) spec else this.enter,
    exit = if (token == TransitionToken.Exit) spec else this.exit,
    predictiveExit = if (token == TransitionToken.PredictiveExit) spec else this.predictiveExit,
)
