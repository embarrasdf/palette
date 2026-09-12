package com.embarrasdf.palette.components.demo.control

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import com.embarrasdf.palette.components.color.ColorDisplay
import com.embarrasdf.palette.components.color.ColorDisplayStyle
import com.embarrasdf.palette.components.color.ColorPickerDialogContent
import com.embarrasdf.palette.components.color.ColorPickerDialogContentStyle
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import androidx.compose.ui.unit.Dp

data class ColorControlStyle(
    val labelStyle: TextStyle = TextStyle(),
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val colorDisplayStyle: ColorDisplayStyle = ColorDisplayStyle(),
    val colorPickerDialogContentStyle: ColorPickerDialogContentStyle = ColorPickerDialogContentStyle(),
    val surfaceStyle: SurfaceStyle = SurfaceStyle(),
    val spacing: Dp = 16.dp,
    val contentSpacing: Dp = 8.dp,
)

@Composable
fun ColorControl(
    control: Control.Color,
    modifier: Modifier = Modifier,
    style: ColorControlStyle = ColorControlStyle(),
) {
    val color by rememberUpdatedState(control.color())
    var showDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(style.spacing),
        modifier = modifier,
    ) {
        Text(
            text = control.name,
            style = style.labelStyle,
        )
        Button(
            style = style.buttonStyle,
            onClick = { showDialog = true },
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(style.contentSpacing),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(IntrinsicSize.Max)
            ) {
                ColorDisplay(
                    color = color,
                    style = style.colorDisplayStyle,
                    modifier = Modifier
                        .fillMaxHeight()
                )
                Text(color.toString(), style = style.labelStyle)
            }
        }
    }

    if (showDialog) {
        ColorPickerDialog(
            color = color,
            onColorSelected = control.onColorChange,
            onDismissRequest = { showDialog = false },
            style = style,
        )
    }
}

@Composable
private fun ColorPickerDialog(
    color: Color,
    onColorSelected: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    style: ColorControlStyle,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(style = style.surfaceStyle) {
            ColorPickerDialogContent(
                color = color,
                onColorSelected = onColorSelected,
                onDismissRequest = onDismissRequest,
                style = style.colorPickerDialogContentStyle,
                modifier = Modifier
                    .padding(style.spacing)
            )
        }
    }
}
