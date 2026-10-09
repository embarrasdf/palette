package com.embarrasdf.palette.theme.primitive

import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Shape

enum class ShapePrimitiveToken(val default: Shape) {
    Circle(Shape.Circle),
    Rectangle(Shape.Rectangle()),
    RoundRect(Shape.Rectangle(cornerRadius = 64.dp)),
    Triangle(Shape.Triangle),
    Diamond(Shape.Diamond),
}
