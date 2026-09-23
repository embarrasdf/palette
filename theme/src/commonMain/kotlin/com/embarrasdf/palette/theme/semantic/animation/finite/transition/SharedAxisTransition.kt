package com.embarrasdf.palette.theme.semantic.animation.finite.transition

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
 * Builds a symmetric shared-axis [ContentTransform] from a finite animation spec (Material's pattern
 * for peer navigation): the transition leans on the fade and slides only a fraction of the width
 * rather than edge-to-edge, so back mirrors forward as a slide-out without an exaggerated full-width
 * push. It returns a plain [ContentTransform], so it is usable with any `AnimatedContent` — a
 * `NavDisplay` transition slot, a tab switcher, etc. — and does not depend on any navigation library.
 *
 * @param forward chooses the slide direction; back navigation passes `forward = false` to mirror it.
 */
fun AnimationSpec.Finite.toSharedAxisTransition(
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

/**
 * Denominator applied to the full width for the shared-axis slide. A larger value means less travel;
 * the fade carries most of the transition (Material reduces motion by not sliding the full width of
 * the screen).
 */
private const val SlideDivisor = 5
