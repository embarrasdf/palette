package com.embarrasdf.palette.theme.component.core

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.dimension.PaddingValuesTokenSet
import com.embarrasdf.palette.theme.semantic.dimension.PalettePaddingScheme
import com.embarrasdf.palette.theme.semantic.dimension.toPaddingValues
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.shape.toShape
import com.embarrasdf.palette.components.core.ButtonStyle as ComponentButtonStyle

data class ButtonStyleTokenSet(
    val containerColor: ColorToken,
    val shape: ShapeToken,
    val borderStyle: BorderStyleToken?,
    val contentPadding: PaddingValuesTokenSet = PalettePaddingScheme.wide,
)

@Composable
fun ButtonStyleTokenSet.toComponentStyle(): ComponentButtonStyle = ComponentButtonStyle(
    containerColor = containerColor.toColor(),
    shape = shape.toShape(),
    borderStyle = borderStyle?.resolve(),
    disabledContentAlpha = PaletteTheme.semantic.color.disabledContentAlpha,
    disabledContainerAlpha = PaletteTheme.semantic.color.disabledContainerAlpha,
    contentPadding = contentPadding.toPaddingValues(),
    indication = PaletteTheme.semantic.indication,
)
