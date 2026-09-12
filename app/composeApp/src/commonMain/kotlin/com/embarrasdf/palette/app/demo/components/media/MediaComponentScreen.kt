package com.embarrasdf.palette.app.demo.components.media

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.demo.components.media.navigation.MediaComponent
import com.embarrasdf.palette.components.demo.media.MediaControlBarDemo
import com.embarrasdf.palette.components.demo.media.MediaControlSheetDemo
import com.embarrasdf.palette.components.demo.media.MediaItemArtworkDemo
import com.embarrasdf.palette.components.demo.media.PlayPauseButtonDemo
import com.embarrasdf.palette.components.demo.media.SkipButtonDemo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun MediaComponentScreen(
    component: MediaComponent,
    onNavigateUp: () -> Unit,
    onThemeClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            DemoTopBar(
                title = component.title,
                onNavigateUp = onNavigateUp,
                onThemeClick = onThemeClick,
            )
        },
    ) { innerPadding ->
        when (component) {
            MediaComponent.MediaControlBar -> MediaControlBarDemo(
                modifier = Modifier.padding(innerPadding)
            )
            MediaComponent.MediaControlSheet -> MediaControlSheetDemo(
                modifier = Modifier.padding(innerPadding)
            )
            MediaComponent.MediaItemArtwork -> MediaItemArtworkDemo(
                modifier = Modifier.padding(innerPadding)
            )
            MediaComponent.PlayPauseButton -> PlayPauseButtonDemo(
                modifier = Modifier.padding(innerPadding)
            )
            MediaComponent.SkipButton -> SkipButtonDemo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
