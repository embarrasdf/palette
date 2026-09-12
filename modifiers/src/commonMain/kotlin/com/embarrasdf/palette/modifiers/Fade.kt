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
import com.alexrdclement.trace.trace

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE)
@Retention(AnnotationRetention.SOURCE)
annotation class FadeSideMask

enum class FadeSide(val value: Int) {
    Left(1 shl 0),    // 1
    Top(1 shl 1),     // 2
    Right(1 shl 2),   // 4
    Bottom(1 shl 3);  // 8

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
) = fade(FadeSide.Bottom, length, borderColor)

fun Modifier.fade(
    side: FadeSide,
    length: Dp,
    borderColor: Color? = null,
) = fade(side.value, length, borderColor)

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
                val (brush, topLeft, size) = when (side) {
                    FadeSide.Left -> Triple(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color.Black),
                            startX = 0f,
                            endX = fadeLengthPx
                        ),
                        Offset(0f, 0f),
                        Size(fadeLengthPx, size.height)
                    )
                    FadeSide.Top -> Triple(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black),
                            startY = 0f,
                            endY = fadeLengthPx
                        ),
                        Offset(0f, 0f),
                        Size(size.width, fadeLengthPx)
                    )
                    FadeSide.Right -> Triple(
                        Brush.horizontalGradient(
                            listOf(Color.Black, Color.Transparent),
                            startX = size.width - fadeLengthPx,
                            endX = size.width
                        ),
                        Offset(size.width - fadeLengthPx, 0f),
                        Size(fadeLengthPx, size.height)
                    )
                    FadeSide.Bottom -> Triple(
                        Brush.verticalGradient(
                            listOf(Color.Black, Color.Transparent),
                            startY = size.height - fadeLengthPx,
                            endY = size.height
                        ),
                        Offset(0f, size.height - fadeLengthPx),
                        Size(size.width, fadeLengthPx)
                    )
                }

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
                        style = Stroke(2f)
                    )
                }
            }
        }
    }
