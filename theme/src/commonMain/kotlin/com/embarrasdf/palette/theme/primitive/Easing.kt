package com.embarrasdf.palette.theme.primitive

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing

enum class EasingPrimitiveToken(val default: Easing) {
    Linear(LinearEasing),
    Standard(FastOutSlowInEasing),
    Decelerate(LinearOutSlowInEasing),
    Accelerate(FastOutLinearInEasing),
    Emphasized(CubicBezierEasing(0.2f, 0f, 0f, 1f)),
}
