package com.embarrasdf.palette.theme.primitive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.embarrasdf.palette.components.core.Shape
import androidx.compose.ui.text.font.FontFamily as ComposeFontFamily
import androidx.compose.ui.text.font.FontStyle as ComposeFontStyle
import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight

object Primitive {
    val fontFamily: Map<FontFamily, ComposeFontFamily>
        @Composable
        @ReadOnlyComposable
        get() = LocalPrimitiveTokens.current.fontFamily

    val fontWeight: Map<FontWeight, ComposeFontWeight>
        @Composable
        @ReadOnlyComposable
        get() = LocalPrimitiveTokens.current.fontWeight

    val fontStyle: Map<FontStyle, ComposeFontStyle>
        @Composable
        @ReadOnlyComposable
        get() = LocalPrimitiveTokens.current.fontStyle

    val shape: Map<ShapePrimitiveToken, Shape>
        @Composable
        @ReadOnlyComposable
        get() = LocalPrimitiveTokens.current.shape

    val indication: Map<IndicationPrimitiveToken, IndicationTokenSet>
        @Composable
        @ReadOnlyComposable
        get() = LocalPrimitiveTokens.current.indication

    val easing: Map<EasingPrimitiveToken, EasingCurve>
        @Composable
        get() = LocalPrimitiveTokens.current.easing
}
