package com.embarrasdf.palette.components.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.core.TextDemo
import com.embarrasdf.palette.components.demo.core.TextDemoControl
import com.embarrasdf.palette.components.demo.core.TextDemoState
import com.embarrasdf.palette.components.demo.core.TextDemoStateSaver
import com.embarrasdf.palette.components.demo.core.TextFieldDemo
import com.embarrasdf.palette.components.demo.core.TextFieldDemoControl
import com.embarrasdf.palette.components.demo.core.TextFieldDemoState
import com.embarrasdf.palette.components.demo.core.TextFieldDemoStateSaver
import com.embarrasdf.palette.components.demo.geometry.CartesianGridScaleState
import com.embarrasdf.palette.components.demo.geometry.CircleDemo
import com.embarrasdf.palette.components.demo.geometry.CircleDemoControl
import com.embarrasdf.palette.components.demo.geometry.CircleDemoState
import com.embarrasdf.palette.components.demo.geometry.CurveStitchDemo
import com.embarrasdf.palette.components.demo.geometry.CurveStitchDemoControl
import com.embarrasdf.palette.components.demo.geometry.CurveStitchDemoState
import com.embarrasdf.palette.components.demo.geometry.CurveStitchDemoStateSaver
import com.embarrasdf.palette.components.demo.geometry.GridDemo
import com.embarrasdf.palette.components.demo.geometry.GridDemoControl
import com.embarrasdf.palette.components.demo.geometry.GridDemoState
import com.embarrasdf.palette.components.demo.geometry.GridDemoStateSaver
import com.embarrasdf.palette.components.demo.geometry.SphereDemo
import com.embarrasdf.palette.components.demo.geometry.SphereDemoControl
import com.embarrasdf.palette.components.demo.geometry.SphereDemoState
import com.embarrasdf.palette.components.demo.geometry.SphereDemoStateSaver
import com.embarrasdf.palette.components.demo.geometry.circleDemoStateSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.requireRestore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.toPersistentList

enum class ComponentDemoType {
    Circle,
    CurveStitch,
    Grid,
    Sphere,
    Text,
    TextField,
}

@Composable
fun ComponentDemo(
    modifier: Modifier = Modifier,
    state: ComponentDemoState = rememberComponentDemoState(),
    control: ComponentDemoControl = rememberComponentDemoControl(state = state),
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize(),
    ) {
        ComponentDemo(
            state = state,
            control = control,
        )
    }
}

@Composable
fun DemoScope.ComponentDemo(
    modifier: Modifier = Modifier,
    state: ComponentDemoState = rememberComponentDemoState(),
    control: ComponentDemoControl = rememberComponentDemoControl(state = state),
) {
    when (state.demoType) {
        ComponentDemoType.Circle -> CircleDemo(
            state = state.circleDemoState,
            modifier = modifier
                .fillMaxSize(),
        )
        ComponentDemoType.CurveStitch -> CurveStitchDemo(
            state = state.curveStitchDemoState,
            enablePointerInput = false,
            modifier = modifier
                .fillMaxSize(),
        )
        ComponentDemoType.Grid -> GridDemo(
            state = state.gridDemoState,
            modifier = modifier
                .fillMaxSize(),
        )
        ComponentDemoType.Sphere -> SphereDemo(
            state = state.sphereDemoState,
            modifier = modifier
                .fillMaxSize(),
        )
        ComponentDemoType.Text -> TextDemo(
            state = state.textDemoState,
            control = control.textDemoControl,
            modifier = modifier,
        )
        ComponentDemoType.TextField -> TextFieldDemo(
            state = state.textFieldDemoState,
            control = control.textFieldDemoControl,
            modifier = modifier,
        )
    }
}

@Composable
fun rememberComponentDemoState(
    componentDemoTypeInitial: ComponentDemoType = ComponentDemoType.Circle,
): ComponentDemoState {
    val density = LocalDensity.current
    val color = PaletteTheme.semantic.color.primary
    return rememberSaveable(
        density,
        color,
        saver = componentDemoStateSaver(
            density = density,
            color = color,
        ),
    ) {
        ComponentDemoState(
            density = density,
            color = color,
            componentDemoTypeInitial = componentDemoTypeInitial,
        )
    }
}

@Stable
class ComponentDemoState(
    density: Density,
    color: Color,
    componentDemoTypeInitial: ComponentDemoType = ComponentDemoType.Circle,
    circleDemoStateInitial: CircleDemoState = CircleDemoState(
        density = density,
        colorInitial = color,
    ),
    curveStitchDemoStateInitial: CurveStitchDemoState = CurveStitchDemoState(),
    gridDemoStateInitial: GridDemoState = GridDemoState(
        density = density,
        color = color,
        xGridScaleStateInitial = CartesianGridScaleState(
            gridSpacingInitial = 28.dp,
        ),
        yGridScaleStateInitial = CartesianGridScaleState(
            gridSpacingInitial = 28.dp,
        ),
    ),
    sphereDemoStateInitial: SphereDemoState = SphereDemoState(
        strokeColor = color,
        outlineStrokeColor = color,
    ),
    textDemoStateInitial: TextDemoState = TextDemoState(),
    textFieldDemoStateInitial: TextFieldDemoState = TextFieldDemoState(
        initialText = "Hello world",
    ),
) {
    var demoType by mutableStateOf(componentDemoTypeInitial)
        internal set

    val circleDemoState = circleDemoStateInitial

    val curveStitchDemoState = curveStitchDemoStateInitial

    val gridDemoState = gridDemoStateInitial

    val sphereDemoState = sphereDemoStateInitial

    val textDemoState = textDemoStateInitial

    val textFieldDemoState = textFieldDemoStateInitial
}

