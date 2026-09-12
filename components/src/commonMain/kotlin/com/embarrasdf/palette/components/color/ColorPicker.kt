package com.embarrasdf.palette.components.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Slider
import com.embarrasdf.palette.components.core.SliderStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle

data class ColorPickerControlsStyle(
    val labelStyle: TextStyle = TextStyle(),
    val sliderStyle: SliderStyle = SliderStyle(),
    val spacing: Dp = 8.dp,
)

data class ColorPickerStyle(
    val spacing: Dp = 16.dp,
    val colorDisplayStyle: ColorDisplayStyle = ColorDisplayStyle(),
    val controlsStyle: ColorPickerControlsStyle = ColorPickerControlsStyle(),
)

@Composable
fun ColorPicker(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    style: ColorPickerStyle = ColorPickerStyle(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(style.spacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        ColorDisplay(
            color = color,
            style = style.colorDisplayStyle,
            modifier = Modifier
                .weight(1f, fill = false)
                .semantics {
                    contentDescription = "Selected color"
                }
        )
        ColorPickerControls(
            color = color,
            onColorChange = onColorChange,
            style = style.controlsStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ColorPickerControls(
    color: Color,
    onColorChange: (Color) -> Unit,
    colorSpace: ColorSpace = color.colorSpace,
    modifier: Modifier = Modifier,
    style: ColorPickerControlsStyle = ColorPickerControlsStyle(),
) {
    data class ColorComponentInfo(
        val name: String,
        val value: Float,
        val onValueChange: (Float) -> Color,
        val valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    )
    val colorComponents = listOf(
        ColorComponentInfo(
            name = "R",
            value = color.red,
            onValueChange = { color.copy(red = it) },
            valueRange = colorSpace.getMinValue(0) .. colorSpace.getMaxValue(0)
        ),
        ColorComponentInfo(
            name = "G",
            value = color.green,
            onValueChange = { color.copy(green = it) },
            valueRange = colorSpace.getMinValue(1) .. colorSpace.getMaxValue(1)
        ),
        ColorComponentInfo(
            name = "B",
            value = color.blue,
            onValueChange = { color.copy(blue = it) },
            valueRange = colorSpace.getMinValue(2) .. colorSpace.getMaxValue(2)
        ),
        ColorComponentInfo(
            name = "A",
            value = color.alpha,
            onValueChange = { color.copy(alpha = it) },
            valueRange = colorSpace.getMinValue(3) .. colorSpace.getMaxValue(3)
        ),
    )

    Column(
        modifier = modifier,
    ) {
        for (colorComponent in colorComponents) {
            ColorSlider(
                value = colorComponent.value,
                onValueChange = { onColorChange(colorComponent.onValueChange(it)) },
                label = colorComponent.name,
                style = style,
                valueRange = colorComponent.valueRange,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ColorSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    style: ColorPickerControlsStyle = ColorPickerControlsStyle(),
    valueRange: ClosedFloatingPointRange<Float> = 0f..255f,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(style.spacing),
        modifier = modifier,
    ) {
        Text(text = label, style = style.labelStyle)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            style = style.sliderStyle,
            modifier = Modifier.semantics {
                contentDescription = label
            }
        )
    }
}

@Preview
@Composable
private fun ColorPickerPreview() {
    var color = Color(0xFF6200EE)
    ColorPicker(
        color = color,
        onColorChange = { color = it},
        modifier = Modifier
    )
}
