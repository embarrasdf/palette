package com.embarrasdf.palette.theme.semantic.spacing

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val none: Dp,
    val xs: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
)

val PaletteSpacing = Spacing(
    none = 0.dp,
    xs = 4.dp,
    small = 8.dp,
    medium = 16.dp,
    large = 24.dp,
)

fun Spacing.copy(
    token: SpacingToken,
    value: Dp,
) = this.copy(
    none = if (token == SpacingToken.None) value else this.none,
    xs = if (token == SpacingToken.XS) value else this.xs,
    small = if (token == SpacingToken.Small) value else this.small,
    medium = if (token == SpacingToken.Medium) value else this.medium,
    large = if (token == SpacingToken.Large) value else this.large,
)
