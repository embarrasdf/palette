package com.embarrasdf.palette.app.theme.semantic.color

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.embarrasdf.palette.components.color.ColorPickerDialogContent
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.core.Surface
import com.embarrasdf.palette.theme.semantic.color.toColor

@Composable
fun ColorPickerDialog(
    colorToken: ColorToken,
    onColorSelected: (Color) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            style = PaletteTheme.component.core.surface.container,
        ) {
            ColorPickerDialogContent(
                color = colorToken.toColor(),
                onColorSelected = onColorSelected,
                onDismissRequest = onDismissRequest,
                style = PaletteTheme.component.color.colorPickerDialogContent,
                modifier = Modifier
                    .padding(PaletteTheme.semantic.dimension.spacing.medium)
            )
        }
    }
}

@Preview
@Composable
fun ColorPickerDialogPreview() {
    PaletteTheme {
        ColorPickerDialogContent(
            color = ColorToken.Primary.toColor(),
            onColorSelected = {},
            onDismissRequest = {},
            style = PaletteTheme.component.color.colorPickerDialogContent,
        )
    }
}
