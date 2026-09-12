package com.embarrasdf.palette.components.demo.media

import com.embarrasdf.palette.theme.PaletteTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.paddingValuesControls
import com.embarrasdf.palette.components.media.MediaControlBar
import com.embarrasdf.palette.components.media.MediaControlBarStyle
import com.embarrasdf.palette.components.media.model.Artist
import com.embarrasdf.palette.components.media.model.MediaItem
import kotlinx.collections.immutable.persistentListOf

private val PreviewMediaItem = MediaItem(
    artworkThumbnailUrl = null,
    artworkLargeUrl = null,
    title = "Title",
    artists = listOf(Artist("Artist 1"), Artist("Artist 2")),
)

@Composable
fun MediaControlBarDemo(
    modifier: Modifier = Modifier,
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }
    val contentPaddingInitial = PaletteTheme.component.media.mediaControlBar.contentPadding
    var contentPadding by remember { mutableStateOf(contentPaddingInitial) }

    val controls = persistentListOf(
        Control.Slider(
            name = "Progress",
            value = { progress },
            onValueChange = { progress = it },
        ),
        Control.Toggle(
            name = "Playing",
            value = { isPlaying },
            onValueChange = { isPlaying = it },
        ),
        Control.ControlColumn(
            name = "Style",
            indent = true,
            expandedInitial = true,
            controls = {
                persistentListOf(
                    paddingValuesControls(
                        name = "Content padding",
                        value = { contentPadding },
                        onValueChange = { contentPadding = it },
                    ),
                )
            },
        ),
    )

    Demo(
        controls = controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        MediaControlBarDemo(
            progress = { progress },
            isPlaying = isPlaying,
            onPlayPauseClick = { isPlaying = !isPlaying },
            style = PaletteTheme.component.media.mediaControlBar.copy(contentPadding = contentPadding),
        )
    }
}

@Composable
fun DemoScope.MediaControlBarDemo(
    progress: () -> Float,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: MediaControlBarStyle = PaletteTheme.component.media.mediaControlBar,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxHeight = constraints.maxHeight
        MediaControlBar(
            mediaItem = PreviewMediaItem,
            isPlaying = isPlaying,
            onPlayPauseClick = onPlayPauseClick,
            progress = progress,
            style = style,
            expandedContentSize = DpSize(
                width = maxWidth,
                height = with(LocalDensity.current) { maxHeight.toDp() / 2f },
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
