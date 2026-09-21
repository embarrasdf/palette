package com.embarrasdf.palette.theme.semantic.animation

data class AnimationScheme(
    val transition: AnimationSpec.Finite,
)

val PaletteAnimationScheme = AnimationScheme(
    transition = AnimationSpec.Tween(),
)

fun AnimationScheme.copy(
    token: AnimationToken,
    spec: AnimationSpec.Finite,
) = this.copy(
    transition = if (token == AnimationToken.Transition) spec else this.transition,
)
