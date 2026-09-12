package com.embarrasdf.palette.components.demo.geometry

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.ColorSaver
import com.embarrasdf.palette.components.util.DrawStyleSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CircleDemo(
    modifier: Modifier = Modifier,
    state: CircleDemoState = rememberCircleDemoState(),
    control: CircleDemoControl = rememberCircleDemoControl(state),
) {
    Demo(
        controls = control.controls,
        modifier = modifier.fillMaxSize(),
    ) {
        CircleDemo(
            state = state,
            modifier = modifier,
        )
    }
}

@Composable
fun DemoScope.CircleDemo(
    modifier: Modifier = Modifier,
    state: CircleDemoState = rememberCircleDemoState(),
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(PaletteTheme.semantic.color.surface)
    ) {
        drawCircle(state.color, style = state.drawStyle, radius = size.minDimension / 4f)
    }
}

@Composable
fun rememberCircleDemoState(
    color: Color = PaletteTheme.semantic.color.primary,
): CircleDemoState {
    val density = LocalDensity.current
    return rememberSaveable(
        density,
        saver = CircleDemoStateSaver(
            density = density,
        )
    ) {
        CircleDemoState(
            colorInitial = color,
            density = density,
        )
    }
}

@Stable
class CircleDemoState(
    val density: Density,
    colorInitial: Color,
    drawStyleInitial: DrawStyle = Fill,
    strokeWidthInitial: Dp = 2.dp,
) {
    var color by mutableStateOf(colorInitial)
        internal set

    var drawStyle by mutableStateOf(drawStyleInitial)
        internal set

    var strokeWidth by mutableStateOf(strokeWidthInitial)
        internal set

    val strokeWidthPx
        get() = with(density) { strokeWidth.toPx() }
}

private const val colorKey = "color"
private const val drawStyleKey = "drawStyle"
private const val strokeWidthKey = "strokeWidth"

fun CircleDemoStateSaver(
    density: Density,
) = mapSaverSafe(
    save = { value ->
        mapOf(
            colorKey to save(value.color, ColorSaver, this),
            drawStyleKey to save(value.drawStyle, DrawStyleSaver, this),
            strokeWidthKey to value.strokeWidth.value,
        )
    },
    restore = { map ->
        CircleDemoState(
            density = density,
            colorInitial = restore(map[colorKey], ColorSaver)!!,
            drawStyleInitial = restore(map[drawStyleKey], DrawStyleSaver)!!,
            strokeWidthInitial = (map[strokeWidthKey] as Float).dp,
        )
    },
)

@Composable
fun rememberCircleDemoControl(
    state: CircleDemoState = rememberCircleDemoState(),
): CircleDemoControl {
    return remember(state) { CircleDemoControl(state) }
}

@Stable
class CircleDemoControl(
    val state: CircleDemoState,
) {
    val colorControl = Control.Color(
        name = "Color",
        color = { state.color },
        onColorChange = { state.color = it },
    )

    enum class DemoDrawStyle {
        Fill,
        Stroke,
    }

    val drawStyleControl = enumControl(
        name = "Draw Style",
        values = { DemoDrawStyle.entries },
        selectedValue = {
            when (state.drawStyle) {
                is Fill -> DemoDrawStyle.Fill
                is Stroke -> DemoDrawStyle.Stroke
                else -> DemoDrawStyle.Fill
            }
        },
        onValueChange = {
            state.drawStyle = when (it) {
                DemoDrawStyle.Fill -> Fill
                DemoDrawStyle.Stroke -> Stroke(width = state.strokeWidthPx)
            }
        },
    )

    val strokeWidthControl = Control.Slider(
        name = "Stroke Width",
        value = { state.strokeWidth.value },
        onValueChange = {
            state.strokeWidth = it.dp
            if (state.drawStyle is Stroke) {
                state.drawStyle = Stroke(width = state.strokeWidthPx)
            }
        },
        valueRange = { 1f..100f },
    )

    val controls
        get() = if (state.drawStyle is Stroke) {
            persistentListOf(
                colorControl,
                drawStyleControl,
                strokeWidthControl,
            )
        } else {
            persistentListOf(
                colorControl,
                drawStyleControl,
            )
        }
}

@Preview
@Composable
private fun Preview() {
    CircleDemo()
}