private const val DemoTypeKey = "demoType"
private const val CircleDemoStateKey = "circleDemoState"
private const val CurveStitchDemoStateKey = "curveStitchDemoState"
private const val GridDemoStateKey = "gridDemoState"
private const val SphereDemoStateKey = "sphereDemoState"
private const val TextDemoStateKey = "textDemoState"
private const val TextFieldDemoStateKey = "textFieldDemoState"

fun componentDemoStateSaver(
    density: Density,
    color: Color,
) = mapSaverSafe(
    save = { value ->
        mapOf(
            DemoTypeKey to value.demoType,
            CircleDemoStateKey to save(
                value = value.circleDemoState,
                saver = circleDemoStateSaver(density = density, color = color),
                scope = this,
            ),
            CurveStitchDemoStateKey to save(
                value = value.curveStitchDemoState,
                saver = CurveStitchDemoStateSaver,
                scope = this,
            ),
            GridDemoStateKey to save(value = value.gridDemoState, saver = GridDemoStateSaver, scope = this),
            SphereDemoStateKey to save(value = value.sphereDemoState, saver = SphereDemoStateSaver, scope = this),
            TextDemoStateKey to save(value = value.textDemoState, saver = TextDemoStateSaver, scope = this),
            TextFieldDemoStateKey to save(
                value = value.textFieldDemoState,
                saver = TextFieldDemoStateSaver,
                scope = this,
            ),
        )
    },
    restore = { map ->
        ComponentDemoState(
            density = density,
            color = color,
            componentDemoTypeInitial = map[DemoTypeKey] as ComponentDemoType,
            circleDemoStateInitial = map.requireRestore(
                key = CircleDemoStateKey,
                saver = circleDemoStateSaver(density = density, color = color),
            ),
            curveStitchDemoStateInitial = map.requireRestore(
                key = CurveStitchDemoStateKey,
                saver = CurveStitchDemoStateSaver,
            ),
            gridDemoStateInitial = map.requireRestore(key = GridDemoStateKey, saver = GridDemoStateSaver),
            sphereDemoStateInitial = map.requireRestore(key = SphereDemoStateKey, saver = SphereDemoStateSaver),
            textDemoStateInitial = map.requireRestore(key = TextDemoStateKey, saver = TextDemoStateSaver),
            textFieldDemoStateInitial = map.requireRestore(
                key = TextFieldDemoStateKey,
                saver = TextFieldDemoStateSaver,
            ),
        )
    },
)

@Composable
fun rememberComponentDemoControl(state: ComponentDemoState = rememberComponentDemoState()): ComponentDemoControl {
    return remember(state) { ComponentDemoControl(state) }
}

@Stable
class ComponentDemoControl(
    val state: ComponentDemoState,
) {
    val demoTypeControl = enumControl(
        name = "Component",
        values = { ComponentDemoType.entries },
        selectedValue = { state.demoType },
        onValueChange = { state.demoType = it },
    )

    val circleDemoControl = CircleDemoControl(state = state.circleDemoState)
    val circleDemoControls = Control.ControlColumn(
        name = "Circle",
        controls = { circleDemoControl.controls },
        expandedInitial = true,
    )

    val curveStitchDemoControl = CurveStitchDemoControl(state = state.curveStitchDemoState)
    val curveStitchDemoControls = Control.ControlColumn(
        name = "Curve Stitch",
        controls = { curveStitchDemoControl.controls },
        expandedInitial = true,
    )

    val gridDemoControl = GridDemoControl(state = state.gridDemoState)
    val gridDemoControls = Control.ControlColumn(
        name = "Grid",
        controls = { gridDemoControl.controls },
        expandedInitial = true,
    )

    val sphereDemoControl = SphereDemoControl(state = state.sphereDemoState)
    val sphereDemoControls = Control.ControlColumn(
        name = "Sphere",
        controls = { sphereDemoControl.controls },
        expandedInitial = true,
    )

    val textDemoControl = TextDemoControl(state = state.textDemoState)
    val textDemoControls = Control.ControlColumn(
        name = "Text",
        controls = { textDemoControl.controls },
        expandedInitial = true,
    )

    val textFieldDemoControl = TextFieldDemoControl(state = state.textFieldDemoState)
    val textFieldDemoControls = Control.ControlColumn(
        name = "Text Field",
        controls = { textFieldDemoControl.controls },
        expandedInitial = true,
    )

    val controls
        get() = buildList {
            add(demoTypeControl)
            when (state.demoType) {
                ComponentDemoType.Circle -> add(circleDemoControls)
                ComponentDemoType.CurveStitch -> add(curveStitchDemoControls)
                ComponentDemoType.Grid -> add(gridDemoControls)
                ComponentDemoType.Sphere -> add(sphereDemoControls)
                ComponentDemoType.Text -> add(textDemoControls)
                ComponentDemoType.TextField -> add(textFieldDemoControls)
            }
        }.toPersistentList()
}
