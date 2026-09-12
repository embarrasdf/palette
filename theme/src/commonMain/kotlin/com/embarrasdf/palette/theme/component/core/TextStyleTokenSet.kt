package com.embarrasdf.palette.theme.component.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.typography.Typography
import com.embarrasdf.palette.theme.semantic.typography.TypographyToken
import com.embarrasdf.palette.theme.semantic.format.core.TextFormatScheme
import com.embarrasdf.palette.theme.semantic.format.core.TextFormatToken
import com.embarrasdf.palette.theme.semantic.format.core.toFormat
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.typography.toComposeTextStyle

data class TextStyleTokenSet(
    val typographyToken: TypographyToken,
    val textFormatToken: TextFormatToken,
    val color: ColorToken,
)

@Composable
fun TextStyleTokenSet.toTextStyle(): TextStyle =
    toTextStyle(
        typography = PaletteTheme.semantic.typography,
        textFormats = PaletteTheme.semantic.format.textFormats,
        color = color.toColor(),
    )

fun TextStyleTokenSet.toTextStyle(
    typography: Typography,
    textFormats: TextFormatScheme,
    color: Color,
): TextStyle {
    return TextStyle(
        composeTextStyle = typographyToken.toComposeTextStyle(typography).copy(color = color),
        format = textFormatToken.toFormat(textFormats),
    )
}
