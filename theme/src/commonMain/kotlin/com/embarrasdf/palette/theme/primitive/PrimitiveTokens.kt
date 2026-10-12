package com.embarrasdf.palette.theme.primitive

import androidx.compose.runtime.staticCompositionLocalOf
import com.embarrasdf.palette.components.core.Shape
import androidx.compose.ui.text.font.FontFamily as ComposeFontFamily
import androidx.compose.ui.text.font.FontStyle as ComposeFontStyle
import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight

data class PrimitiveTokens(
    val fontFamily: Map<FontFamily, ComposeFontFamily> =
        FontFamily.entries.associateWith { it.toComposeFontFamily() },
    val fontWeight: Map<FontWeight, ComposeFontWeight> =
        FontWeight.entries.associateWith { it.toComposeFontWeight() },
    val fontStyle: Map<FontStyle, ComposeFontStyle> =
        FontStyle.entries.associateWith { it.toComposeFontStyle() },
    val shape: Map<ShapePrimitiveToken, Shape> =
        ShapePrimitiveToken.entries.associateWith { it.default },
    val indication: Map<IndicationPrimitiveToken, IndicationTokenSet> =
        IndicationPrimitiveToken.entries.associateWith { it.default },
)

// Part of the theme's public API: PaletteTheme provides it and components read it.
@Suppress("CompositionLocalAllowlist")
val LocalPrimitiveTokens = staticCompositionLocalOf { PrimitiveTokens() }
