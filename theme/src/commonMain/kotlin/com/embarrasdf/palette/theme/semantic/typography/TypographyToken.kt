package com.embarrasdf.palette.theme.semantic.typography

import com.embarrasdf.palette.theme.PaletteTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle as ComposeTextStyle
import androidx.compose.ui.unit.sp
import com.embarrasdf.palette.theme.primitive.FontFamily
import com.embarrasdf.palette.theme.primitive.FontStyle
import com.embarrasdf.palette.theme.primitive.FontWeight

enum class TypographyToken(val default: TypographyTokenSet) {
    Display(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.2).sp,
        )
    ),
    Headline(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
        )
    ),
    TitleLarge(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        )
    ),
    TitleMedium(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.2.sp,
        )
    ),
    TitleSmall(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        )
    ),
    BodyLarge(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        )
    ),
    BodyMedium(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp,
        )
    ),
    BodySmall(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
        )
    ),
    LabelLarge(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        )
    ),
    LabelMedium(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )
    ),
    LabelSmall(
        TypographyTokenSet(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )
    ),
}

@Composable
fun TypographyToken.toComposeTextStyle(): ComposeTextStyle {
    return this.toComposeTextStyle(PaletteTheme.semantic.typography)
}

fun TypographyToken.toComposeTextStyle(typography: Typography): ComposeTextStyle {
    return when (this) {
        TypographyToken.Display -> typography.display
        TypographyToken.Headline -> typography.headline
        TypographyToken.TitleLarge -> typography.titleLarge
        TypographyToken.TitleMedium -> typography.titleMedium
        TypographyToken.TitleSmall -> typography.titleSmall
        TypographyToken.BodyLarge -> typography.bodyLarge
        TypographyToken.BodyMedium -> typography.bodyMedium
        TypographyToken.BodySmall -> typography.bodySmall
        TypographyToken.LabelLarge -> typography.labelLarge
        TypographyToken.LabelMedium -> typography.labelMedium
        TypographyToken.LabelSmall -> typography.labelSmall
    }
}
