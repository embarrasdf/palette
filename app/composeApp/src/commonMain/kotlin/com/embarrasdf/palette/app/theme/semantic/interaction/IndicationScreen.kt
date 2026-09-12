package com.embarrasdf.palette.app.theme.semantic.interaction

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.core.ButtonDemo
import com.embarrasdf.palette.components.demo.core.ButtonDemoControl
import com.embarrasdf.palette.components.demo.core.ButtonDemoState
import com.embarrasdf.palette.components.demo.core.ButtonDemoStateSaver
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.primitive.IndicationPrimitiveToken
import com.embarrasdf.palette.theme.semantic.interaction.IndicationToken
import com.embarrasdf.palette.theme.semantic.interaction.InteractionScheme
import com.embarrasdf.palette.theme.semantic.interaction.copy
import com.embarrasdf.palette.theme.semantic.interaction.primitiveToken
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun IndicationScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberIndicationScreenState(themeState = themeController)
    val control = rememberIndicationScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Indication",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        Demo(
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            this@Demo.ButtonDemo(
                state = state.buttonDemoState,
                control = control.buttonDemoControl,
            )
        }
    }
}

@Composable
fun rememberIndicationScreenState(
    themeState: ThemeState,
): IndicationScreenState {
    return rememberSaveable(
        themeState,
        saver = IndicationScreenStateSaver(themeState),
    ) {
        IndicationScreenState(
            themeState = themeState,
            buttonDemoStateInitial = ButtonDemoState(),
        )
    }
}

@Stable
class IndicationScreenState(
    val themeState: ThemeState,
    buttonDemoStateInitial: ButtonDemoState,
) {
    val interactionScheme: InteractionScheme
        get() = themeState.semantic.interaction

    var buttonDemoState by mutableStateOf(buttonDemoStateInitial)
        internal set
}

private const val buttonDemoStateKey = "buttonDemoState"

fun IndicationScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            buttonDemoStateKey to save(state.buttonDemoState, ButtonDemoStateSaver, this)
        )
    },
    restore = { map ->
        IndicationScreenState(
            themeState = themeState,
            buttonDemoStateInitial = restore(map[buttonDemoStateKey], ButtonDemoStateSaver)!!,
        )
    }
)

@Composable
fun rememberIndicationScreenControl(
    state: IndicationScreenState,
    themeController: ThemeController,
): IndicationScreenControl {
    return remember(state, themeController) {
        IndicationScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class IndicationScreenControl(
    val state: IndicationScreenState,
    val themeController: ThemeController,
) {
    val indicationControl = enumControl(
        name = "Indication",
        values = { IndicationPrimitiveToken.entries },
        selectedValue = { IndicationToken.Default.primitiveToken(state.interactionScheme) },
        onValueChange = { primitiveToken ->
            themeController.updateSemantic {
                it.copy(interaction = it.interaction.copy(IndicationToken.Default, primitiveToken))
            }
        },
    )

    val buttonDemoControl = ButtonDemoControl(state = state.buttonDemoState)

    val controls: PersistentList<Control> = persistentListOf(
        indicationControl,
        Control.ControlColumn(
            name = "Demo button",
            indent = true,
            controls = { buttonDemoControl.controls },
            expandedInitial = false,
        )
    )
}
