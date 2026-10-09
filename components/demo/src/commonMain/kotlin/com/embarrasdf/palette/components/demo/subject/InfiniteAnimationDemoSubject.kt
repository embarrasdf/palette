package com.embarrasdf.palette.components.demo.subject

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.toComposeSpec

/**
 * An [AnimationDemoSubject] driven continuously by an [AnimationSpec.Infinite], for observing a
 * looping value animation in real time.
 */
@Composable
fun InfiniteAnimationDemoSubject(
    subject: AnimationDemoSubject,
    spec: AnimationSpec.Infinite,
    modifier: Modifier = Modifier,
) {
    val easings = PaletteTheme.primitive.easing
    // Key on the spec value so edits restart the transition; InfiniteTransition.animateFloat
    // otherwise ignores later animationSpec changes.
    val progress = key(spec) {
        val transition = rememberInfiniteTransition(label = "InfiniteAnimationDemo")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = spec.toComposeSpec<Float>(easings),
            label = "progress",
        )
    }

    val color = PaletteTheme.semantic.color.primary
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(PaletteTheme.semantic.color.surface)
    ) {
        drawAnimationSubject(subject, progress.value, color)
    }
}
