package com.embarrasdf.palette.theme.semantic.animation.finite

import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec

/**
 * Value-animation tokens built from finite (terminating) specs. Holds the reusable timing presets
 * that motion transitions and other value animations reference. For now there is a single
 * [default] preset.
 */
data class FiniteAnimationScheme(
    val default: AnimationSpec.Finite = AnimationSpec.Tween(),
)

val PaletteFiniteAnimationScheme = FiniteAnimationScheme()
