package com.embarrasdf.palette.components.demo.color

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.color.ColorPicker
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.ColorSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ColorPickerDemo(
    state: ColorPickerDemoState = rememberColorPickerDemoState(),
    control: ColorPickerDemoControl = rememberColorPickerDemoControl(state),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        ColorPickerDemo(
            state = state,
            control = control,
        )
    }
}

@Composable
fun BoxWithConstraintsScope.ColorPickerDemo(
    modifier: Modifier = Modifier,
    state: ColorPickerDemoState = rememberColorPickerDemoState(),
    control: ColorPickerDemoControl = rememberColorPickerDemoControl(state),
) {
    ColorPicker(
        style = PaletteTheme.component.color.colorPicker.copy(
            spacing = state.spacing,
        ),
        color = state.color,
        onColorChange = control::onColorChange,
        modifier = modifier
            .fillMaxWidth()
            .align(Alignment.Center)
            .padding(PaletteTheme.semantic.dimension.spacing.medium)
    )
}

@Composable
fun rememberColorPickerDemoState(
    colorInitial: Color = PaletteTheme.semantic.color.primary,
    spacingInitial: Dp = PaletteTheme.component.color.colorPicker.spacing,
): ColorPickerDemoState = rememberSaveable(
    saver = ColorPickerDemoStateSaver,
) {
    ColorPickerDemoState(
        colorInitial = colorInitial,
        spacingInitial = spacingInitial,
    )
}

@Stable
class ColorPickerDemoState(
    colorInitial: Color,
    spacingInitial: Dp = 16.dp,
) {
    var color by mutableStateOf(colorInitial)
        internal set

    var spacing by mutableStateOf(spacingInitial)
        internal set
}

private const val colorKey = "color"
private const val spacingKey = "spacing"

val ColorPickerDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            colorKey to save(value.color, ColorSaver, this),
            spacingKey to value.spacing.value,
        )
    },
    restore = { map ->
        ColorPickerDemoState(
            colorInitial = restore(map[colorKey], ColorSaver)!!,
            spacingInitial = (map[spacingKey] as Float).dp,
        )
    },
)

@Composable
fun rememberColorPickerDemoControl(
    state: ColorPickerDemoState,
): ColorPickerDemoControl = remember(state) { ColorPickerDemoControl(state) }

@Stable
class ColorPickerDemoControl(
    val state: ColorPickerDemoState,
) {
    val spacingControl = Control.Slider(
        name = "Spacing",
        value = { state.spacing.value },
        onValueChange = { state.spacing = it.dp },
        valueRange = { 0f..48f },
    )

    val styleControls = Control.ControlColumn(
        name = "Style",
        indent = true,
        expandedInitial = true,
        controls = {
            persistentListOf(
                spacingControl,
            )
        },
    )

    val controls = persistentListOf<Control>(
        styleControls,
    )

    fun onColorChange(color: Color) {
        state.color = color
    }
}
