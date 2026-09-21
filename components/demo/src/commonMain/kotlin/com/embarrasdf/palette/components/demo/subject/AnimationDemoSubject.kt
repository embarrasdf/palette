package com.embarrasdf.palette.components.demo.subject

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.toComposeSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Simple, illustrative subjects for observing an [AnimationSpec] in real time. Each drives a single
 * 0f..1f value with the resolved spec and applies it in a different way.
 */
enum class AnimationDemoSubject {
    /** A dot translating left to right and back. */
    Ball,

    /** A square growing and shrinking. */
    Scale,

    /** A block fading in and out — a non-spatial subject. */
    Fade,
}

private const val HoldMillis = 600L

@Composable
fun AnimationDemoSubject(
    subject: AnimationDemoSubject,
    spec: AnimationSpec,
    modifier: Modifier = Modifier,
) {
    val easings = PaletteTheme.primitive.easing
    val progress = remember { Animatable(0f) }

    // Keyed on the spec value (data-class equality), so edits restart the loop but recompositions
    // that leave the spec unchanged do not.
    LaunchedEffect(subject, spec, easings) {
        val composeSpec = spec.toComposeSpec<Float>(easings)
        progress.snapTo(0f)
        while (isActive) {
            progress.animateTo(1f, composeSpec)
            delay(HoldMillis)
            progress.animateTo(0f, composeSpec)
            delay(HoldMillis)
        }
    }

    val color = PaletteTheme.semantic.color.primary
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(PaletteTheme.semantic.color.surface)
    ) {
        val p = progress.value
        when (subject) {
            AnimationDemoSubject.Ball -> {
                val radius = size.minDimension * 0.08f
                val cx = lerp(radius, size.width - radius, p)
                drawCircle(color = color, radius = radius, center = Offset(cx, size.height / 2f))
            }

            AnimationDemoSubject.Scale -> {
                val minSide = size.minDimension * 0.12f
                val maxSide = size.minDimension * 0.6f
                val side = lerp(minSide, maxSide, p)
                drawRect(
                    color = color,
                    topLeft = Offset((size.width - side) / 2f, (size.height - side) / 2f),
                    size = Size(side, side),
                )
            }

            AnimationDemoSubject.Fade -> {
                val side = size.minDimension * 0.4f
                drawRect(
                    color = color,
                    topLeft = Offset((size.width - side) / 2f, (size.height - side) / 2f),
                    size = Size(side, side),
                    alpha = p,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        AnimationDemoSubject(
            subject = AnimationDemoSubject.Ball,
            spec = AnimationSpec.Tween(),
        )
    }
}
