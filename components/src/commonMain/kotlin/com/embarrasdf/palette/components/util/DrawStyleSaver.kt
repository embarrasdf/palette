package com.embarrasdf.palette.components.util

import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke

private enum class DrawStyleType {
    Fill,
    Stroke,
}

private const val DrawStyleKey = "drawStyle"
private const val StrokeWidthKey = "strokeWidth"
private const val StrokeMiterKey = "strokeMiter"
private const val StrokeCapKey = "strokeCap"
private const val StrokeJoinKey = "strokeJoin"

// NOTE: does not save PathEffect for Stroke
val DrawStyleSaver: Saver<DrawStyle, Any> = mapSaverSafe(
    save = { value ->
        when (value) {
            Fill -> mapOf(
                DrawStyleKey to DrawStyleType.Fill,
            )
            is Stroke -> mapOf(
                DrawStyleKey to DrawStyleType.Stroke,
                StrokeWidthKey to value.width,
                StrokeMiterKey to value.miter,
                StrokeCapKey to save(value = value.cap, saver = StrokeCapSaver, scope = this),
                StrokeJoinKey to save(value = value.join, saver = StrokeJoinSaver, scope = this),
            )
        }
    },
    restore = { map ->
        when (map[DrawStyleKey] as DrawStyleType) {
            DrawStyleType.Fill -> Fill
            DrawStyleType.Stroke -> Stroke(
                width = map[StrokeWidthKey] as Float,
                miter = map[StrokeMiterKey] as Float,
                cap = map.requireRestore(key = StrokeCapKey, saver = StrokeCapSaver),
                join = map.requireRestore(key = StrokeJoinKey, saver = StrokeJoinSaver),
            )
        }
    },
)
