package com.embarrasdf.palette.theme.semantic.animation

import com.embarrasdf.palette.theme.semantic.animation.transition.PaletteTransitionScheme
import com.embarrasdf.palette.theme.semantic.animation.transition.TransitionScheme

data class AnimationScheme(
    val transition: TransitionScheme = PaletteTransitionScheme,
)

val PaletteAnimationScheme = AnimationScheme(
    transition = PaletteTransitionScheme,
)
