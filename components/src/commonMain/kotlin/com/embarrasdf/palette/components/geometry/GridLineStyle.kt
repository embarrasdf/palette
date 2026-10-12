package com.embarrasdf.palette.components.geometry

import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.embarrasdf.palette.components.util.ColorSaver
import com.embarrasdf.palette.components.util.DrawStyleSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save

data class GridLineStyle(
    val color: Color,
    val stroke: Stroke,
)

private const val ColorKey = "color"
private const val StrokeKey = "stroke"

val GridLineStyleSaver: Saver<GridLineStyle, Any> = mapSaverSafe(
    save = { value ->
        mapOf(
            ColorKey to save(value = value.color, saver = ColorSaver, scope = this),
            StrokeKey to save(value = value.stroke, saver = DrawStyleSaver, scope = this),
        )
    },
    restore = { map ->
        GridLineStyle(
            color = requireNotNull(restore(value = map[ColorKey], saver = ColorSaver)) { "Missing color" },
            stroke = requireNotNull(restore(value = map[StrokeKey], saver = DrawStyleSaver)) { "Missing stroke" },
        )
    },
)
