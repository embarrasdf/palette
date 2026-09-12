package com.embarrasdf.palette.theme.component.core

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.core.TextStyle

object TextStyles {
    val display: TextStyle @Composable get() = TextStyleToken.Display.resolve()
    val headline: TextStyle @Composable get() = TextStyleToken.Headline.resolve()
    val titleLarge: TextStyle @Composable get() = TextStyleToken.TitleLarge.resolve()
    val titleMedium: TextStyle @Composable get() = TextStyleToken.TitleMedium.resolve()
    val titleSmall: TextStyle @Composable get() = TextStyleToken.TitleSmall.resolve()
    val bodyLarge: TextStyle @Composable get() = TextStyleToken.BodyLarge.resolve()
    val bodyMedium: TextStyle @Composable get() = TextStyleToken.BodyMedium.resolve()
    val bodySmall: TextStyle @Composable get() = TextStyleToken.BodySmall.resolve()
    val labelLarge: TextStyle @Composable get() = TextStyleToken.LabelLarge.resolve()
    val labelMedium: TextStyle @Composable get() = TextStyleToken.LabelMedium.resolve()
    val labelSmall: TextStyle @Composable get() = TextStyleToken.LabelSmall.resolve()

    @Composable
    operator fun get(token: TextStyleToken): TextStyle = token.resolve()
}
