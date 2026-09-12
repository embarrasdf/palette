package com.embarrasdf.palette.theme.primitive

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily as ComposeFontFamily
import androidx.compose.ui.text.font.FontStyle as ComposeFontStyle
import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight
import com.embarrasdf.palette.components.core.Shape

object Primitive {
    val fontFamily: Map<FontFamily, ComposeFontFamily>
        @Composable
        get() = LocalPrimitiveTokens.current.fontFamily

    val fontWeight: Map<FontWeight, ComposeFontWeight>
        @Composable
        get() = LocalPrimitiveTokens.current.fontWeight

    val fontStyle: Map<FontStyle, ComposeFontStyle>
        @Composable
        get() = LocalPrimitiveTokens.current.fontStyle

    val shape: Map<ShapePrimitiveToken, Shape>
        @Composable
        get() = LocalPrimitiveTokens.current.shape

    val indication: Map<IndicationPrimitiveToken, IndicationTokenSet>
        @Composable
        get() = LocalPrimitiveTokens.current.indication
}
