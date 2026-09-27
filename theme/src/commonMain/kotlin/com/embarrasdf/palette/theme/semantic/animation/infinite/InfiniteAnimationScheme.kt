package com.embarrasdf.palette.theme.semantic.animation.infinite

import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec

/** Infinite (non-terminating) value-animation presets — loops / ambient motion — keyed by tempo. */
data class InfiniteAnimationScheme(
    val default: AnimationSpec.Infinite = AnimationSpec.Repeatable(
        animation = AnimationSpec.Tween(durationMillis = 1000),
    ),
    val fast: AnimationSpec.Infinite = AnimationSpec.Repeatable(
        animation = AnimationSpec.Tween(durationMillis = 500),
    ),
    val slow: AnimationSpec.Infinite = AnimationSpec.Repeatable(
        animation = AnimationSpec.Tween(durationMillis = 2000),
    ),
)

val PaletteInfiniteAnimationScheme = InfiniteAnimationScheme()

fun InfiniteAnimationScheme.copy(
    token: InfiniteAnimationToken,
    spec: AnimationSpec.Infinite,
) = this.copy(
    default = if (token == InfiniteAnimationToken.Default) spec else this.default,
    fast = if (token == InfiniteAnimationToken.Fast) spec else this.fast,
    slow = if (token == InfiniteAnimationToken.Slow) spec else this.slow,
)
