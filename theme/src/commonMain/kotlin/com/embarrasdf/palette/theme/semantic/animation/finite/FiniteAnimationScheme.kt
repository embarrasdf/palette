package com.embarrasdf.palette.theme.semantic.animation.finite

import com.embarrasdf.palette.theme.semantic.animation.finite.transition.PaletteTransitionScheme
import com.embarrasdf.palette.theme.semantic.animation.finite.transition.TransitionScheme

/**
 * Animation schemes built from finite (terminating) specs. Screen transitions live here; other
 * finite-spec groups (e.g. component/surface motion) would be added alongside [transition].
 */
data class FiniteAnimationScheme(
    val transition: TransitionScheme = PaletteTransitionScheme,
)

val PaletteFiniteAnimationScheme = FiniteAnimationScheme(
    transition = PaletteTransitionScheme,
)
