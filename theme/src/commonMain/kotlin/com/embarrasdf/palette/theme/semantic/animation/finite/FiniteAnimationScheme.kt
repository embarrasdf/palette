package com.embarrasdf.palette.theme.semantic.animation.finite

import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.AnimationToken

/**
 * Named value-animation presets built from finite (terminating) specs.
 */
data class FiniteAnimationScheme(
    val default: AnimationSpec.Finite = AnimationSpec.Tween(),
    val fast: AnimationSpec.Finite = AnimationSpec.Tween(durationMillis = 150),
    val slow: AnimationSpec.Finite = AnimationSpec.Tween(durationMillis = 500),
)

val PaletteFiniteAnimationScheme = FiniteAnimationScheme()

fun FiniteAnimationScheme.copy(
    token: AnimationToken,
    spec: AnimationSpec.Finite,
) = this.copy(
    default = if (token == AnimationToken.Default) spec else this.default,
    fast = if (token == AnimationToken.Fast) spec else this.fast,
    slow = if (token == AnimationToken.Slow) spec else this.slow,
)
