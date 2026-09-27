package com.embarrasdf.palette.theme.semantic.animation.infinite

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec

enum class InfiniteAnimationToken {
    Default,
    Fast,
    Slow,
}

fun InfiniteAnimationToken.toSpec(animationScheme: AnimationScheme): AnimationSpec.Infinite {
    return when (this) {
        InfiniteAnimationToken.Default -> animationScheme.infinite.default
        InfiniteAnimationToken.Fast -> animationScheme.infinite.fast
        InfiniteAnimationToken.Slow -> animationScheme.infinite.slow
    }
}

@Composable
fun InfiniteAnimationToken.toSpec(): AnimationSpec.Infinite {
    return toSpec(PaletteTheme.semantic.animation)
}
