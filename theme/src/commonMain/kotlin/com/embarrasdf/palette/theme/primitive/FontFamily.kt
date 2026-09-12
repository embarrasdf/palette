package com.embarrasdf.palette.theme.primitive

import androidx.compose.ui.text.font.FontFamily as ComposeFontFamily

enum class FontFamily {
    Default,
    Monospace,
    SansSerif,
    Serif,
    Cursive,
}

fun FontFamily.toComposeFontFamily(): ComposeFontFamily {
    return when (this) {
        FontFamily.Default -> ComposeFontFamily.Default
        FontFamily.Monospace -> ComposeFontFamily.Monospace
        FontFamily.SansSerif -> ComposeFontFamily.SansSerif
        FontFamily.Serif -> ComposeFontFamily.Serif
        FontFamily.Cursive -> ComposeFontFamily.Cursive
    }
}

fun ComposeFontFamily.toFontFamily(): FontFamily {
    return when (this) {
        ComposeFontFamily.Default -> FontFamily.Default
        ComposeFontFamily.Monospace -> FontFamily.Monospace
        ComposeFontFamily.SansSerif -> FontFamily.SansSerif
        ComposeFontFamily.Serif -> FontFamily.Serif
        ComposeFontFamily.Cursive -> FontFamily.Cursive
        else -> throw IllegalArgumentException("Unknown FontFamily: $this")
    }
}
