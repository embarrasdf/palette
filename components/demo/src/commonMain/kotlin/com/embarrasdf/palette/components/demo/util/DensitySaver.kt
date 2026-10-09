package com.embarrasdf.palette.components.demo.util

import androidx.compose.ui.unit.Density
import com.embarrasdf.palette.components.util.mapSaverSafe

private const val DensityKey = "density"
private const val FontScaleKey = "fontScale"

val DensitySaver = mapSaverSafe(
    save = { value ->
        mapOf(
            DensityKey to value.density,
            FontScaleKey to value.fontScale,
        )
    },
    restore = { map ->
        Density(
            density = map[DensityKey] as Float,
            fontScale = map[FontScaleKey] as Float,
        )
    },
)
