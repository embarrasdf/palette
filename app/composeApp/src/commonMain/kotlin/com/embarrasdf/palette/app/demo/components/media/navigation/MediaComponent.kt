package com.embarrasdf.palette.app.demo.components.media.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class MediaComponent : CatalogItem {
    MediaControlBar,
    MediaControlSheet,
    MediaItemArtwork,
    PlayPauseButton,
    SkipButton,
    ;

    override val title = this.name
}
