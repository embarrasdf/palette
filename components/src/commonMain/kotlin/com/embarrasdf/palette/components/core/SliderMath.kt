package com.embarrasdf.palette.components.core

import androidx.compose.ui.util.lerp
import kotlin.math.abs
import kotlin.math.roundToInt

fun stepsForIncrement(
    valueRange: ClosedFloatingPointRange<Float>,
    stepIncrement: Float,
): Int {
    val rangeSize = valueRange.endInclusive - valueRange.start
    return ((rangeSize / stepIncrement).roundToInt() - 1).coerceAtLeast(0)
}

internal fun stepsToTickFractions(steps: Int): FloatArray =
    if (steps == 0) floatArrayOf() else FloatArray(steps + 2) { it.toFloat() / (steps + 1) }

internal fun snapValuesToTickFractions(
    snapValues: List<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
): FloatArray {
    val rangeSize = valueRange.endInclusive - valueRange.start
    return snapValues
        .filter { it in valueRange }
        .map { (it - valueRange.start) / rangeSize }
        .sorted()
        .toFloatArray()
}

internal fun snapValueToTick(
    current: Float,
    tickFractions: FloatArray,
    valueRange: ClosedFloatingPointRange<Float>,
): Float {
    if (tickFractions.isEmpty()) return current
    return tickFractions
        .minByOrNull { abs(lerp(valueRange.start, valueRange.endInclusive, it) - current) }
        ?.run { lerp(valueRange.start, valueRange.endInclusive, this) }
        ?: current
}

// Scale x1 from a1..b1 range to a2..b2 range
internal fun scale(
    a1: Float,
    b1: Float,
    x1: Float,
    a2: Float,
    b2: Float,
) = lerp(a2, b2, calcFraction(a = a1, b = b1, pos = x1))

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
internal fun calcFraction(
    a: Float,
    b: Float,
    pos: Float,
) = (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)
