package com.embarrasdf.palette.components.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun PaddingValues.calculateVerticalPadding() = this.calculateTopPadding() + this.calculateBottomPadding()

fun PaddingValues.calculateVerticalPaddingValues() = PaddingValues(
    start = 0.dp,
    top = this.calculateTopPadding(),
    end = 0.dp,
    bottom = this.calculateBottomPadding(),
)

@Composable
@ReadOnlyComposable
fun PaddingValues.calculateStartPadding() = this.calculateStartPadding(LocalLayoutDirection.current)

@Composable
@ReadOnlyComposable
fun PaddingValues.calculateEndPadding() = this.calculateEndPadding(LocalLayoutDirection.current)

@Composable
@ReadOnlyComposable
fun PaddingValues.calculateHorizontalPaddingValues() = PaddingValues(
    start = this.calculateStartPadding(),
    top = 0.dp,
    end = this.calculateEndPadding(),
    bottom = 0.dp,
)

@Composable
fun PaddingValues.calculateHorizontalPadding() = calculateHorizontalPadding(
    layoutDirection = LocalLayoutDirection.current,
)

fun PaddingValues.calculateHorizontalPadding(layoutDirection: LayoutDirection) =
    this.calculateStartPadding(layoutDirection) + this.calculateEndPadding(layoutDirection)
