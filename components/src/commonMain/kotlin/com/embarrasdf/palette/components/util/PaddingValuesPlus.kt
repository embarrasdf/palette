package com.embarrasdf.palette.components.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return this.plus(other, layoutDirection)
}

fun PaddingValues.plus(
    other: PaddingValues,
    layoutDirection: LayoutDirection,
): PaddingValues {
    return plus(
        start = other.calculateStartPadding(layoutDirection = layoutDirection),
        top = other.calculateTopPadding(),
        end = other.calculateEndPadding(layoutDirection = layoutDirection),
        bottom = other.calculateBottomPadding(),
        layoutDirection = layoutDirection,
    )
}

@Composable
fun PaddingValues.plus(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return this.plus(
        start = start,
        top = top,
        end = end,
        bottom = bottom,
        layoutDirection = layoutDirection,
    )
}

fun PaddingValues.plus(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
    layoutDirection: LayoutDirection,
): PaddingValues {
    return copy(
        start = this.calculateStartPadding(layoutDirection = layoutDirection) + start,
        top = this.calculateTopPadding() + top,
        end = this.calculateEndPadding(layoutDirection = layoutDirection) + end,
        bottom = this.calculateBottomPadding() + bottom,
        layoutDirection = layoutDirection,
    )
}

@Composable
fun PaddingValues.plus(
    horizontal: PaddingValues = PaddingValues(0.dp),
    vertical: PaddingValues = PaddingValues(0.dp),
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return this.plus(
        vertical = vertical,
        horizontal = horizontal,
        layoutDirection = layoutDirection,
    )
}

fun PaddingValues.plus(
    horizontal: PaddingValues = PaddingValues(0.dp),
    vertical: PaddingValues = PaddingValues(0.dp),
    layoutDirection: LayoutDirection,
): PaddingValues {
    return this.plus(
        start = horizontal.calculateStartPadding(layoutDirection = layoutDirection),
        top = vertical.calculateTopPadding(),
        end = horizontal.calculateEndPadding(layoutDirection = layoutDirection),
        bottom = vertical.calculateBottomPadding(),
        layoutDirection = layoutDirection,
    )
}

@Composable
fun PaddingValues.plus(all: Dp): PaddingValues {
    return this.plus(
        vertical = PaddingValues(all = all),
        horizontal = PaddingValues(all = all),
        layoutDirection = LocalLayoutDirection.current,
    )
}

fun PaddingValues.plus(
    all: Dp,
    layoutDirection: LayoutDirection,
): PaddingValues {
    return this.plus(
        vertical = PaddingValues(all = all),
        horizontal = PaddingValues(all = all),
        layoutDirection = layoutDirection,
    )
}
