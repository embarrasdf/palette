package com.embarrasdf.palette.theme.semantic.motion

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.semantic.animation.AnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationToken
import com.embarrasdf.palette.theme.semantic.animation.toSpec

/**
 * A single enter/exit choreography: a [MotionPattern] (what moves) timed by a value-animation preset
 * referenced through [animation] (how it's timed). Resolving pulls the referenced spec from the
 * [AnimationScheme], mirroring how shape tokens reference the primitive layer.
 */
data class Transition(
    val pattern: MotionPattern,
    val animation: AnimationToken = AnimationToken.Default,
)

fun Transition.toEnter(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): EnterTransition = pattern.toEnter(animation.toSpec(animationScheme), easings, forward)

fun Transition.toExit(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ExitTransition = pattern.toExit(animation.toSpec(animationScheme), easings, forward)

fun Transition.toContentTransform(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ContentTransform = pattern.toContentTransform(animation.toSpec(animationScheme), easings, forward)

@Composable
fun Transition.toContentTransform(forward: Boolean): ContentTransform {
    return toContentTransform(
        animationScheme = PaletteTheme.semantic.animation,
        easings = PaletteTheme.primitive.easing,
        forward = forward,
    )
}
