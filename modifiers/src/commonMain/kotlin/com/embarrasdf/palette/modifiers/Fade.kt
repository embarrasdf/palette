package com.embarrasdf.palette.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.embarrasdf.trace.trace

private const val BottomShift = 3

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE)
@Retention(AnnotationRetention.SOURCE)
annotation class FadeSideMask

enum class FadeSide(val value: Int) {
    Left(1 shl 0), // 1
    Top(1 shl 1), // 2
    Right(1 shl 2), // 4
    Bottom(1 shl BottomShift), // 8
    ;

    companion object {
        fun fromMask(mask: Int): List<FadeSide> {
            return entries.filter { (mask and it.value) != 0 }
        }
    }

    operator fun plus(other: FadeSide): Int = this.value or other.value
    operator fun plus(mask: Int): Int = this.value or mask
}

fun Modifier.bottomFade(
    length: Dp,
    borderColor: Color? = null,
) = fade(
    side = FadeSide.Bottom,
    length = length,
    borderColor = borderColor,
)

fun Modifier.fade(
    side: FadeSide,
    length: Dp,
    borderColor: Color? = null,
) = fade(
    sides = side.value,
    length = length,
    borderColor = borderColor,
)

fun Modifier.fade(
    @FadeSideMask sides: Int,
    length: Dp,
    borderColor: Color? = null,
) = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        trace("fade") {
            drawContent()

            val fadeSides = FadeSide.fromMask(sides)
            val fadeLengthPx = length.toPx()

            for (side in fadeSides) {
                val (brush, topLeft, size) = fadeRegion(
                    side = side,
                    fadeLengthPx = fadeLengthPx,
                    size = size,
                )

                drawRect(
                    brush = brush,
                    topLeft = topLeft,
                    size = size,
                    blendMode = BlendMode.DstIn,
                )

                if (borderColor != null) {
                    drawRect(
                        color = borderColor,
                        topLeft = topLeft,
                        size = size,
                        style = Stroke(2f),
                    )
                }
            }
        }
    }

private data class FadeRegion(
    val brush: Brush,
    val topLeft: Offset,
    val size: Size,
)

private fun fadeRegion(
    side: FadeSide,
    fadeLengthPx: Float,
    size: Size,
): FadeRegion = when (side) {
    FadeSide.Left -> FadeRegion(
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = 0f,
            endX = fadeLengthPx,
        ),
        topLeft = Offset(x = 0f, y = 0f),
        size = Size(width = fadeLengthPx, height = size.height),
    )
    FadeSide.Top -> FadeRegion(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startY = 0f,
            endY = fadeLengthPx,
        ),
        topLeft = Offset(x = 0f, y = 0f),
        size = Size(width = size.width, height = fadeLengthPx),
    )
    FadeSide.Right -> FadeRegion(
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Black, Color.Transparent),
            startX = size.width - fadeLengthPx,
            endX = size.width,
        ),
        topLeft = Offset(x = size.width - fadeLengthPx, y = 0f),
        size = Size(width = fadeLengthPx, height = size.height),
    )
    FadeSide.Bottom -> FadeRegion(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Black, Color.Transparent),
            startY = size.height - fadeLengthPx,
            endY = size.height,
        ),
        topLeft = Offset(x = 0f, y = size.height - fadeLengthPx),
        size = Size(width = size.width, height = fadeLengthPx),
    )
}
