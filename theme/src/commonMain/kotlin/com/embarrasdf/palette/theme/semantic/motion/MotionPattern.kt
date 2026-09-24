package com.embarrasdf.palette.theme.semantic.motion

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.toComposeSpec

/**
 * The enter/exit choreography vocabulary — *what* moves as an element appears and disappears. A
 * pattern is timed by a value-animation spec (see [toEnter]/[toExit]) and produces Compose
 * [EnterTransition]/[ExitTransition]/[ContentTransform], so it works with `AnimatedVisibility`,
 * `AnimatedContent`, and `NavDisplay` alike — it is not tied to any navigation library.
 */
sealed interface MotionPattern {
    /** Fade combined with a partial-width horizontal slide (Material's shared-axis, reduced-motion). */
    data class SharedAxis(val fraction: Float = 0.2f) : MotionPattern

    /** Crossfade only. */
    data object Fade : MotionPattern

    /** Fade combined with a scale from/to [from]. */
    data class Scale(val from: Float = 0.9f) : MotionPattern
}

enum class MotionPatternType {
    SharedAxis,
    Fade,
    Scale,
}

fun MotionPattern.type(): MotionPatternType = when (this) {
    is MotionPattern.SharedAxis -> MotionPatternType.SharedAxis
    is MotionPattern.Fade -> MotionPatternType.Fade
    is MotionPattern.Scale -> MotionPatternType.Scale
}

fun MotionPattern.toEnter(
    spec: AnimationSpec.Finite,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): EnterTransition {
    val floatSpec = spec.toComposeSpec<Float>(easings)
    val direction = if (forward) 1 else -1
    return when (this) {
        is MotionPattern.SharedAxis -> {
            val offsetSpec = spec.toComposeSpec<IntOffset>(easings)
            fadeIn(floatSpec) +
                slideInHorizontally(offsetSpec) { width -> (direction * width * fraction).toInt() }
        }
        MotionPattern.Fade -> fadeIn(floatSpec)
        is MotionPattern.Scale -> fadeIn(floatSpec) + scaleIn(floatSpec, initialScale = from)
    }
}

fun MotionPattern.toExit(
    spec: AnimationSpec.Finite,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ExitTransition {
    val floatSpec = spec.toComposeSpec<Float>(easings)
    val direction = if (forward) 1 else -1
    return when (this) {
        is MotionPattern.SharedAxis -> {
            val offsetSpec = spec.toComposeSpec<IntOffset>(easings)
            fadeOut(floatSpec) +
                slideOutHorizontally(offsetSpec) { width -> (-direction * width * fraction).toInt() }
        }
        MotionPattern.Fade -> fadeOut(floatSpec)
        is MotionPattern.Scale -> fadeOut(floatSpec) + scaleOut(floatSpec, targetScale = from)
    }
}

fun MotionPattern.toContentTransform(
    spec: AnimationSpec.Finite,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ContentTransform {
    return toEnter(spec, easings, forward) togetherWith toExit(spec, easings, forward)
}
