package com.embarrasdf.palette.theme.semantic.typography

import androidx.compose.ui.text.TextStyle as ComposeTextStyle
import androidx.compose.ui.unit.TextUnit
import com.embarrasdf.palette.theme.primitive.FontFamily
import com.embarrasdf.palette.theme.primitive.FontStyle
import com.embarrasdf.palette.theme.primitive.FontWeight
import com.embarrasdf.palette.theme.primitive.PrimitiveTokens

data class TypographyTokenSet(
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    val fontStyle: FontStyle,
    val fontSize: TextUnit,
    val lineHeight: TextUnit,
    val letterSpacing: TextUnit,
)

fun TypographyTokenSet.toComposeTextStyle(primitiveTokens: PrimitiveTokens): ComposeTextStyle =
    ComposeTextStyle(
        fontFamily = primitiveTokens.fontFamily.getValue(fontFamily),
        fontWeight = primitiveTokens.fontWeight.getValue(fontWeight),
        fontStyle = primitiveTokens.fontStyle.getValue(fontStyle),
        fontSize = fontSize,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
    )
