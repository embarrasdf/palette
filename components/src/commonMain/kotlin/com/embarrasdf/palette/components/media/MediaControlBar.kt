package com.embarrasdf.palette.components.media

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.embarrasdf.palette.components.MediaControlBarContentDescription
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.media.model.Artist
import com.embarrasdf.palette.components.media.model.MediaItem
import com.embarrasdf.palette.components.util.Spacer
import com.embarrasdf.palette.components.util.calculateEndPadding
import com.embarrasdf.palette.components.util.calculateHorizontalPadding
import com.embarrasdf.palette.components.util.calculateStartPadding
import com.embarrasdf.palette.components.util.calculateVerticalPadding
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.util.toIntSize
import com.embarrasdf.palette.components.util.toPx
import com.alexrdclement.trace.trace
import kotlin.math.roundToInt

private const val TraceName = "MediaControlBar"
private const val ArtworkTraceName = "$TraceName:MediaItemArtwork"

data class MediaControlBarStyle(
    val titleStyle: TextStyle = TextStyle(),
    val artistStyle: TextStyle = TextStyle(),
    val contentSpacing: Dp = 8.dp,
    val playPauseButtonSize: Dp = 52.dp,
    val contentPadding: PaddingValues = PaddingValues(0.dp),
    val minContentSize: DpSize = DpSize(width = 64.dp, height = 64.dp),
    val maxContentSize: DpSize = DpSize(width = Dp.Infinity, height = Dp.Infinity),
    val artworkStyle: MediaItemArtworkStyle = MediaItemArtworkStyle(),
    val playPauseButtonStyle: PlayPauseButtonStyle = PlayPauseButtonStyle(),
    val surfaceStyle: SurfaceStyle = SurfaceStyle(),
)

private data class CachedSizes(
    val minContentWidth: Float,
    val minContentHeight: Float,
    val contentWidthPaddedDelta: Float,
    val contentHeightPaddedDelta: Float,
    val contentStartX: Float,
    val contentStartY: Float,
    val xDelta: Float,
    val yDelta: Float,
)

/**
 * @param expandedContentSize Size the artwork animates to at full [progress], clamped to
 *   [MediaControlBarStyle.minContentSize]..[MediaControlBarStyle.maxContentSize] and the incoming
 *   constraints. [DpSize.Unspecified] (default) fills the available space up to the style's max.
 */
