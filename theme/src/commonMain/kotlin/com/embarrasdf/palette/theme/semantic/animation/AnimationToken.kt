package com.embarrasdf.palette.theme.semantic.animation

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme

/**
 * Named value-animation presets. Motion transitions (and other value animations) reference these
 * rather than inlining a spec, so timing character is defined once. For now there is a single
 * [Default] preset.
 */
enum class AnimationToken {
    Default,
}

fun AnimationToken.toSpec(animationScheme: AnimationScheme): AnimationSpec.Finite {
    return when (this) {
        AnimationToken.Default -> animationScheme.finite.default
    }
}

@Composable
fun AnimationToken.toSpec(): AnimationSpec.Finite {
    return toSpec(PaletteTheme.semantic.animation)
}
