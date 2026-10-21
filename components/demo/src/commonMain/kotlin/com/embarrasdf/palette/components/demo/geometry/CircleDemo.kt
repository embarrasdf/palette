package com.embarrasdf.palette.components.demo.geometry

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.state.DemoSavers
import com.embarrasdf.palette.components.demo.state.DemoState
import com.embarrasdf.palette.components.demo.state.demoStateSaver
import com.embarrasdf.palette.components.demo.state.rememberDemoState
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
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
            .background(PaletteTheme.semantic.color.surface),
    ) {
        drawCircle(color = state.color, style = state.drawStyle, radius = size.minDimension / 4f)
    }
}

@Composable
fun rememberCircleDemoState(color: Color = PaletteTheme.semantic.color.primary): CircleDemoState {
    val density = LocalDensity.current
    return rememberDemoState(density) {
        CircleDemoState(
            density = density,
            colorInitial = color,
        )
    }
}

@Stable
class CircleDemoState(
    val density: Density,
    colorInitial: Color,
) : DemoState() {
    var color by saveable(colorInitial)
        internal set

    var drawStyle by saveable(initial = Fill, saver = DemoSavers.DrawStyle)
        internal set

    var strokeWidth by saveable(2.dp)
        internal set

    val strokeWidthPx
        get() = with(density) { strokeWidth.toPx() }
}

fun circleDemoStateSaver(
    density: Density,
    color: Color,
) = demoStateSaver { CircleDemoState(density = density, colorInitial = color) }

@Composable
fun rememberCircleDemoControl(state: CircleDemoState = rememberCircleDemoState()): CircleDemoControl {
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
