package com.embarrasdf.palette.components.demo.media

import com.embarrasdf.palette.theme.PaletteTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.paddingValuesControls
import com.embarrasdf.palette.components.media.PlayPauseButton
import com.embarrasdf.palette.components.media.PlayPauseButtonStyle
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PlayPauseButtonDemo(
    modifier: Modifier = Modifier,
) {
    var isPlaying by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }
    val base = PaletteTheme.component.media.playPauseButton
    var contentPadding by remember { mutableStateOf(base.buttonStyle.contentPadding) }

    val controls = persistentListOf(
        Control.Toggle(
            name = "Playing",
            value = { isPlaying },
            onValueChange = { isPlaying = it },
        ),
        Control.Toggle(
            name = "Enabled",
            value = { isEnabled },
            onValueChange = { isEnabled = it },
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
        PlayPauseButtonDemo(
            isPlaying = isPlaying,
            isEnabled = isEnabled,
            onPlayPauseClick = { isPlaying = !isPlaying },
            style = base.copy(buttonStyle = base.buttonStyle.copy(contentPadding = contentPadding)),
        )
    }
}

@Composable
fun DemoScope.PlayPauseButtonDemo(
    isPlaying: Boolean,
    isEnabled: Boolean,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PlayPauseButtonStyle = PaletteTheme.component.media.playPauseButton,
) {
    PlayPauseButton(
        onClick = onPlayPauseClick,
        isPlaying = isPlaying,
        isEnabled = isEnabled,
        style = style,
        modifier = modifier
            .size(52.dp)
            .align(Alignment.Center),
    )
}
