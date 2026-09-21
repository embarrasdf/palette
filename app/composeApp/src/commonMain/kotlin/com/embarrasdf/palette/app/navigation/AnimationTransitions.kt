package com.embarrasdf.palette.app.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Easing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.toComposeSpec

/**
 * Screen transitions are built from a single semantic [AnimationSpec]. This is the one place the
 * v1 choreography lives; splitting enter/exit into separate tokens later changes only this file.
 *
 * All navigation uses a symmetric shared-axis motion (Material's pattern for peer navigation): the
 * transition leans on the fade and slides only a fraction of the width rather than edge-to-edge, so
 * back mirrors forward as a slide-out without an exaggerated full-width push. Predictive back uses
 * the same mirrored slide, driven by the gesture.
 */

/**
 * Denominator applied to the full width for the shared-axis slide. A larger value means less
 * travel; the fade carries most of the transition (Material reduces motion by not sliding the full
 * width of the screen).
 */
private const val SlideDivisor = 5

/**
 * A symmetric shared-axis transition. [forward] chooses the slide direction; back navigation passes
 * `forward = false` to mirror it as a slide-out.
 */
fun AnimationSpec.toSharedAxisTransition(
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ContentTransform {
    val floatSpec = toComposeSpec<Float>(easings)
    val offsetSpec = toComposeSpec<IntOffset>(easings)
    val direction = if (forward) 1 else -1

    val enter = fadeIn(floatSpec) +
        slideInHorizontally(offsetSpec) { fullWidth -> direction * fullWidth / SlideDivisor }
    val exit = fadeOut(floatSpec) +
        slideOutHorizontally(offsetSpec) { fullWidth -> -direction * fullWidth / SlideDivisor }

    return enter togetherWith exit
}
