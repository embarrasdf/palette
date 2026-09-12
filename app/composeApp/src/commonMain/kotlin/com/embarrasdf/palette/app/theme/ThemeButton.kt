package com.embarrasdf.palette.app.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun ThemeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        style = PaletteTheme.component.core.button.tertiary,
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            "Theme",
            style = PaletteTheme.component.core.text.labelSmall,
        )
    }
}
