package com.embarrasdf.palette.theme.primitive

import androidx.compose.ui.text.font.FontStyle as ComposeFontStyle

enum class FontStyle {
    Normal,
    Italic,
}

fun FontStyle.toComposeFontStyle(): ComposeFontStyle = when (this) {
    FontStyle.Normal -> ComposeFontStyle.Normal
    FontStyle.Italic -> ComposeFontStyle.Italic
}

fun ComposeFontStyle.toFontStyle(): FontStyle = when (this) {
    ComposeFontStyle.Normal -> FontStyle.Normal
    ComposeFontStyle.Italic -> FontStyle.Italic
    else -> FontStyle.Normal
}
