package com.embarrasdf.palette.app.theme.component.core

import com.embarrasdf.palette.theme.PaletteTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.core.TextAlign
import com.embarrasdf.palette.components.demo.core.TextDemo
import com.embarrasdf.palette.components.demo.core.TextDemoControl
import com.embarrasdf.palette.components.demo.core.TextDemoState
import com.embarrasdf.palette.components.demo.core.TextDemoStateSaver
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.theme.components.demo.control.spacingTokenPaddingControls
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.component.core.BorderStyleToken
import com.embarrasdf.palette.theme.component.core.ButtonStyleToken
import com.embarrasdf.palette.theme.component.core.ButtonStyleTokenSet
import com.embarrasdf.palette.theme.semantic.color.toColor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ButtonStyleScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberButtonStyleScreenState(themeState = themeController)
    val control = rememberButtonStyleScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Button style",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = ButtonStyleToken.entries,
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { style ->
            val textDemoState = state.textDemoState(style)
            val contentColor = when (style) {
                ButtonStyleToken.Primary -> ColorToken.OnPrimary
                ButtonStyleToken.Secondary -> ColorToken.Secondary
                ButtonStyleToken.Tertiary -> ColorToken.Primary
            }.toColor()
            LaunchedEffect(textDemoState, contentColor) {
                textDemoState.textStyleDemoState.color = contentColor
            }
            Button(
                style = PaletteTheme.component.core.button[style],
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                this@DemoList.TextDemo(
                    state = textDemoState,
                    control = control.textDemoControl(style),
                )
            }
        }
    }
}

@Composable
fun rememberButtonStyleScreenState(
    themeState: ThemeState,
    textDemoStatesInitial: Map<ButtonStyleToken, TextDemoState> =
        ButtonStyleToken.entries.associateWith {
            TextDemoState(
                textAlignInitial = TextAlign.Center,
                autoSizeInitial = true,
            )
        },
): ButtonStyleScreenState {
    return rememberSaveable(
        themeState,
        saver = ButtonStyleScreenStateSaver(themeState),
    ) {
        ButtonStyleScreenState(
            themeState = themeState,
            textDemoStates = textDemoStatesInitial,
        )
    }
}

@Stable
class ButtonStyleScreenState(
    val themeState: ThemeState,
    val textDemoStates: Map<ButtonStyleToken, TextDemoState>,
) {
    fun tokenSet(token: ButtonStyleToken): ButtonStyleTokenSet =
        themeState.component.button.getValue(token)

    fun textDemoState(token: ButtonStyleToken): TextDemoState =
        textDemoStates.getValue(token)
}

fun ButtonStyleScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        ButtonStyleToken.entries.associate { token ->
            token.name to save(state.textDemoState(token), TextDemoStateSaver, this)
        }
    },
    restore = { map ->
        ButtonStyleScreenState(
            themeState = themeState,
            textDemoStates = ButtonStyleToken.entries.associateWith { token ->
                restore(map[token.name], TextDemoStateSaver)!!
            },
        )
    }
)

@Composable
fun rememberButtonStyleScreenControl(
    state: ButtonStyleScreenState,
    themeController: ThemeController,
): ButtonStyleScreenControl {
    return remember(state, themeController) {
        ButtonStyleScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class ButtonStyleScreenControl(
    val state: ButtonStyleScreenState,
    val themeController: ThemeController,
) {
    private val textDemoControls: Map<ButtonStyleToken, TextDemoControl> =
        ButtonStyleToken.entries.associateWith { token ->
            TextDemoControl(state.textDemoState(token))
        }

    val buttonStyleControls = ButtonStyleToken.entries.map { token ->
        makeControlForToken(
            token = token,
            state = state,
            themeController = themeController,
            textDemoControl = textDemoControls.getValue(token),
        )
    }

    val controls: PersistentList<Control> = persistentListOf(
        *buttonStyleControls.toTypedArray(),
    )

    fun textDemoControl(token: ButtonStyleToken): TextDemoControl =
        textDemoControls.getValue(token)
}

private fun makeControlForToken(
    token: ButtonStyleToken,
    state: ButtonStyleScreenState,
    themeController: ThemeController,
    textDemoControl: TextDemoControl,
): Control {
    fun setTokenSet(value: ButtonStyleTokenSet) {
        themeController.updateComponent { it.copy(button = it.button + (token to value)) }
    }

    fun defaultBorder() = when (token) {
        ButtonStyleToken.Primary -> BorderStyleToken.Primary
        ButtonStyleToken.Secondary -> BorderStyleToken.Secondary
        ButtonStyleToken.Tertiary -> BorderStyleToken.Tertiary
    }

    val containerColorControl = enumControl(
        name = "Container color",
        values = { ColorToken.entries },
        selectedValue = { state.tokenSet(token).containerColor },
        onValueChange = { newValue ->
            setTokenSet(state.tokenSet(token).copy(containerColor = newValue))
        },
    )

    val shapeControl = enumControl(
        name = "Shape",
        values = { ShapeToken.entries },
        selectedValue = { state.tokenSet(token).shape },
        onValueChange = { newValue ->
            setTokenSet(state.tokenSet(token).copy(shape = newValue))
        },
    )

    val borderStyleToggleControl = Control.Toggle(
        name = "Border",
        value = { state.tokenSet(token).borderStyle != null },
        onValueChange = { newValue ->
            val border = if (newValue) {
                state.tokenSet(token).borderStyle ?: defaultBorder()
            } else {
                null
            }
            setTokenSet(state.tokenSet(token).copy(borderStyle = border))
        },
    )

    val borderStyleControl = enumControl(
        name = "Border style",
        values = { BorderStyleToken.entries },
        selectedValue = { state.tokenSet(token).borderStyle ?: defaultBorder() },
        onValueChange = { newValue ->
            setTokenSet(state.tokenSet(token).copy(borderStyle = newValue))
        },
    )

    val contentPaddingControl = spacingTokenPaddingControls(
        name = "Content padding",
        value = { state.tokenSet(token).contentPadding },
        onValueChange = { newValue ->
            setTokenSet(state.tokenSet(token).copy(contentPadding = newValue))
        },
    )

    return Control.ControlColumn(
        name = token.name,
        controls = {
            val borderControls = if (state.tokenSet(token).borderStyle != null) {
                listOf(
                    borderStyleToggleControl,
                    borderStyleControl,
                )
            } else {
                listOf(borderStyleToggleControl)
            }
            persistentListOf(
                containerColorControl,
                shapeControl,
                *borderControls.toTypedArray(),
                contentPaddingControl,
                Control.ControlColumn(
                    name = "Demo text",
                    indent = true,
                    controls = { textDemoControl.controls },
                    expandedInitial = false,
                ),
            )
        },
    )
}
