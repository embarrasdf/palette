package com.embarrasdf.palette.theme.semantic.color

import com.embarrasdf.palette.theme.PaletteTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ColorToken {
    Primary,
    OnPrimary,
    Secondary,
    OnSecondary,
    Background,
    OnBackground,
    Surface,
    OnSurface,
    Outline,
}

@Composable
fun ColorToken.toColor(): Color {
    return when (this) {
        ColorToken.Primary -> PaletteTheme.semantic.color.primary
        ColorToken.OnPrimary -> PaletteTheme.semantic.color.onPrimary
        ColorToken.Secondary -> PaletteTheme.semantic.color.secondary
        ColorToken.OnSecondary -> PaletteTheme.semantic.color.onSecondary
        ColorToken.Background -> PaletteTheme.semantic.color.background
        ColorToken.OnBackground -> PaletteTheme.semantic.color.onBackground
        ColorToken.Surface -> PaletteTheme.semantic.color.surface
        ColorToken.OnSurface -> PaletteTheme.semantic.color.onSurface
        ColorToken.Outline -> PaletteTheme.semantic.color.outline
    }
}
