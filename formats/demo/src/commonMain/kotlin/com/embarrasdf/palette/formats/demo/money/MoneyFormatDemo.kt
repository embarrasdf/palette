package com.embarrasdf.palette.formats.demo.money

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.formats.demo.core.NumberFormatDemoControl
import com.embarrasdf.palette.formats.demo.core.NumberFormatDemoState
import com.embarrasdf.palette.formats.money.MoneyFormat
import com.embarrasdf.palette.formats.money.format
import com.embarrasdf.palette.formats.money.update
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MoneyFormatDemo(
    state: MoneyFormatDemoState = rememberMoneyFormatDemoState(),
    control: MoneyFormatDemoControl = rememberMoneyFormatDemoControl(state),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        MoneyFormatDemo(
            state = state,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun DemoScope.MoneyFormatDemo(
    state: MoneyFormatDemoState = rememberMoneyFormatDemoState(),
    modifier: Modifier = Modifier,
) {
    Text(
        text = state.text,
        style = PaletteTheme.component.core.text.headline,
        modifier = modifier.align(Alignment.Center)
    )
}

@Composable
fun rememberMoneyFormatDemoState(
    moneyFormatInitial: MoneyFormat = MoneyFormat()
): MoneyFormatDemoState {
    return rememberSaveable(
        moneyFormatInitial,
        saver = MoneyFormatDemoStateSaver(),
    ) {
        MoneyFormatDemoState(
            moneyFormatInitial = moneyFormatInitial,
        )
    }
}

@Stable
class MoneyFormatDemoState(
    moneyFormatInitial: MoneyFormat = MoneyFormat(),
    val demoTextFieldState: TextFieldState = TextFieldState("12345678.90"),
    val currencySymbolTextFieldState: TextFieldState = TextFieldState(
        initialText = moneyFormatInitial.currencySymbol ?: "",
    ),
    val numberFormatDemoState: NumberFormatDemoState = NumberFormatDemoState(
        numberFormatInitial = moneyFormatInitial.numberFormat,
        demoTextFieldState = demoTextFieldState,
    ),
) {
    var moneyFormat by mutableStateOf(moneyFormatInitial)
        internal set

    val text by derivedStateOf {
        moneyFormat.format(demoTextFieldState.text.toString())
    }
}

fun MoneyFormatDemoStateSaver() = mapSaverSafe(
    save = { state ->
        mapOf()
    },
    restore = { map ->
        MoneyFormatDemoState()
    }
)

@Composable
fun rememberMoneyFormatDemoControl(
    state: MoneyFormatDemoState,
    updateState: (MoneyFormat) -> Unit = { state.moneyFormat = it },
): MoneyFormatDemoControl {
    return remember(state, state.moneyFormat) {
        MoneyFormatDemoControl(
            state = state,
            onValueChange = updateState,
        )
    }
}

@Stable
class MoneyFormatDemoControl(
    val state: MoneyFormatDemoState,
    val onValueChange: (MoneyFormat) -> Unit,
) {
    val demoTextFieldControl = Control.TextField(
        name = "Number",
        includeLabel = false,
        textFieldState = state.demoTextFieldState,
        keyboardOptions = {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            )
        }
    )

    val currencySymbolControl = Control.TextField(
        name = "Currency symbol",
        textFieldState = state.currencySymbolTextFieldState,
        onValueChange = { newValue ->
            val newState = state.moneyFormat.update(
                currencySymbol = newValue,
            )
            onValueChange(newState)
        },
    )

    val numberFormatDemoControl = NumberFormatDemoControl(
        state = state.numberFormatDemoState,
        onValueChange = { newValue ->
            val newState = state.moneyFormat.update(
                numberFormat = newValue,
            )
            onValueChange(newState)
        },
        includeTextFieldControl = false,
    )
    val numberFormatControls = Control.ControlColumn(
        name = "Number format",
        controls = { numberFormatDemoControl.controls },
        expandedInitial = true,
        indent = true,
    )

    val controls: PersistentList<Control> = persistentListOf(
        demoTextFieldControl,
        currencySymbolControl,
        numberFormatControls,
    )
}
