package com.embarrasdf.palette.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class ChevronDirection {
    Up,
    Down,
    Left,
    Right,
}

data class ChevronButtonStyle(
    val buttonStyle: ButtonStyle = ButtonStyle(contentPadding = PaddingValues(16.dp)),
    val iconStyle: IconStyle = IconStyle(size = Sizing.Fill),
)

@Composable
fun ChevronButton(
    direction: ChevronDirection,
    modifier: Modifier = Modifier,
    style: ChevronButtonStyle = ChevronButtonStyle(),
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        style = style.buttonStyle,
        modifier = modifier
    ) {
        ChevronIcon(
            direction = direction,
            style = style.iconStyle,
        )
    }
}

@Composable
fun ChevronIcon(
    direction: ChevronDirection,
    modifier: Modifier = Modifier,
    style: IconStyle = IconStyle(),
) {
    val rotation = when (direction) {
        ChevronDirection.Left -> 0f
        ChevronDirection.Up -> 90f
        ChevronDirection.Down -> 270f
        ChevronDirection.Right -> 180f
    }
    Icon(
        style = style,
        modifier = modifier,
    ) { color ->
        Box(
            modifier = Modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = false)
                .rotate(rotation)
                .clip(ChevronIconShape)
                .background(color)
        )
    }
}

private const val ChevronIconWidthProportion = 0.8f
private val ChevronIconShape: Shape = GenericShape { size, _ ->
    val offsetEndX = size.width * ChevronIconWidthProportion
    moveTo(offsetEndX, 0f)
    lineTo(0f, size.height / 2f)
    lineTo(offsetEndX, size.height)
    lineTo(offsetEndX, 0f)
}

@Preview
@Composable
private fun ChevronButtonPreview() {
    Surface {
        ChevronButton(
            direction = ChevronDirection.Right,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun ChevronIconPreview() {
    Surface {
        ChevronIcon(
            direction = ChevronDirection.Right,
        )
    }
}
