package com.embarrasdf.palette.theme.semantic.animation

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme

enum class AnimationToken {
    Transition,
}

fun AnimationToken.toSpec(animationScheme: AnimationScheme): AnimationSpec.Finite {
    return when (this) {
        AnimationToken.Transition -> animationScheme.transition
    }
}

@Composable
fun AnimationToken.toSpec(): AnimationSpec.Finite {
    return toSpec(PaletteTheme.semantic.animation)
}
