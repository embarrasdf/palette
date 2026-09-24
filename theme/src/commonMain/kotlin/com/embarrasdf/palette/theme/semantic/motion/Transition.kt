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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.semantic.animation.AnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationToken
import com.embarrasdf.palette.theme.semantic.animation.toComposeSpec
import com.embarrasdf.palette.theme.semantic.animation.toSpec

/** The axis a [Transition.SharedAxis] slides along. */
enum class Axis {
    X,
    Y,
}

/**
 * How an element appears and disappears. Each variant composes property animations (fade, slide,
 * scale) timed by the value-animation preset it references through [AnimationToken], and resolves to
 * Compose [EnterTransition]/[ExitTransition]/[ContentTransform] for use with any `AnimatedVisibility`,
 * `AnimatedContent`, or navigation host.
 */
sealed interface Transition {
    data class SharedAxis(
        val axis: Axis = Axis.X,
        val fraction: Float = 0.2f,
        val animation: AnimationToken = AnimationToken.Default,
    ) : Transition

    data class Fade(
        val animation: AnimationToken = AnimationToken.Default,
    ) : Transition

    data class Scale(
        val from: Float = 0.9f,
        val animation: AnimationToken = AnimationToken.Default,
    ) : Transition

    data object None : Transition
}

enum class TransitionType {
    SharedAxis,
    Fade,
    Scale,
    None,
}

fun Transition.type(): TransitionType = when (this) {
    is Transition.SharedAxis -> TransitionType.SharedAxis
    is Transition.Fade -> TransitionType.Fade
    is Transition.Scale -> TransitionType.Scale
    Transition.None -> TransitionType.None
}

fun Transition.toEnter(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): EnterTransition {
    val direction = if (forward) 1 else -1
    return when (this) {
        is Transition.SharedAxis -> {
            val spec = animation.toSpec(animationScheme)
            val floatSpec = spec.toComposeSpec<Float>(easings)
            val offsetSpec = spec.toComposeSpec<IntOffset>(easings)
            val slide = when (axis) {
                Axis.X -> slideInHorizontally(offsetSpec) { size -> (direction * size * fraction).toInt() }
                Axis.Y -> slideInVertically(offsetSpec) { size -> (direction * size * fraction).toInt() }
            }
            fadeIn(floatSpec) + slide
        }
        is Transition.Fade -> fadeIn(animation.toSpec(animationScheme).toComposeSpec<Float>(easings))
        is Transition.Scale -> {
            val floatSpec = animation.toSpec(animationScheme).toComposeSpec<Float>(easings)
            fadeIn(floatSpec) + scaleIn(floatSpec, initialScale = from)
        }
        Transition.None -> EnterTransition.None
    }
}

fun Transition.toExit(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ExitTransition {
    val direction = if (forward) 1 else -1
    return when (this) {
        is Transition.SharedAxis -> {
            val spec = animation.toSpec(animationScheme)
            val floatSpec = spec.toComposeSpec<Float>(easings)
            val offsetSpec = spec.toComposeSpec<IntOffset>(easings)
            val slide = when (axis) {
                Axis.X -> slideOutHorizontally(offsetSpec) { size -> (-direction * size * fraction).toInt() }
                Axis.Y -> slideOutVertically(offsetSpec) { size -> (-direction * size * fraction).toInt() }
            }
            fadeOut(floatSpec) + slide
        }
        is Transition.Fade -> fadeOut(animation.toSpec(animationScheme).toComposeSpec<Float>(easings))
        is Transition.Scale -> {
            val floatSpec = animation.toSpec(animationScheme).toComposeSpec<Float>(easings)
            fadeOut(floatSpec) + scaleOut(floatSpec, targetScale = from)
        }
        Transition.None -> ExitTransition.None
    }
}

fun Transition.toContentTransform(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ContentTransform {
    return toEnter(animationScheme, easings, forward) togetherWith toExit(animationScheme, easings, forward)
}

@Composable
fun Transition.toContentTransform(forward: Boolean): ContentTransform {
    return toContentTransform(
        animationScheme = PaletteTheme.semantic.animation,
        easings = PaletteTheme.primitive.easing,
        forward = forward,
    )
}
