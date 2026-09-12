package com.embarrasdf.palette.components.demo.media

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.layout.PeekSheetAnchor
import com.embarrasdf.palette.components.layout.rememberPeekSheetState
import com.embarrasdf.palette.components.media.MediaControlSheet
import com.embarrasdf.palette.components.media.model.Artist
import com.embarrasdf.palette.components.media.model.MediaItem
import com.embarrasdf.palette.components.util.copy
import com.embarrasdf.palette.components.util.horizontalPaddingValues
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.coroutines.launch

@Composable
fun MediaControlSheetDemo(
    modifier: Modifier = Modifier,
) {
    val mediaItem = MediaItem(
        artworkThumbnailUrl = null,
        artworkLargeUrl = null,
        title = "Title",
        artists = listOf(Artist("Artist 1"), Artist("Artist 2"))
    )
    var isPlaying by remember { mutableStateOf(false) }
    val state = rememberPeekSheetState(initialValue = PeekSheetAnchor.Peek)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.horizontalPaddingValues())
    ) {
        Text(text = "Current value ${state.currentValue}", style = PaletteTheme.component.core.text.labelLarge)
        Text(text = "Target value ${state.targetValue}", style = PaletteTheme.component.core.text.labelLarge)
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val maxHeight = constraints.maxHeight
        MediaControlSheet(
            mediaItem = mediaItem,
            isPlaying = isPlaying,
            onPlayPauseClick = { isPlaying = !isPlaying },
            onControlBarClick = {
                coroutineScope.launch {
                    if (state.isExpanded) {
                        state.peek()
                    } else {
                        state.expand()
                    }
                }
            },
            state = state,
            style = PaletteTheme.component.media.mediaControlSheet,
            expandedContentSize = DpSize(
                width = Dp.Infinity,
                height = with(LocalDensity.current) { maxHeight.toDp() / 2f },
            ),
            modifier = Modifier
                .padding(WindowInsets.safeDrawing.asPaddingValues().copy(top = 0.dp)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = state.partialToFullProgress
                    }
            ) {
                Text(text = "Current value ${state.currentValue}", style = PaletteTheme.component.core.text.labelLarge)
                Text(text = "Target value ${state.targetValue}", style = PaletteTheme.component.core.text.labelLarge)
            }
        }
    }
}
