package com.embarrasdf.palette.components.util

import kotlin.math.PI

private const val DegreesPerHalfTurn = 180.0

fun Int.toRadians(): Double {
    return this * (PI / DegreesPerHalfTurn)
}

fun Float.toRadians(): Double {
    return this * (PI / DegreesPerHalfTurn)
}
