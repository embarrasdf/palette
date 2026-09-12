package com.embarrasdf.palette.theme.component.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.shape.toShape
import com.embarrasdf.palette.components.core.BorderStyle as ComponentBorderStyle

data class BorderStyleTokenSet(
    val width: Dp = Dp.Hairline,
    val color: ColorToken = ColorToken.Outline,
    val shape: ShapeToken = ShapeToken.Surface,
)

@Composable
fun BorderStyleTokenSet.toComponentStyle(): ComponentBorderStyle = ComponentBorderStyle(
    width = this.width,
    color = this.color.toColor(),
    shape = this.shape.toShape(),
)
