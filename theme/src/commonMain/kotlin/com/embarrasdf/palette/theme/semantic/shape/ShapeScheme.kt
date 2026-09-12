package com.embarrasdf.palette.theme.semantic.shape

import com.embarrasdf.palette.theme.primitive.ShapePrimitiveToken

data class ShapeScheme(
    val primary: ShapePrimitiveToken,
    val secondary: ShapePrimitiveToken,
    val tertiary: ShapePrimitiveToken,
    val surface: ShapePrimitiveToken,
)

val PaletteShapeScheme = ShapeScheme(
    primary = ShapePrimitiveToken.Circle,
    secondary = ShapePrimitiveToken.RoundRect,
    tertiary = ShapePrimitiveToken.Rectangle,
    surface = ShapePrimitiveToken.Rectangle,
)

fun ShapeScheme.copy(
    token: ShapeToken,
    shapePrimitiveToken: ShapePrimitiveToken,
) = this.copy(
    primary = if (token == ShapeToken.Primary) shapePrimitiveToken else this.primary,
    secondary = if (token == ShapeToken.Secondary) shapePrimitiveToken else this.secondary,
    tertiary = if (token == ShapeToken.Tertiary) shapePrimitiveToken else this.tertiary,
    surface = if (token == ShapeToken.Surface) shapePrimitiveToken else this.surface,
)
