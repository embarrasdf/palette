package com.embarrasdf.palette.components.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

@Composable
fun DpSize.toIntSize(): IntSize = IntSize(
    width = this.width.toPx().roundToInt(),
    height = this.height.toPx().roundToInt(),
)
