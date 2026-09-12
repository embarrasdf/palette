package com.embarrasdf.palette.theme.semantic

import com.embarrasdf.palette.theme.semantic.color.ColorTokens
import com.embarrasdf.palette.theme.semantic.format.Formats
import com.embarrasdf.palette.theme.semantic.format.PaletteFormats
import com.embarrasdf.palette.theme.semantic.interaction.InteractionScheme
import com.embarrasdf.palette.theme.semantic.interaction.PaletteInteractionScheme
import com.embarrasdf.palette.theme.semantic.dimension.Dimension
import com.embarrasdf.palette.theme.semantic.dimension.PaletteDimension
import com.embarrasdf.palette.theme.semantic.shape.PaletteShapeScheme
import com.embarrasdf.palette.theme.semantic.shape.ShapeScheme
import com.embarrasdf.palette.theme.semantic.typography.SemanticTypography

data class SemanticTokens(
    val colors: ColorTokens = ColorTokens(),
    val typography: SemanticTypography = SemanticTypography(),
    val shapeScheme: ShapeScheme = PaletteShapeScheme,
    val dimension: Dimension = PaletteDimension,
    val interaction: InteractionScheme = PaletteInteractionScheme,
    val formats: Formats = PaletteFormats,
)