@Composable
fun MediaControlBar(
    mediaItem: MediaItem,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: MediaControlBarStyle = MediaControlBarStyle(),
    expandedContentSize: DpSize = DpSize.Unspecified,
    progress: () -> Float = { 0f },
    onClick: () -> Unit = {},
    stateDescription: String? = null,
) {
    trace(TraceName) {
        val contentPadding = style.contentPadding
        val paddingWidthPx = contentPadding.calculateHorizontalPadding().toPx()
        val paddingHeightPx = contentPadding.calculateVerticalPadding().toPx()
        val minContentSizePx = style.minContentSize.toIntSize()
        val maxContentSizePx = DpSize(
            width = if (expandedContentSize.width.isSpecified) {
                minOf(expandedContentSize.width, style.maxContentSize.width)
            } else style.maxContentSize.width,
            height = if (expandedContentSize.height.isSpecified) {
                minOf(expandedContentSize.height, style.maxContentSize.height)
            } else style.maxContentSize.height,
        ).toIntSize()

        BoxWithConstraints {
            val maxWidthPx = constraints.maxWidth
            val minContentWidth by derivedStateOf {
                maxOf(minContentSizePx.width, constraints.minWidth)
            }
            val minContentHeight by derivedStateOf {
                maxOf(minContentSizePx.height, constraints.minHeight)
            }
            val maxContentWidth by derivedStateOf {
                minOf(maxContentSizePx.width, constraints.maxWidth)
            }
            val maxContentHeight by derivedStateOf {
                minOf(maxContentSizePx.height, constraints.maxHeight)
            }

            val cachedSizes = remember(
                maxWidthPx,
                minContentWidth,
                minContentHeight,
                maxContentWidth,
                maxContentHeight,
            ) {
                val minContentWidthPadded = minContentWidth + paddingWidthPx
                val minContentHeightPadded = minContentHeight + paddingHeightPx
                val maxContentWidthPadded = maxContentWidth - paddingWidthPx
                val maxContentHeightPadded = maxContentHeight - paddingHeightPx

                val contentWidthPaddedDelta = maxContentWidthPadded - minContentWidthPadded
                val contentHeightPaddedDelta = maxContentHeightPadded - minContentHeightPadded

                val contentStartX = 0f
                val contentEndX = ((maxWidthPx - maxContentWidthPadded) / 2f)
                val xDelta = contentEndX - contentStartX

                val contentStartY = 0f
                val contentEndY = contentStartY
                val yDelta = contentEndY - contentStartY

                CachedSizes(
                    minContentWidth = minContentWidth.toFloat(),
                    minContentHeight = minContentHeight.toFloat(),
                    contentWidthPaddedDelta = contentWidthPaddedDelta,
                    contentHeightPaddedDelta = contentHeightPaddedDelta,
                    contentStartX = contentStartX,
                    contentStartY = contentStartY,
                    xDelta = xDelta,
                    yDelta = yDelta,
                )
            }

            Surface(onClick = onClick, style = style.surfaceStyle) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .heightIn(minHeight)
                        .fillMaxWidth()
                        .padding(vertical = contentPadding.calculateVerticalPadding())
                        .semantics {
                            contentDescription =
                                MediaControlBarContentDescription
                            stateDescription?.let {
                                this@semantics.stateDescription = it
                            }
                        }
                ) {
                    Spacer(width = contentPadding.calculateStartPadding())

                    MediaItemArtwork(
                        imageUrl = mediaItem.artworkLargeUrl,
                        style = style.artworkStyle,
                        modifier = Modifier
                            .layout { measurable, constraints ->
                                trace("$ArtworkTraceName:layout") {
                                    val computedProgress = progress()

                                    val widthDelta = cachedSizes.contentWidthPaddedDelta * computedProgress
                                    val width = cachedSizes.minContentWidth + widthDelta

                                    val heightDelta = cachedSizes.contentHeightPaddedDelta * computedProgress
                                    val height = cachedSizes.minContentHeight + heightDelta

                                    val x = cachedSizes.contentStartX + (cachedSizes.xDelta * computedProgress)
                                    val y = cachedSizes.contentStartY + (cachedSizes.yDelta * computedProgress)

                                    val placeable = measurable.measure(
                                        constraints.copy(
                                            minWidth = width.roundToInt(),
                                            minHeight = height.roundToInt(),
                                            maxWidth = width.roundToInt(),
                                            maxHeight = height.roundToInt(),
                                        )
                                    )
                                    layout(width.roundToInt(), height.roundToInt()) {
                                        placeable.place(x.roundToInt(), y.roundToInt())
                                    }
                                }
                            }
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = style.contentSpacing)
                            .graphicsLayer {
                                alpha = 1f - progress()
                            }
                    ) {
                        Text(
                            text = mediaItem.title,
                            style = style.titleStyle,
                            maxLines = 1,
                            modifier = Modifier
                                .basicMarquee()
                        )
                        Text(
                            text = mediaItem.artists.joinToString { it.name },
                            style = style.artistStyle,
                            maxLines = 1,
                            modifier = Modifier
                                .basicMarquee()
                        )
                    }

                    PlayPauseButton(
                        onClick = onPlayPauseClick,
                        isPlaying = isPlaying,
                        style = style.playPauseButtonStyle,
                        modifier = Modifier
                            .size(style.playPauseButtonSize)
                            .padding(style.contentSpacing)
                            .graphicsLayer {
                                alpha = 1f - progress()
                            }
                    )

                    Spacer(width = contentPadding.calculateEndPadding())
                }
            }
        }
    }
}

private class ProgressPreviewParameterProvider : PreviewParameterProvider<Float> {
    override val values = sequenceOf(0f, 0.5f, 1f)
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ProgressPreviewParameterProvider::class) progress: Float
) {
    var isPlaying by remember { mutableStateOf(false) }
    MediaControlBar(
        mediaItem = MediaItem(
            artworkThumbnailUrl = null,
            artworkLargeUrl = null,
            title = "Title",
            artists = listOf(Artist("Artist 1"), Artist("Artist 2")),
        ),
        isPlaying = isPlaying,
        onPlayPauseClick = { isPlaying = !isPlaying },
        progress = { progress },
    )
}
