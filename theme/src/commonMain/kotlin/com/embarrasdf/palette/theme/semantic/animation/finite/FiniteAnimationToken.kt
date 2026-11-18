package com.embarrasdf.palette.theme.semantic.animation.finite

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec

enum class FiniteAnimationToken {
    Default,
    Fast,
    Slow,
}

fun FiniteAnimationToken.toSpec(animationScheme: AnimationScheme): AnimationSpec.Finite {
    return when (this) {
        FiniteAnimationToken.Default -> animationScheme.finite.default
        FiniteAnimationToken.Fast -> animationScheme.finite.fast
        FiniteAnimationToken.Slow -> animationScheme.finite.slow
    }
}

@Composable
fun FiniteAnimationToken.toSpec(): AnimationSpec.Finite {
    return toSpec(PaletteTheme.semantic.animation)
}
