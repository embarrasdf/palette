package com.embarrasdf.palette.theme.semantic.format.core

import com.embarrasdf.palette.formats.core.TextFormat

data class TextFormatScheme(
    val body: TextFormat,
    val display: TextFormat,
    val headline: TextFormat,
    val label: TextFormat,
    val title: TextFormat,
)

fun TextFormatScheme.update(
    token: TextFormatToken,
    value: TextFormat,
) = this.copy(
    body = if (token == TextFormatToken.Body) value else this.body,
    display = if (token == TextFormatToken.Display) value else this.display,
    headline = if (token == TextFormatToken.Headline) value else this.headline,
    label = if (token == TextFormatToken.Label) value else this.label,
    title = if (token == TextFormatToken.Title) value else this.title,
)

val PaletteTextFormatScheme = TextFormatScheme(
    body = TextFormatToken.Body.toFormat(),
    display = TextFormatToken.Display.toFormat(),
    headline = TextFormatToken.Headline.toFormat(),
    label = TextFormatToken.Label.toFormat(),
    title = TextFormatToken.Title.toFormat(),
)
