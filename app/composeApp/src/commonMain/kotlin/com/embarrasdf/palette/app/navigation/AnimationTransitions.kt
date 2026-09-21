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
 * Builds the enter/exit [ContentTransform] used for screen transitions from a single semantic
 * [AnimationSpec]. This is the one place the v1 choreography (a fade combined with a directional
 * slide) is defined; splitting enter and exit into separate tokens later changes only this helper.
 *
 * @param forward true for a forward navigation, false for a pop — the slide direction reverses.
 */
fun AnimationSpec.toEnterExit(
    easings: Map<EasingPrimitiveToken, Easing>,
    forward: Boolean,
): ContentTransform {
    val floatSpec = toComposeSpec<Float>(easings)
    val offsetSpec = toComposeSpec<IntOffset>(easings)
    val direction = if (forward) 1 else -1

    val enter = fadeIn(floatSpec) +
        slideInHorizontally(offsetSpec) { fullWidth -> direction * fullWidth }
    val exit = fadeOut(floatSpec) +
        slideOutHorizontally(offsetSpec) { fullWidth -> -direction * fullWidth }

    return enter togetherWith exit
}
