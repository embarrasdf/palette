package com.embarrasdf.palette.components.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class IconStyle(
    val size: Sizing = Sizing.Fill,
    val color: Color = Color.Unspecified,
)

@Composable
fun Icon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    style: IconStyle = IconStyle(),
) {
    Icon(modifier = modifier, style = style) { color ->
        Image(
            imageVector = imageVector,
            contentDescription = contentDescription,
            colorFilter = if (color.isSpecified) ColorFilter.tint(color) else null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun Icon(
    modifier: Modifier = Modifier,
    style: IconStyle = IconStyle(),
    content: @Composable BoxScope.(color: Color) -> Unit,
) {
    Box(modifier = modifier.size(style.size)) {
        content(style.color)
    }
}

@Preview
@Composable
private fun IconPreview() {
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = null,
        style = IconStyle(size = Sizing.Fixed(48.dp)),
    )
}
