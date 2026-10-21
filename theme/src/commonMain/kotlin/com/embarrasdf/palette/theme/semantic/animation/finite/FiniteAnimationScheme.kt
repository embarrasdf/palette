package com.embarrasdf.palette.theme.semantic.animation.finite

import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec

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
    token: FiniteAnimationToken,
    spec: AnimationSpec.Finite,
) = this.copy(
    default = if (token == FiniteAnimationToken.Default) spec else this.default,
    fast = if (token == FiniteAnimationToken.Fast) spec else this.fast,
    slow = if (token == FiniteAnimationToken.Slow) spec else this.slow,
)
