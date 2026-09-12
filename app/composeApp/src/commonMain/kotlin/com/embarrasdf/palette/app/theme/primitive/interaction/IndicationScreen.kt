package com.embarrasdf.palette.app.theme.primitive.interaction

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.core.ButtonDemo
import com.embarrasdf.palette.components.demo.core.ButtonDemoControl
import com.embarrasdf.palette.components.demo.core.rememberButtonDemoControl
import com.embarrasdf.palette.components.demo.core.rememberButtonDemoState
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.control.rememberThemeController
import com.embarrasdf.palette.theme.primitive.IndicationPrimitiveToken
import com.embarrasdf.palette.theme.primitive.IndicationTokenSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun IndicationScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberPrimitiveIndicationScreenState(themeState = themeController)
    val buttonDemoState = rememberButtonDemoState(
        buttonStyleInitial = PaletteTheme.component.core.button.primary.copy(
            indication = themeController.primitive.indication.getValue(state.subject).toIndication(),
        ),
    )
    val buttonDemoControl = rememberButtonDemoControl(buttonDemoState)
    val control = rememberPrimitiveIndicationScreenControl(
        state = state,
        themeController = themeController,
        buttonDemoControl = buttonDemoControl,
    )

    // Preview the selected primitive on the demo button: keep the themed style for the chosen button
    // variant and swap in the selected primitive's indication. Keyed on the token set (a stable data
    // class) and the button variant, so it re-syncs when either changes.
    val baseButtonStyle by rememberUpdatedState(
        PaletteTheme.component.core.button[buttonDemoState.style],
    )
    val tokenSet = state.tokenSet(state.subject)
    LaunchedEffect(tokenSet, buttonDemoState.style) {
        buttonDemoControl.updateStyle(baseButtonStyle.copy(indication = tokenSet.toIndication()))
    }

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
                state = buttonDemoState,
                control = buttonDemoControl,
            )
        }
    }
}

@Composable
fun rememberPrimitiveIndicationScreenState(
    themeState: ThemeState,
): PrimitiveIndicationScreenState {
    return rememberSaveable(
        themeState,
        saver = PrimitiveIndicationScreenStateSaver(themeState),
    ) {
        PrimitiveIndicationScreenState(themeState = themeState)
    }
}

@Stable
class PrimitiveIndicationScreenState(
    val themeState: ThemeState,
    subjectInitial: IndicationPrimitiveToken = IndicationPrimitiveToken.ColorSplit,
) {
    var subject by mutableStateOf(subjectInitial)

    fun tokenSet(token: IndicationPrimitiveToken): IndicationTokenSet =
        themeState.primitive.indication.getValue(token)
}

private const val subjectKey = "subject"

fun PrimitiveIndicationScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state -> mapOf(subjectKey to state.subject.name) },
    restore = { map ->
        PrimitiveIndicationScreenState(
            themeState = themeState,
            subjectInitial = (map[subjectKey] as? String)
                ?.let { IndicationPrimitiveToken.valueOf(it) }
                ?: IndicationPrimitiveToken.ColorSplit,
        )
    }
)

@Composable
fun rememberPrimitiveIndicationScreenControl(
    state: PrimitiveIndicationScreenState,
    themeController: ThemeController,
    buttonDemoControl: ButtonDemoControl,
): PrimitiveIndicationScreenControl {
    return remember(state, themeController, buttonDemoControl) {
        PrimitiveIndicationScreenControl(
            state = state,
            themeController = themeController,
            buttonDemoControl = buttonDemoControl,
        )
    }
}

@Stable
class PrimitiveIndicationScreenControl(
    val state: PrimitiveIndicationScreenState,
    val themeController: ThemeController,
    val buttonDemoControl: ButtonDemoControl,
) {
    private val indicationControl = enumControl(
        name = "Indication",
        values = { IndicationPrimitiveToken.entries },
        selectedValue = { state.subject },
        onValueChange = { state.subject = it },
    )

    private val colorInvertControl = ColorInvertIndicationControl(
        value = { tokenSet(IndicationPrimitiveToken.ColorInvert) },
        onValueChange = { updateIndication(IndicationPrimitiveToken.ColorInvert, it) },
    )

    private val colorSplitControl = ColorSplitIndicationControl(
        value = { tokenSet(IndicationPrimitiveToken.ColorSplit) },
        onValueChange = { updateIndication(IndicationPrimitiveToken.ColorSplit, it) },
    )

    private val noiseControl = NoiseIndicationControl(
        value = { tokenSet(IndicationPrimitiveToken.Noise) },
        onValueChange = { updateIndication(IndicationPrimitiveToken.Noise, it) },
    )

    private val pixelateControl = PixelateIndicationControl(
        value = { tokenSet(IndicationPrimitiveToken.Pixelate) },
        onValueChange = { updateIndication(IndicationPrimitiveToken.Pixelate, it) },
    )

    private val warpControl = WarpIndicationControl(
        value = { tokenSet(IndicationPrimitiveToken.Warp) },
        onValueChange = { updateIndication(IndicationPrimitiveToken.Warp, it) },
    )

    private val buttonControls = Control.ControlColumn(
        name = "Demo button",
        controls = { buttonDemoControl.controls },
        expandedInitial = false,
    )

    val controls: PersistentList<Control>
        get() = buildList {
            add(indicationControl)
            addAll(
                when (state.subject) {
                    IndicationPrimitiveToken.None -> emptyList()
                    IndicationPrimitiveToken.ColorInvert -> colorInvertControl.controls
                    IndicationPrimitiveToken.ColorSplit -> colorSplitControl.controls
                    IndicationPrimitiveToken.Noise -> noiseControl.controls
                    IndicationPrimitiveToken.Pixelate -> pixelateControl.controls
                    IndicationPrimitiveToken.Warp -> warpControl.controls
                }
            )
            add(buttonControls)
        }.toPersistentList()

    private inline fun <reified T : IndicationTokenSet> tokenSet(
        token: IndicationPrimitiveToken,
    ): T = state.tokenSet(token) as T

    private fun updateIndication(
        token: IndicationPrimitiveToken,
        tokenSet: IndicationTokenSet,
    ) {
        themeController.updatePrimitive {
            it.copy(indication = it.indication + (token to tokenSet))
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        IndicationScreen(
            themeController = rememberThemeController(),
            onNavigateUp = {},
        )
    }
}
