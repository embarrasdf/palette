package com.embarrasdf.palette.theme.component.core

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.shape.toShape
import com.embarrasdf.palette.components.core.SurfaceStyle as ComponentSurfaceStyle

data class SurfaceStyleTokenSet(
    val color: ColorToken,
    val shape: ShapeToken,
    val borderStyle: BorderStyleToken?,
)

@Composable
fun SurfaceStyleTokenSet.toComponentStyle(): ComponentSurfaceStyle = ComponentSurfaceStyle(
    shape = shape.toShape(),
    color = color.toColor(),
    borderStyle = borderStyle?.resolve(),
    indication = PaletteTheme.semantic.indication,
)
