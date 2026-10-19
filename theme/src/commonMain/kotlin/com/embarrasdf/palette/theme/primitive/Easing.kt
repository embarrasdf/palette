package com.embarrasdf.palette.theme.primitive

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme

/**
 * The named easing curves, each defined by its cubic Bézier control points so the values are
 * editable and seedable. The Material reference curves are exactly these béziers.
 */
enum class EasingPrimitiveToken(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
) {
    Linear(0f, 0f, 1f, 1f),
    Standard(0.4f, 0f, 0.2f, 1f),
    Decelerate(0f, 0f, 0.2f, 1f),
    Accelerate(0.4f, 0f, 1f, 1f),
    Emphasized(0.2f, 0f, 0f, 1f),
    ;

    val default: Easing get() = CubicBezierEasing(x1, y1, x2, y2)
}

/**
 * A selected easing curve: either a reference to a named [EasingPrimitiveToken] (themeable through
 * the primitive easing map) or an inline [CubicBezier] curve.
 */
sealed interface EasingValue {
    data class Preset(val token: EasingPrimitiveToken) : EasingValue

    data class CubicBezier(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
    ) : EasingValue
}

/** Resolves this easing value to a Compose [Easing], looking presets up in the primitive map. */
fun EasingValue.toEasing(easings: Map<EasingPrimitiveToken, Easing>): Easing = when (this) {
    is EasingValue.Preset -> easings.getValue(token)
    is EasingValue.CubicBezier -> CubicBezierEasing(x1, y1, x2, y2)
}

@Composable
fun EasingValue.toEasing(): Easing = toEasing(PaletteTheme.primitive.easing)
