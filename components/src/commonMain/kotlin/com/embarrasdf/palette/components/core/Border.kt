package com.embarrasdf.palette.components.core

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class BorderStyle(
    val width: Dp = Dp.Hairline,
    val color: Color = Color.Unspecified,
    val shape: Shape = Shape.Rectangle(),
)

fun Modifier.border(
    style: BorderStyle,
): Modifier = this.border(
    width = style.width,
    color = style.color,
    shape = style.shape.toComposeShape(),
)
