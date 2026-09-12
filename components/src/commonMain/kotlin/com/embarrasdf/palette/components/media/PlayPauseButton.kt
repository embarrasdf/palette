package com.embarrasdf.palette.components.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.PlayPauseButtonContentDescriptionPaused
import com.embarrasdf.palette.components.PlayPauseButtonContentDescriptionPlaying
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Icon
import com.embarrasdf.palette.components.core.Sizing
import com.embarrasdf.palette.components.core.IconStyle
import com.embarrasdf.palette.components.preview.BoolPreviewParameterProvider

data class PlayPauseButtonStyle(
    val buttonStyle: ButtonStyle = ButtonStyle(contentPadding = PaddingValues(0.dp)),
    val iconStyle: IconStyle = IconStyle(size = Sizing.Scale(0.9f)),
)

@Composable
fun PlayPauseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PlayPauseButtonStyle = PlayPauseButtonStyle(),
    onLongClick: (() -> Unit)? = null,
    isPlaying: Boolean = false,
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        onLongClick = onLongClick,
        style = style.buttonStyle,
        enabled = isEnabled,
        modifier = modifier
            .aspectRatio(1f)
    ) { shapePadding ->
        Icon(
            imageVector = if (isPlaying) Icons.Default.Close else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) {
                PlayPauseButtonContentDescriptionPlaying
            } else PlayPauseButtonContentDescriptionPaused,
            style = style.iconStyle,
            modifier = Modifier
                .padding(shapePadding)
        )
    }
}

@Preview
@Composable
private fun PlayPreview(
    @PreviewParameter(BoolPreviewParameterProvider::class) isEnabled: Boolean,
) {
    PlayPauseButton(
        isEnabled = isEnabled,
        isPlaying = false,
        onClick = {},
    )
}

@Preview
@Composable
private fun PausePreview(
    @PreviewParameter(BoolPreviewParameterProvider::class) isEnabled: Boolean,
) {
    PlayPauseButton(
        isEnabled = isEnabled,
        isPlaying = true,
        onClick = {},
    )
}
