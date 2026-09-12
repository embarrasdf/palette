package com.embarrasdf.palette.components.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.ChevronDirection
import com.embarrasdf.palette.components.core.ChevronIcon
import com.embarrasdf.palette.components.core.IconStyle
import com.embarrasdf.palette.components.core.Sizing
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.height

data class BackNavigationButtonStyle(
    val buttonStyle: ButtonStyle = ButtonStyle(contentPadding = PaddingValues(0.dp)),
    val iconStyle: IconStyle = IconStyle(size = Sizing.Fixed(16.dp)),
    val size: Sizing = Sizing.Fixed(48.dp),
)

@Composable
fun BackNavigationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: BackNavigationButtonStyle = BackNavigationButtonStyle(),
) {
    Button(
        onClick = onClick,
        style = style.buttonStyle,
        modifier = modifier
            .height(style.size)
            .aspectRatio(1f),
    ) {
        ChevronIcon(
            direction = ChevronDirection.Left,
            style = style.iconStyle,
        )
    }
}

@Preview
@Composable
private fun BackNavigationButtonPreview() {
    Surface {
        BackNavigationButton(
            onClick = {},
        )
    }
}
