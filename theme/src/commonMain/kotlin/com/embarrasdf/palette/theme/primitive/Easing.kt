package com.embarrasdf.palette.theme.primitive

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme

/**
 * A normalized easing curve, defined by its Bézier control points with the endpoints fixed at
 * (0, 0) and (1, 1). [Cubic] has two control points; [Quadratic] has one.
 */
sealed interface EasingCurve {
    data class Cubic(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
    ) : EasingCurve

    data class Quadratic(
        val x1: Float,
        val y1: Float,
    ) : EasingCurve
}

private const val ONE_THIRD = 1f / 3f
private const val TWO_THIRDS = 2f / 3f

/**
 * The cubic Bézier equal to this quadratic one: its control points sit two-thirds of the way from
 * each fixed endpoint toward the quadratic's single control point.
 */
fun EasingCurve.Quadratic.toCubic(): EasingCurve.Cubic = EasingCurve.Cubic(
    x1 = TWO_THIRDS * x1,
    y1 = TWO_THIRDS * y1,
    x2 = ONE_THIRD + TWO_THIRDS * x1,
    y2 = ONE_THIRD + TWO_THIRDS * y1,
)

fun EasingCurve.toEasing(): Easing = when (this) {
    is EasingCurve.Cubic -> CubicBezierEasing(x1, y1, x2, y2)
    is EasingCurve.Quadratic -> toCubic().toEasing()
}

/**
 * The named easing curves. Each is an [EasingCurve] so its control points are readable and
 * editable; the Material reference curves are exactly these cubic béziers.
 */
enum class EasingPrimitiveToken(val defaultCurve: EasingCurve) {
    Linear(EasingCurve.Cubic(0f, 0f, 1f, 1f)),
    Standard(EasingCurve.Cubic(0.4f, 0f, 0.2f, 1f)),
    Decelerate(EasingCurve.Cubic(0f, 0f, 0.2f, 1f)),
    Accelerate(EasingCurve.Cubic(0.4f, 0f, 1f, 1f)),
    Emphasized(EasingCurve.Cubic(0.2f, 0f, 0f, 1f)),
}

/**
 * A selected easing: either a reference to a named [EasingPrimitiveToken] (themeable through the
 * primitive easing map) or an inline custom [EasingCurve].
 */
sealed interface EasingValue {
    data class Preset(val token: EasingPrimitiveToken) : EasingValue

    data class Custom(val curve: EasingCurve) : EasingValue
}

/** Resolves this easing value to a Compose [Easing], looking presets up in the primitive map. */
fun EasingValue.toEasing(curves: Map<EasingPrimitiveToken, EasingCurve>): Easing = when (this) {
    is EasingValue.Preset -> curves.getValue(token).toEasing()
    is EasingValue.Custom -> curve.toEasing()
}

@Composable
fun EasingValue.toEasing(): Easing = toEasing(PaletteTheme.primitive.easing)
