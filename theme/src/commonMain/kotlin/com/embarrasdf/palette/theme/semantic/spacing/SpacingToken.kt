package com.embarrasdf.palette.theme.semantic.spacing

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.embarrasdf.palette.theme.PaletteTheme

enum class SpacingToken {
    None,
    XS,
    Small,
    Medium,
    Large,
}

fun SpacingToken.toSpacing(spacing: Spacing): Dp {
    return when (this) {
        SpacingToken.None -> spacing.none
        SpacingToken.XS -> spacing.xs
        SpacingToken.Small -> spacing.small
        SpacingToken.Medium -> spacing.medium
        SpacingToken.Large -> spacing.large
    }
}

@Composable
fun SpacingToken.toSpacing(): Dp {
    return toSpacing(PaletteTheme.semantic.dimension.spacing)
}
