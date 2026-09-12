package com.embarrasdf.palette.theme.component.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.media.MediaControlBarStyle
import com.embarrasdf.palette.components.media.MediaControlSheetStyle
import com.embarrasdf.palette.components.media.MediaItemArtworkStyle
import com.embarrasdf.palette.components.media.PlayPauseButtonStyle
import com.embarrasdf.palette.components.media.SkipButtonStyle
import com.embarrasdf.palette.components.core.Sizing
import com.embarrasdf.palette.components.core.IconStyle
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.core.CoreStyles
import com.embarrasdf.palette.theme.component.core.TextStyles
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.shape.toShape

object MediaStyles {

    val playPauseButton: PlayPauseButtonStyle
        @Composable get() = PlayPauseButtonStyle(
            buttonStyle = ButtonStyle(
                containerColor = ColorToken.Primary.toColor(),
                shape = ShapeToken.Primary.toShape(),
                contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
                disabledContentAlpha = PaletteTheme.semantic.color.disabledContentAlpha,
                disabledContainerAlpha = PaletteTheme.semantic.color.disabledContainerAlpha,
                indication = PaletteTheme.semantic.indication,
            ),
            iconStyle = IconStyle(
                size = Sizing.Scale(0.9f),
                color = PaletteTheme.semantic.color.onPrimary,
            ),
        )

    val skipButton: SkipButtonStyle
        @Composable get() = SkipButtonStyle(
            buttonStyle = CoreStyles.button.secondary.copy(
                contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
            ),
            iconStyle = IconStyle(
                size = Sizing.Scale(0.7f),
                color = PaletteTheme.semantic.color.secondary,
            ),
        )

    val mediaItemArtwork: MediaItemArtworkStyle
        @Composable get() = MediaItemArtworkStyle(
            fallbackTextStyle = TextStyles.labelLarge,
        )

    val mediaControlBar: MediaControlBarStyle
        @Composable get() = MediaControlBarStyle(
            titleStyle = TextStyles.titleMedium,
            artistStyle = TextStyles.bodyMedium,
            contentSpacing = PaletteTheme.semantic.dimension.spacing.small,
            artworkStyle = mediaItemArtwork,
            playPauseButtonStyle = playPauseButton,
            surfaceStyle = CoreStyles.surface.default,
            maxContentSize = DpSize(width = Dp.Infinity, height = 600.dp),
        )

    val mediaControlSheet: MediaControlSheetStyle
        @Composable get() = MediaControlSheetStyle(
            controlBarStyle = mediaControlBar,
        )
}
