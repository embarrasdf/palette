package com.embarrasdf.palette.components.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.components.core.BorderStyle
import com.embarrasdf.palette.components.core.Shape
import com.embarrasdf.palette.components.core.border
import com.embarrasdf.palette.components.core.toComposeShape

data class ColorDisplayStyle(
    val shape: Shape = Shape.Circle,
    val borderStyle: BorderStyle? = null,
)

@Composable
fun ColorDisplay(
    color: Color,
    modifier: Modifier = Modifier,
    style: ColorDisplayStyle = ColorDisplayStyle(),
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(color = color, shape = style.shape.toComposeShape())
            .then(if (style.borderStyle != null) Modifier.border(style = style.borderStyle) else Modifier),
    )
}

@Preview
@Composable
fun ColorDisplayPreview() {
    ColorDisplay(
        color = Color.Red,
        modifier = Modifier
            .fillMaxSize(),
    )
}
