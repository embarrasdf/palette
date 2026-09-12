package com.embarrasdf.palette.app.theme.semantic.format

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.formats.demo.core.NumberFormatDemo
import com.embarrasdf.palette.formats.demo.core.NumberFormatDemoState
import com.embarrasdf.palette.formats.demo.core.rememberNumberFormatDemoControl
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.components.layout.BoxWithLabel
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.formats.core.NumberFormat
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.semantic.format.Formats
import com.embarrasdf.palette.theme.semantic.format.core.NumberFormatScheme
import com.embarrasdf.palette.theme.semantic.format.core.NumberFormatToken
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun NumberFormatScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberNumberFormatScreenState(formats = themeController.semantic.formats)
    val control = rememberNumberFormatScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Number",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = state.numberFormatsByToken.entries.toList(),
            controls = control.controls,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { (token, _) ->
            BoxWithLabel(
                label = token.name,
            ) {
                NumberFormatDemo(
                    state = state.numberFormatDemoStatesByToken[token]!!,
                )
            }
        }
    }
}

@Composable
fun rememberNumberFormatScreenState(
    formats: Formats,
): NumberFormatScreenState {
    return rememberSaveable(
        formats,
        saver = NumberFormatScreenStateSaver(formats),
    ) {
        NumberFormatScreenState(
            formats = formats,
        )
    }
}

@Stable
class NumberFormatScreenState(
    val formats: Formats,
) {
    val numberFormatScheme: NumberFormatScheme
        get() = formats.numberFormats

    val numberFormatsByToken = NumberFormatToken.entries.associateWith { token ->
        when (token) {
            NumberFormatToken.Default -> numberFormatScheme.default
            NumberFormatToken.Currency -> numberFormatScheme.currency
        }
    }

    val numberFormatDemoStatesByToken = NumberFormatToken.entries.associateWith { token ->
        NumberFormatDemoState(
            numberFormatInitial = numberFormatsByToken[token]!!,
            demoTextFieldState = TextFieldState("12345678.90"),
        )
    }
}

fun NumberFormatScreenStateSaver(formats: Formats) = mapSaverSafe(
    save = { state ->
        mapOf()
    },
    restore = { map ->
        NumberFormatScreenState(
            formats = formats,
        )
    }
)

@Composable
fun rememberNumberFormatScreenControl(
    state: NumberFormatScreenState,
    themeController: ThemeController,
): NumberFormatScreenControl {
    val formatControlByToken = NumberFormatToken.entries.associateWith { token ->
        makeControlForToken(
            token = token,
            state = state,
            themeController = themeController,
        )
    }
    return remember(state, themeController) {
        NumberFormatScreenControl(
            state = state,
            themeController = themeController,
            formatControlByToken = formatControlByToken,
        )
    }
}

@Stable
class NumberFormatScreenControl(
    val state: NumberFormatScreenState,
    val themeController: ThemeController,
    formatControlByToken: Map<NumberFormatToken, Control>,
) {
    val controls: PersistentList<Control> = persistentListOf(
        *formatControlByToken.values.toTypedArray(),
    )
}

@Composable
private fun makeControlForToken(
    token: NumberFormatToken,
    state: NumberFormatScreenState,
    themeController: ThemeController,
): Control {
    val demoControl = rememberNumberFormatDemoControl(
        state = state.numberFormatDemoStatesByToken[token]!!,
        onValueChange = { newValue ->
            themeController.updateSemantic {
                it.copy(
                    formats = state.formats.copy(
                        numberFormats = state.numberFormatScheme.update(
                            token = token,
                            numberFormat = newValue,
                        )
                    )
                )
            }
        }
    )
    return Control.ControlColumn(
        name = token.name,
        controls = { demoControl.controls },
        expandedInitial = false,
    )
}

fun NumberFormatScheme.update(
    token: NumberFormatToken,
    numberFormat: NumberFormat,
): NumberFormatScheme {
    return when (token) {
        NumberFormatToken.Default -> this.copy(
            default = numberFormat,
        )
        NumberFormatToken.Currency -> this.copy(
            currency = numberFormat,
        )
    }
}
