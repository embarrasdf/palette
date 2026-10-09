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

/** The axis a [TransitionEffect.Translate] moves along. */
enum class Axis {
    X,
    Y,
}

/**
 * A single property animation. Each effect references the value-animation preset that times it, so
 * effects composed into one [Transition] can run at independent speeds.
 */
sealed interface TransitionEffect {
    val animation: AnimationToken

    data class Fade(
        override val animation: AnimationToken = AnimationToken.Default,
    ) : TransitionEffect

    data class Scale(
        val from: Float = 0.9f,
        val to: Float = 0.9f,
        override val animation: AnimationToken = AnimationToken.Default,
    ) : TransitionEffect

    data class Translate(
        val axis: Axis = Axis.X,
        val fraction: Float = 0.2f,
        override val animation: AnimationToken = AnimationToken.Default,
    ) : TransitionEffect
}

enum class TransitionEffectType {
    Fade,
    Scale,
    Translate,
}

fun TransitionEffect.type(): TransitionEffectType = when (this) {
    is TransitionEffect.Fade -> TransitionEffectType.Fade
    is TransitionEffect.Scale -> TransitionEffectType.Scale
    is TransitionEffect.Translate -> TransitionEffectType.Translate
}

/** An ordered list of [TransitionEffect]s composited into one enter/exit animation. */
typealias Transition = List<TransitionEffect>

fun Transition.toEnter(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): EnterTransition = fold(EnterTransition.None) { acc, effect ->
    acc + effect.toEnter(animationScheme, easings, forward)
}

fun Transition.toExit(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ExitTransition = fold(ExitTransition.None) { acc, effect ->
    acc + effect.toExit(animationScheme, easings, forward)
}

fun Transition.toContentTransform(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ContentTransform =
    toEnter(animationScheme, easings, forward) togetherWith toExit(animationScheme, easings, forward)

@Composable
fun Transition.toContentTransform(forward: Boolean): ContentTransform = toContentTransform(
    animationScheme = PaletteTheme.semantic.animation,
    easings = PaletteTheme.primitive.easing,
    forward = forward,
)

private fun TransitionEffect.toEnter(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): EnterTransition {
    val spec = animation.toSpec(animationScheme)
    val floatSpec = spec.toComposeSpec<Float>(easings)
    return when (this) {
        is TransitionEffect.Fade -> fadeIn(floatSpec)
        is TransitionEffect.Scale -> scaleIn(floatSpec, initialScale = from)
        is TransitionEffect.Translate -> {
            val offsetSpec = spec.toComposeSpec<IntOffset>(easings)
            val direction = if (forward) 1 else -1
            when (axis) {
                Axis.X -> slideInHorizontally(offsetSpec) { size -> (direction * size * fraction).toInt() }
                Axis.Y -> slideInVertically(offsetSpec) { size -> (direction * size * fraction).toInt() }
            }
        }
    }
}

private fun TransitionEffect.toExit(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ExitTransition {
    val spec = animation.toSpec(animationScheme)
    val floatSpec = spec.toComposeSpec<Float>(easings)
    return when (this) {
        is TransitionEffect.Fade -> fadeOut(floatSpec)
        is TransitionEffect.Scale -> scaleOut(floatSpec, targetScale = to)
        is TransitionEffect.Translate -> {
            val offsetSpec = spec.toComposeSpec<IntOffset>(easings)
            val direction = if (forward) 1 else -1
            when (axis) {
                Axis.X -> slideOutHorizontally(offsetSpec) { size -> (-direction * size * fraction).toInt() }
                Axis.Y -> slideOutVertically(offsetSpec) { size -> (-direction * size * fraction).toInt() }
            }
        }
    }
}
