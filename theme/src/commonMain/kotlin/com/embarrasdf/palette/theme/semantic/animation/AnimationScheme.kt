package com.embarrasdf.palette.theme.semantic.animation

import com.embarrasdf.palette.theme.semantic.animation.finite.FiniteAnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.finite.PaletteFiniteAnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.infinite.InfiniteAnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.infinite.PaletteInfiniteAnimationScheme

/**
 * Value-animation tokens, grouped by spec family: [finite] holds terminating-spec presets, [infinite]
 * holds non-terminating-spec presets.
 */
data class AnimationScheme(
    val finite: FiniteAnimationScheme = PaletteFiniteAnimationScheme,
    val infinite: InfiniteAnimationScheme = PaletteInfiniteAnimationScheme,
)

val PaletteAnimationScheme = AnimationScheme(
    finite = PaletteFiniteAnimationScheme,
    infinite = PaletteInfiniteAnimationScheme,
)
