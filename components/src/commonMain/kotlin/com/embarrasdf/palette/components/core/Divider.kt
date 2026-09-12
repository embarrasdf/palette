package com.embarrasdf.palette.components.core

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DividerStyle(
    val thickness: Dp = 1.dp,
    val color: Color = Color.Unspecified,
)

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    style: DividerStyle = DividerStyle(),
) = Canvas(modifier.fillMaxWidth().height(style.thickness)) {
    drawLine(
        color = style.color,
        strokeWidth = style.thickness.toPx(),
        start = Offset(0f, style.thickness.toPx() / 2),
        end = Offset(size.width, style.thickness.toPx() / 2),
    )
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    style: DividerStyle = DividerStyle(),
) = Canvas(modifier.fillMaxHeight().width(style.thickness)) {
    drawLine(
        color = style.color,
        strokeWidth = style.thickness.toPx(),
        start = Offset(style.thickness.toPx() / 2, 0f),
        end = Offset(style.thickness.toPx() / 2, size.height),
    )
}
