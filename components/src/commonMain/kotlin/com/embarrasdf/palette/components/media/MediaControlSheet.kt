package com.embarrasdf.palette.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.MediaControlBarStateDescriptionExpanded
import com.embarrasdf.palette.components.MediaControlBarStateDescriptionPartiallyExpanded
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.layout.PeekSheet
import com.embarrasdf.palette.components.layout.PeekSheetAnchor
import com.embarrasdf.palette.components.layout.PeekSheetState
import com.embarrasdf.palette.components.layout.rememberPeekSheetState
import com.embarrasdf.palette.components.media.model.Artist
import com.embarrasdf.palette.components.media.model.MediaItem
import kotlinx.coroutines.launch

data class MediaControlSheetStyle(
    val controlBarStyle: MediaControlBarStyle = MediaControlBarStyle(),
    val contentPadding: PaddingValues = PaddingValues(0.dp),
)

/**
 * @param expandedContentSize Size the control bar's artwork animates to when expanded, forwarded to
 *   [MediaControlBar]. [DpSize.Unspecified] (default) fills the available space up to the bar
 *   style's max.
 */
@Composable
fun MediaControlSheet(
    mediaItem: MediaItem,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onControlBarClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: MediaControlSheetStyle = MediaControlSheetStyle(),
    state: PeekSheetState = rememberPeekSheetState(),
    expandedContentSize: DpSize = DpSize.Unspecified,
    aboveControlBar: @Composable () -> Unit = {},
    belowControlBar: @Composable () -> Unit = {},
) {
    PeekSheet(
        peekHeight = style.controlBarStyle.minContentSize.height,
        modifier = modifier,
        state = state,
        contentPadding = style.contentPadding,
        above = aboveControlBar,
        bar = { progress ->
            MediaControlBar(
                mediaItem = mediaItem,
                isPlaying = isPlaying,
                onPlayPauseClick = onPlayPauseClick,
                onClick = onControlBarClick,
                progress = progress,
                expandedContentSize = expandedContentSize,
                style = style.controlBarStyle,
                stateDescription = when (state.currentValue) {
                    PeekSheetAnchor.Peek -> MediaControlBarStateDescriptionPartiallyExpanded
                    PeekSheetAnchor.Expanded -> MediaControlBarStateDescriptionExpanded
                },
                modifier = Modifier.fillMaxWidth(),
            )
        },
        content = belowControlBar,
    )
}

@Preview
@Composable
private fun Preview() {
    val state = rememberPeekSheetState(initialValue = PeekSheetAnchor.Peek)
    val coroutineScope = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(false) }
    Surface {
        MediaControlSheet(
            mediaItem = MediaItem(
                artworkThumbnailUrl = null,
                artworkLargeUrl = null,
                title = "Title",
                artists = listOf(Artist("Artist 1"), Artist("Artist 2")),
            ),
            isPlaying = isPlaying,
            onPlayPauseClick = { isPlaying = !isPlaying },
            onControlBarClick = {
                coroutineScope.launch {
                    if (state.isExpanded) state.peek() else state.expand()
                }
            },
            state = state,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text("Content")
            }
        }
    }
}
