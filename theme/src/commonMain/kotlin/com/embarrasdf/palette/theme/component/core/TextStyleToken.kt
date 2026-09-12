package com.embarrasdf.palette.theme.component.core

import com.embarrasdf.palette.theme.component.LocalComponentTokens

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.typography.TypographyToken
import com.embarrasdf.palette.theme.semantic.format.core.TextFormatToken

enum class TextStyleToken(val default: TextStyleTokenSet) {
    Display(TextStyleTokenSet(TypographyToken.Display, TextFormatToken.Display, ColorToken.OnSurface)),
    Headline(TextStyleTokenSet(TypographyToken.Headline, TextFormatToken.Headline, ColorToken.OnSurface)),
    TitleLarge(TextStyleTokenSet(TypographyToken.TitleLarge, TextFormatToken.Title, ColorToken.OnSurface)),
    TitleMedium(TextStyleTokenSet(TypographyToken.TitleMedium, TextFormatToken.Title, ColorToken.OnSurface)),
    TitleSmall(TextStyleTokenSet(TypographyToken.TitleSmall, TextFormatToken.Title, ColorToken.OnSurface)),
    BodyLarge(TextStyleTokenSet(TypographyToken.BodyLarge, TextFormatToken.Body, ColorToken.OnSurface)),
    BodyMedium(TextStyleTokenSet(TypographyToken.BodyMedium, TextFormatToken.Body, ColorToken.OnSurface)),
    BodySmall(TextStyleTokenSet(TypographyToken.BodySmall, TextFormatToken.Body, ColorToken.OnSurface)),
    LabelLarge(TextStyleTokenSet(TypographyToken.LabelLarge, TextFormatToken.Label, ColorToken.OnSurface)),
    LabelMedium(TextStyleTokenSet(TypographyToken.LabelMedium, TextFormatToken.Label, ColorToken.OnSurface)),
    LabelSmall(TextStyleTokenSet(TypographyToken.LabelSmall, TextFormatToken.Label, ColorToken.OnSurface)),
}

@Composable
fun TextStyleToken.tokenSet(): TextStyleTokenSet =
    LocalComponentTokens.current.text.getValue(this)

@Composable
fun TextStyleToken.resolve(): TextStyle =
    tokenSet().toTextStyle()
