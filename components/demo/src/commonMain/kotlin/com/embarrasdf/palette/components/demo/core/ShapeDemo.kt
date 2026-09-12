package com.embarrasdf.palette.components.demo.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Shape
import com.embarrasdf.palette.components.core.ShapeType
import com.embarrasdf.palette.components.core.toComposeShape
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import kotlinx.collections.immutable.persistentListOf

fun defaultShapeDemoSize(type: ShapeType): DpSize = when (type) {
    ShapeType.Rectangle -> DpSize(width = 176.dp, height = 112.dp)
    ShapeType.Circle -> DpSize(width = 128.dp, height = 128.dp)
    ShapeType.Triangle -> DpSize(width = 128.dp, height = 128.dp)
    ShapeType.Diamond -> DpSize(width = 128.dp, height = 128.dp)
}

@Composable
fun ShapeDemo(
    shape: Shape,
    modifier: Modifier = Modifier,
    color: Color = PaletteTheme.semantic.color.onSurface,
    state: ShapeDemoState = rememberShapeDemoState(shape),
    control: ShapeDemoControl = rememberShapeDemoControl(state),
) {
    Demo(
        controls = control.controls,
        modifier = modifier,
    ) {
        ShapeDemo(
            shape = shape,
            color = color,
            state = state,
        )
    }
}

@Composable
fun DemoScope.ShapeDemo(
    shape: Shape,
    modifier: Modifier = Modifier,
    color: Color = PaletteTheme.semantic.color.onSurface,
    state: ShapeDemoState = rememberShapeDemoState(shape),
) {
    Box(
        modifier = modifier
            .align(Alignment.Center)
            .size(width = state.width, height = state.height)
            .background(
                color = color,
                shape = shape.toComposeShape(),
            )
    )
}

@Composable
fun rememberShapeDemoState(
    shape: Shape,
): ShapeDemoState {
    val defaultSize = defaultShapeDemoSize(shape.type)
    return rememberSaveable(
        shape.type,
        saver = ShapeDemoStateSaver,
    ) {
        ShapeDemoState(
            widthInitial = defaultSize.width,
            heightInitial = defaultSize.height,
        )
    }
}

@Stable
class ShapeDemoState(
    widthInitial: Dp,
    heightInitial: Dp,
) {
    var width by mutableStateOf(widthInitial)
        internal set

    var height by mutableStateOf(heightInitial)
        internal set
}

private const val widthKey = "width"
private const val heightKey = "height"

private val ShapeDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            widthKey to value.width.value,
            heightKey to value.height.value,
        )
    },
    restore = { map ->
        ShapeDemoState(
            widthInitial = (map[widthKey] as Float).dp,
            heightInitial = (map[heightKey] as Float).dp,
        )
    },
)

@Composable
fun rememberShapeDemoControl(
    state: ShapeDemoState,
): ShapeDemoControl {
    return remember(state) { ShapeDemoControl(state) }
}

@Stable
class ShapeDemoControl(
    val state: ShapeDemoState,
) {
    val widthControl = Control.Slider(
        name = "Width",
        value = { state.width.value },
        onValueChange = { state.width = it.dp },
        valueRange = { 16f..256f },
        stepIncrement = 8f,
    )

    val heightControl = Control.Slider(
        name = "Height",
        value = { state.height.value },
        onValueChange = { state.height = it.dp },
        valueRange = { 16f..256f },
        stepIncrement = 8f,
    )

    val controls = persistentListOf<Control>(
        Control.ControlColumn(
            name = "Size",
            controls = { persistentListOf(widthControl, heightControl) },
        ),
    )
}
