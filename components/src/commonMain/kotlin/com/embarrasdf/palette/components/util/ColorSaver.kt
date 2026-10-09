package com.embarrasdf.palette.components.util

import androidx.compose.ui.graphics.Color

private const val RedKey = "red"
private const val GreenKey = "green"
private const val BlueKey = "blue"
private const val AlphaKey = "alpha"

val ColorSaver = mapSaverSafe(
    save = { color ->
        mapOf(
            RedKey to color.red,
            GreenKey to color.green,
            BlueKey to color.blue,
            AlphaKey to color.alpha,
        )
    },
    restore = { map ->
        Color(
            red = map[RedKey] as Float,
            green = map[GreenKey] as Float,
            blue = map[BlueKey] as Float,
            alpha = map[AlphaKey] as Float,
        )
    },
)
