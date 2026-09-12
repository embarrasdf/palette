package com.embarrasdf.palette.theme.semantic.dimension

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
    val touchTargetMin: Dp,
    val iconSmall: Dp,
)

val PaletteSize = Size(
    touchTargetMin = 48.dp,
    iconSmall = 16.dp,
)

fun Size.copy(
    token: SizeToken,
    value: Dp,
) = this.copy(
    touchTargetMin = if (token == SizeToken.TouchTargetMin) value else this.touchTargetMin,
    iconSmall = if (token == SizeToken.IconSmall) value else this.iconSmall,
)
