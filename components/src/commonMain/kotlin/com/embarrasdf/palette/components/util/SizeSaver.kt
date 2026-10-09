package com.embarrasdf.palette.components.util

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

private const val WidthKey = "width"
private const val HeightKey = "height"

val IntSizeSaver = mapSaverSafe(
    save = { value -> mapOf(WidthKey to value.width, HeightKey to value.height) },
    restore = { map ->
        val width = map[WidthKey] as Int
        val height = map[HeightKey] as Int
        IntSize(width, height)
    },
)

val DpSizeSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            WidthKey to value.width.value,
            HeightKey to value.height.value,
        )
   },
    restore = { map ->
        DpSize(
            width = (map[WidthKey] as Float).dp,
            height = (map[HeightKey] as Float).dp,
        )
    },
)
