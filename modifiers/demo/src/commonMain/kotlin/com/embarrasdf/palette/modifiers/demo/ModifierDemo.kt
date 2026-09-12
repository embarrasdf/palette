package com.embarrasdf.palette.modifiers.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.embarrasdf.palette.components.demo.ComponentDemo
import com.embarrasdf.palette.components.demo.ComponentDemoControl
import com.embarrasdf.palette.components.demo.ComponentDemoState
import com.embarrasdf.palette.components.demo.ComponentDemoType
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ModifierDemo(
    demoModifier: Modifier,
    controls: PersistentList<Control>,
    modifier: Modifier = Modifier,
    state: ModifierDemoState = rememberModifierDemoState(),
    control: ModifierDemoControl = rememberModifierDemoControl(state = state, modifierControls = controls),
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        ComponentDemo(
            state = state.componentDemoState,
            control = control.componentDemoControl,
            modifier = demoModifier
                .align(Alignment.Center),
        )
    }
}


@Composable
fun rememberModifierDemoState(
    componentDemoTypeInitial: ComponentDemoType = ComponentDemoType.Circle,
): ModifierDemoState {
    val density = LocalDensity.current
    val color = PaletteTheme.semantic.color.primary
    return rememberSaveable(
        density,
        color,
        saver = ModifierDemoStateSaver(
            density = density,
            color = color,
        ),
    ) {
        ModifierDemoState(
            density = density,
            color = color,
            componentDemoTypeInitial = componentDemoTypeInitial,
        )
    }
}

@Stable
class ModifierDemoState(
    density: Density,
    color: Color,
    componentDemoTypeInitial: ComponentDemoType = ComponentDemoType.Circle,

) {
    val componentDemoState = ComponentDemoState(
        density = density,
        color = color,
        componentDemoTypeInitial = componentDemoTypeInitial,
    )
}

fun ModifierDemoStateSaver(
    density: Density,
    color: Color,
) = mapSaverSafe(
    save = { value ->
        mapOf()
    },
    restore = { map ->
        ModifierDemoState(
            density = density,
            color = color,
        )
    },
)

@Composable
fun rememberModifierDemoControl(
    modifierControls: PersistentList<Control> = persistentListOf(),
    state: ModifierDemoState = rememberModifierDemoState(),
): ModifierDemoControl {
    return remember(state) { ModifierDemoControl(modifierControls, state) }
}

@Stable
class ModifierDemoControl(
    val modifierControls: PersistentList<Control>,
    val state: ModifierDemoState,
) {
    val componentDemoControl = ComponentDemoControl(
        state = state.componentDemoState,
    )
    val componentDemoControls = Control.ControlColumn(
        name = "Component",
        controls = { componentDemoControl.controls },
        expandedInitial = false,
    )

    val controls
        get() = persistentListOf(
            *modifierControls.toTypedArray(),
            componentDemoControls,
        )
}
