package com.embarrasdf.palette.components.media

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Sizing
import com.embarrasdf.palette.components.core.IconStyle
import com.embarrasdf.palette.components.core.size

data class SkipButtonStyle(
    val buttonStyle: ButtonStyle = ButtonStyle(contentPadding = PaddingValues(0.dp)),
    val iconStyle: IconStyle = IconStyle(size = Sizing.Scale(0.7f)),
)

@Composable
fun SkipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: SkipButtonStyle = SkipButtonStyle(),
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        style = style.buttonStyle,
        modifier = modifier.aspectRatio(1f),
    ) { shapePadding ->
        SkipIcon(
            style = style.iconStyle,
            modifier = Modifier
                .padding(shapePadding),
        )
    }
}

@Composable
fun SkipIcon(
    modifier: Modifier = Modifier,
    style: IconStyle = IconStyle(),
) {
    Canvas(modifier = modifier.size(style.size)) {
        val barWidth = size.width * 0.15f
        val gap = size.width * 0.08f

        drawRect(
            color = style.color,
            topLeft = Offset(size.width - barWidth, 0f),
            size = Size(barWidth, size.height),
        )

        val path = Path().apply {
            moveTo(0f, 0f)                                         // top left (base)
            lineTo(0f, size.height)                               // bottom left (base)
            lineTo(size.width - barWidth - gap, size.height / 2f) // right tip
            close()
        }
        drawPath(path, color = style.color)
    }
}

@Preview
@Composable
private fun SkipButtonPreview() {
    SkipButton(onClick = {})
}

@Preview
@Composable
private fun SkipIconPreview() {
    SkipIcon()
}
