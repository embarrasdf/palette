package com.embarrasdf.palette.theme.primitive

import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight

enum class FontWeight {
    Thin,
    ExtraLight,
    Light,
    Normal,
    Medium,
    SemiBold,
    Bold,
    ExtraBold,
    Black,
}

fun FontWeight.toComposeFontWeight(): ComposeFontWeight {
    return when (this) {
        FontWeight.Thin -> ComposeFontWeight.Thin
        FontWeight.ExtraLight -> ComposeFontWeight.ExtraLight
        FontWeight.Light -> ComposeFontWeight.Light
        FontWeight.Normal -> ComposeFontWeight.Normal
        FontWeight.Medium -> ComposeFontWeight.Medium
        FontWeight.SemiBold -> ComposeFontWeight.SemiBold
        FontWeight.Bold -> ComposeFontWeight.Bold
        FontWeight.ExtraBold -> ComposeFontWeight.ExtraBold
        FontWeight.Black -> ComposeFontWeight.Black
    }
}

fun ComposeFontWeight.toFontWeight(): FontWeight {
    return when (this) {
        ComposeFontWeight.Thin -> FontWeight.Thin
        ComposeFontWeight.ExtraLight -> FontWeight.ExtraLight
        ComposeFontWeight.Light -> FontWeight.Light
        ComposeFontWeight.Normal -> FontWeight.Normal
        ComposeFontWeight.Medium -> FontWeight.Medium
        ComposeFontWeight.SemiBold -> FontWeight.SemiBold
        ComposeFontWeight.Bold -> FontWeight.Bold
        ComposeFontWeight.ExtraBold -> FontWeight.ExtraBold
        ComposeFontWeight.Black -> FontWeight.Black
        else -> throw IllegalArgumentException("Unknown FontWeight: $this")
    }
}
