package com.embarrasdf.palette.components.media

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.components.core.Button

@Composable
fun SkipBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: SkipButtonStyle = SkipButtonStyle(),
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        style = style.buttonStyle,
        modifier = modifier
            .aspectRatio(1f)
    ) { shapePadding ->
        SkipIcon(
            style = style.iconStyle,
            modifier = Modifier
                .padding(shapePadding)
                .graphicsLayer { scaleX = -1f },
        )
    }
}

@Preview
@Composable
private fun SkipBackButtonPreview() {
    SkipBackButton(onClick = {})
}
