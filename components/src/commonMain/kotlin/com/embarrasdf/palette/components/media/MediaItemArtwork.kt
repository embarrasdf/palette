package com.embarrasdf.palette.components.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import coil3.compose.AsyncImage
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle

data class MediaItemArtworkStyle(
    val fallbackTextStyle: TextStyle = TextStyle(),
    val disabledContentAlpha: Float = 1f,
)

@Composable
fun MediaItemArtwork(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    style: MediaItemArtworkStyle = MediaItemArtworkStyle(),
    isEnabled: Boolean = true,
) {
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier
                .alpha(if (isEnabled) 1f else style.disabledContentAlpha)
        )
    } else {
        // TODO: fallback image
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .background(Color.Red)
                .clearAndSetSemantics {}
        ) {
            Text(
                "Art",
                style = style.fallbackTextStyle,
            )
        }
    }
}
