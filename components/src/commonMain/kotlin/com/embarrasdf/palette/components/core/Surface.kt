package com.embarrasdf.palette.components.core

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.preview.BoolPreviewParameterProvider
import kotlin.math.sqrt

data class SurfaceStyle(
    val shape: Shape = Shape.Rectangle(),
    val color: Color = Color.Unspecified,
    val borderStyle: BorderStyle? = null,
    val indication: Indication? = null,
)

@Composable
fun Surface(
    modifier: Modifier = Modifier,
    style: SurfaceStyle = SurfaceStyle(),
    content: @Composable (PaddingValues) -> Unit
) {
    Box(
        modifier = modifier
            .shapeLayout(style.shape)
            .surface(
                composeShape = style.shape.toComposeShape(),
                backgroundColor = style.color,
                borderStyle = style.borderStyle,
            )
            .semantics(mergeDescendants = false) {
                isTraversalGroup = true
            }
            .pointerInput(Unit) {},
        propagateMinConstraints = true
    ) {
        ShapeContent(style.shape, content)
    }
}

@Composable
fun Surface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: SurfaceStyle = SurfaceStyle(),
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    hapticFeedbackEnabled: Boolean = true,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable (PaddingValues) -> Unit
) {
    Box(
        propagateMinConstraints = true,
        modifier = modifier
            .shapeLayout(style.shape)
            .shapeClickable(
                shape = style.shape,
                interactionSource = interactionSource,
                indication = style.indication,
                enabled = enabled,
                onClick = onClick,
                onLongClickLabel = onLongClickLabel,
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
                hapticFeedbackEnabled = hapticFeedbackEnabled,
            )
            .surface(
                composeShape = style.shape.toComposeShape(),
                backgroundColor = style.color,
                borderStyle = style.borderStyle,
            )
    ) {
        ShapeContent(style.shape, content)
    }
}

@Composable
private fun ShapeContent(
    shape: Shape,
    content: @Composable (PaddingValues) -> Unit,
) {
    when (shape) {
        Shape.Circle, Shape.Diamond -> {
            BoxWithConstraints(contentAlignment = Alignment.Center) {
                val inset: Dp = minOf(maxWidth, maxHeight) / 2 * (1f - (1f / sqrt(2f)))
                content(PaddingValues(inset))
            }
        }
        Shape.Triangle -> {
            BoxWithConstraints(contentAlignment = Alignment.Center) {
                // Largest axis-aligned rectangle inscribed in an equilateral triangle:
                // horizontal inset = width / 4 on each side, top inset = height / 3
                content(
                    PaddingValues(
                        start = maxWidth / 4,
                        end = maxWidth / 4,
                        top = maxHeight / 3,
                        bottom = 0.dp,
                    )
                )
            }
        }
        is Shape.Rectangle -> {
            if (shape.cornerRadius > 0.dp) {
                // Inset the content so it clears the rounded corners. The inset is
                // derived from the corner radius alone (not the measured size) so this
                // stays a plain Box, which — unlike SubcomposeLayout — supports intrinsic
                // measurement and can therefore be placed under IntrinsicSize.
                val inset = shape.cornerRadius * (1f - (1f / sqrt(2f)))
                Box(contentAlignment = Alignment.Center) {
                    content(PaddingValues(inset))
                }
            } else {
                content(PaddingValues(0.dp))
            }
        }
    }
}

@Composable
private fun Modifier.surface(
    composeShape: androidx.compose.ui.graphics.Shape,
    backgroundColor: Color,
    borderStyle: BorderStyle?,
) = this
    .graphicsLayer(shape = composeShape, clip = true)
    .then(if (borderStyle != null) Modifier.border(style = borderStyle) else Modifier)
    .background(color = backgroundColor, shape = composeShape)

@Preview
@Composable
private fun Preview(
    @PreviewParameter(BoolPreviewParameterProvider::class) isDarkTheme: Boolean,
) {
    Surface {
        Text("Hello world")
    }
}

@Preview
@Composable
private fun PreviewClickable(
    @PreviewParameter(BoolPreviewParameterProvider::class) enabled: Boolean,
) {
    Surface(
        onClick = {},
        enabled = enabled,
    ) {
        Text("Hello world")
    }
}
