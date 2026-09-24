package com.embarrasdf.palette.theme.semantic

import com.embarrasdf.palette.theme.semantic.animation.AnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.PaletteAnimationScheme
import com.embarrasdf.palette.theme.semantic.motion.MotionScheme
import com.embarrasdf.palette.theme.semantic.motion.PaletteMotionScheme
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
    val animation: AnimationScheme = PaletteAnimationScheme,
    val colors: ColorTokens = ColorTokens(),
    val dimension: Dimension = PaletteDimension,
    val formats: Formats = PaletteFormats,
    val interaction: InteractionScheme = PaletteInteractionScheme,
    val motion: MotionScheme = PaletteMotionScheme,
    val shapeScheme: ShapeScheme = PaletteShapeScheme,
    val typography: SemanticTypography = SemanticTypography(),
)
