package com.embarrasdf.palette.theme.semantic.animation

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme

enum class AnimationToken {
    Default,
    Fast,
    Slow,
}

fun AnimationToken.toSpec(animationScheme: AnimationScheme): AnimationSpec.Finite {
    return when (this) {
        AnimationToken.Default -> animationScheme.finite.default
        AnimationToken.Fast -> animationScheme.finite.fast
        AnimationToken.Slow -> animationScheme.finite.slow
    }
}

@Composable
fun AnimationToken.toSpec(): AnimationSpec.Finite {
    return toSpec(PaletteTheme.semantic.animation)
}
