package com.embarrasdf.palette.theme.component.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.theme.component.LocalComponentTokens
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.format.core.TextFormatToken
import com.embarrasdf.palette.theme.semantic.typography.TypographyToken

enum class TextStyleToken(val default: TextStyleTokenSet) {
    Display(
        TextStyleTokenSet(
            typographyToken = TypographyToken.Display,
            textFormatToken = TextFormatToken.Display,
            color = ColorToken.OnSurface,
        ),
    ),
    Headline(
        TextStyleTokenSet(
            typographyToken = TypographyToken.Headline,
            textFormatToken = TextFormatToken.Headline,
            color = ColorToken.OnSurface,
        ),
    ),
    TitleLarge(
        TextStyleTokenSet(
            typographyToken = TypographyToken.TitleLarge,
            textFormatToken = TextFormatToken.Title,
            color = ColorToken.OnSurface,
        ),
    ),
    TitleMedium(
        TextStyleTokenSet(
            typographyToken = TypographyToken.TitleMedium,
            textFormatToken = TextFormatToken.Title,
            color = ColorToken.OnSurface,
        ),
    ),
    TitleSmall(
        TextStyleTokenSet(
            typographyToken = TypographyToken.TitleSmall,
            textFormatToken = TextFormatToken.Title,
            color = ColorToken.OnSurface,
        ),
    ),
    BodyLarge(
        TextStyleTokenSet(
            typographyToken = TypographyToken.BodyLarge,
            textFormatToken = TextFormatToken.Body,
            color = ColorToken.OnSurface,
        ),
    ),
    BodyMedium(
        TextStyleTokenSet(
            typographyToken = TypographyToken.BodyMedium,
            textFormatToken = TextFormatToken.Body,
            color = ColorToken.OnSurface,
        ),
    ),
    BodySmall(
        TextStyleTokenSet(
            typographyToken = TypographyToken.BodySmall,
            textFormatToken = TextFormatToken.Body,
            color = ColorToken.OnSurface,
        ),
    ),
    LabelLarge(
        TextStyleTokenSet(
            typographyToken = TypographyToken.LabelLarge,
            textFormatToken = TextFormatToken.Label,
            color = ColorToken.OnSurface,
        ),
    ),
    LabelMedium(
        TextStyleTokenSet(
            typographyToken = TypographyToken.LabelMedium,
            textFormatToken = TextFormatToken.Label,
            color = ColorToken.OnSurface,
        ),
    ),
    LabelSmall(
        TextStyleTokenSet(
            typographyToken = TypographyToken.LabelSmall,
            textFormatToken = TextFormatToken.Label,
            color = ColorToken.OnSurface,
        ),
    ),
}

@Composable
@ReadOnlyComposable
fun TextStyleToken.tokenSet(): TextStyleTokenSet = LocalComponentTokens.current.text.getValue(this)

@Composable
fun TextStyleToken.resolve(): TextStyle = tokenSet().toTextStyle()
