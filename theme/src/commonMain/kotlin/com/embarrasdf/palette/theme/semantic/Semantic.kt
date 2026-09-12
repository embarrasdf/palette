package com.embarrasdf.palette.theme.semantic

import androidx.compose.foundation.Indication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.embarrasdf.palette.theme.primitive.LocalPrimitiveTokens
import com.embarrasdf.palette.theme.semantic.color.ColorScheme
import com.embarrasdf.palette.theme.semantic.format.Formats
import com.embarrasdf.palette.theme.semantic.interaction.IndicationToken
import com.embarrasdf.palette.theme.semantic.interaction.InteractionScheme
import com.embarrasdf.palette.theme.semantic.interaction.toIndication
import com.embarrasdf.palette.theme.semantic.dimension.Dimension
import com.embarrasdf.palette.theme.semantic.shape.ShapeScheme
import com.embarrasdf.palette.theme.semantic.typography.Typography
import com.embarrasdf.palette.theme.semantic.typography.resolve

object Semantic {
    val color: ColorScheme
        @Composable
        get() {
            val colors = LocalSemanticTokens.current.colors
            return if (LocalIsDarkMode.current) colors.dark else colors.light
        }

    val typography: Typography
        @Composable
        get() {
            val primitive = LocalPrimitiveTokens.current
            val semantic = LocalSemanticTokens.current
            return remember(
                primitive.fontFamily,
                primitive.fontWeight,
                primitive.fontStyle,
                semantic.typography,
            ) {
                semantic.typography.resolve(primitive)
            }
        }

    val shape: ShapeScheme
        @Composable
        get() = LocalSemanticTokens.current.shapeScheme

    val dimension: Dimension
        @Composable
        get() = LocalSemanticTokens.current.dimension

    val interaction: InteractionScheme
        @Composable
        get() = LocalSemanticTokens.current.interaction

    val indication: Indication
        @Composable
        get() = IndicationToken.Default.toIndication()

    val format: Formats
        @Composable
        get() = LocalSemanticTokens.current.formats
}

val LocalSemanticTokens = staticCompositionLocalOf { SemanticTokens() }

val LocalIsDarkMode = staticCompositionLocalOf { false }
