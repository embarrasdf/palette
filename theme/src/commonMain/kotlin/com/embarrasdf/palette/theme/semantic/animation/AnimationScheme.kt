package com.embarrasdf.palette.theme.semantic.animation

data class AnimationScheme(
    val transition: AnimationSpec,
)

val PaletteAnimationScheme = AnimationScheme(
    transition = AnimationSpec.Tween(),
)

fun AnimationScheme.copy(
    token: AnimationToken,
    spec: AnimationSpec,
) = this.copy(
    transition = if (token == AnimationToken.Transition) spec else this.transition,
)
