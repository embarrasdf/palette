package com.embarrasdf.palette.components.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
@ReadOnlyComposable
fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }
