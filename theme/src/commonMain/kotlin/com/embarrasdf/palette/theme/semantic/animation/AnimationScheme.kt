package com.embarrasdf.palette.theme.semantic.animation

import com.embarrasdf.palette.theme.semantic.animation.finite.FiniteAnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.finite.PaletteFiniteAnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.infinite.InfiniteAnimationScheme

/**
 * The animation umbrella, grouped by spec family: [finite] holds terminating-spec schemes (screen
 * transitions today), [infinite] holds non-terminating-spec schemes (none yet).
 */
data class AnimationScheme(
    val finite: FiniteAnimationScheme = PaletteFiniteAnimationScheme,
    val infinite: InfiniteAnimationScheme = InfiniteAnimationScheme,
)

val PaletteAnimationScheme = AnimationScheme(
    finite = PaletteFiniteAnimationScheme,
    infinite = InfiniteAnimationScheme,
)
