package com.embarrasdf.palette.components.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.layout.dialog.ConfirmCancelButtonRow
import com.embarrasdf.palette.components.layout.dialog.ConfirmCancelButtonRowStyle

data class ColorPickerDialogContentStyle(
    val colorPickerStyle: ColorPickerStyle = ColorPickerStyle(),
    val confirmCancelButtonRowStyle: ConfirmCancelButtonRowStyle = ConfirmCancelButtonRowStyle(),
    val spacing: Dp = 16.dp,
    val padding: PaddingValues = PaddingValues(24.dp),
    val buttonRowSpacing: Dp = 24.dp,
)

@Composable
fun ColorPickerDialogContent(
    color: Color,
    onColorSelected: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: ColorPickerDialogContentStyle = ColorPickerDialogContentStyle(),
) {
    var currentColor by remember { mutableStateOf(color) }

    Column(
        verticalArrangement = Arrangement.spacedBy(style.spacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(style.padding)
    ) {
        ColorPicker(
            style = style.colorPickerStyle,
            color = currentColor,
            onColorChange = { currentColor = it },
            modifier = Modifier
                .weight(1f, fill = false)
        )
        ConfirmCancelButtonRow(
            style = style.confirmCancelButtonRowStyle,
            onConfirm = {
                onColorSelected(currentColor)
                onDismissRequest()
            },
            onDismiss = onDismissRequest,
            modifier = Modifier
                .padding(top = style.buttonRowSpacing)
        )
    }
}
