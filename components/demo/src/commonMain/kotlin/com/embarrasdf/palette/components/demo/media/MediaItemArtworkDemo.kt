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
import com.embarrasdf.palette.components.media.MediaItemArtwork
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MediaItemArtworkDemo(
    modifier: Modifier = Modifier,
) {
    var isEnabled by remember { mutableStateOf(true) }

    val controls = persistentListOf(
        Control.Toggle(
            name = "Enabled",
            value = { isEnabled },
            onValueChange = { isEnabled = it },
        ),
    )

    Demo(
        controls = controls,
        modifier = modifier.fillMaxSize(),
    ) {
        MediaItemArtworkDemo(isEnabled = isEnabled)
    }
}

@Composable
fun DemoScope.MediaItemArtworkDemo(
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    MediaItemArtwork(
        imageUrl = null,
        isEnabled = isEnabled,
        style = PaletteTheme.component.media.mediaItemArtwork,
        modifier = modifier
            .size(200.dp)
            .align(Alignment.Center),
    )
}
