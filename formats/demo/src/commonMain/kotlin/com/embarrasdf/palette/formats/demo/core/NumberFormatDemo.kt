package com.embarrasdf.palette.formats.demo.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextFieldState.Saver.restore
import androidx.compose.foundation.text.input.TextFieldState.Saver.save
import androidx.compose.foundation.text.input.byValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.formats.core.IntGrouping
import com.embarrasdf.palette.formats.core.NumberFormat
import com.embarrasdf.palette.formats.core.format
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun NumberFormatDemo(
    state: NumberFormatDemoState = rememberNumberFormatDemoState(),
    control: NumberFormatDemoControl = rememberNumberFormatDemoControl(state),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        NumberFormatDemo(
            state = state,
        )
    }
}

@Composable
fun DemoScope.NumberFormatDemo(
    state: NumberFormatDemoState,
    modifier: Modifier = Modifier,
) {
    Text(
        text = state.text,
        style = PaletteTheme.component.core.text.headline,
        modifier = modifier.align(Alignment.Center)
    )
}

@Composable
fun rememberNumberFormatDemoState(
    numberFormat: NumberFormat = NumberFormat(),
    demoTextFieldState: TextFieldState = TextFieldState("12345678.90"),
): NumberFormatDemoState {
    return rememberSaveable(
        numberFormat,
        demoTextFieldState,
        saver = NumberFormatDemoStateSaver(),
    ) {
        NumberFormatDemoState(
            numberFormatInitial = numberFormat,
            demoTextFieldState = demoTextFieldState,
        )
    }
}

@Stable
class NumberFormatDemoState(
    numberFormatInitial: NumberFormat = NumberFormat(),
    val demoTextFieldState: TextFieldState = TextFieldState("12345678.90"),
    val minNumDecimalValuesTextFieldState: TextFieldState = TextFieldState(
        initialText = numberFormatInitial.minNumDecimalValues.toString(),
    ),
    val maxNumDecimalValuesTextFieldState: TextFieldState = TextFieldState(
        initialText = numberFormatInitial.maxNumDecimalValues.toString(),
    ),
    val positiveSignTextFieldState: TextFieldState = TextFieldState(
        initialText = numberFormatInitial.positiveSign ?: "",
    ),
    val negativeSignTextFieldState: TextFieldState = TextFieldState(
        initialText = numberFormatInitial.negativeSign,
    ),
    val groupingNumDigitsTextFieldState: TextFieldState = TextFieldState(
        initialText = when (val grouping = numberFormatInitial.intGrouping) {
            is IntGrouping.Uniform -> grouping.numDigits.toString()
            is IntGrouping.None -> "0"
        },
    ),
) {
    var numberFormat by mutableStateOf(numberFormatInitial)
        internal set

    val intGroupingType: DigitGroupingType
        get() = when (numberFormat.intGrouping) {
            is IntGrouping.Uniform -> DigitGroupingType.Uniform
            is IntGrouping.None -> DigitGroupingType.None
        }

    val text by derivedStateOf {
        numberFormat.format(demoTextFieldState.text.toString())
    }
}

private const val demoTextFieldStateKey = "demoTextFieldState"
private const val minNumDecimalValuesTextFieldStateKey = "minNumDecimalValuesTextFieldState"
private const val maxNumDecimalValuesTextFieldStateKey = "maxNumDecimalValuesTextFieldState"
private const val positiveSignTextFieldStateKey = "positiveSignTextFieldState"
private const val negativeSignTextFieldStateKey = "negativeSignTextFieldState"
private const val groupingNumDigitsTextFieldStateKey = "groupingNumDigitsTextFieldState"

fun NumberFormatDemoStateSaver() = mapSaverSafe(
    save = { state ->
        mapOf(
            demoTextFieldStateKey to save(state.demoTextFieldState),
            minNumDecimalValuesTextFieldStateKey to save(state.minNumDecimalValuesTextFieldState),
            maxNumDecimalValuesTextFieldStateKey to save(state.maxNumDecimalValuesTextFieldState),
            positiveSignTextFieldStateKey to save(state.positiveSignTextFieldState),
            negativeSignTextFieldStateKey to save(state.negativeSignTextFieldState),
            groupingNumDigitsTextFieldStateKey to save(state.groupingNumDigitsTextFieldState),
        )
    },
    restore = { map ->
        NumberFormatDemoState(
            demoTextFieldState = restore(map[demoTextFieldStateKey]!!) as TextFieldState,
            minNumDecimalValuesTextFieldState = restore(map[minNumDecimalValuesTextFieldStateKey]!!) as TextFieldState,
            maxNumDecimalValuesTextFieldState = restore(map[maxNumDecimalValuesTextFieldStateKey]!!) as TextFieldState,
            positiveSignTextFieldState = restore(map[positiveSignTextFieldStateKey]!!) as TextFieldState,
            negativeSignTextFieldState = restore(map[negativeSignTextFieldStateKey]!!) as TextFieldState,
            groupingNumDigitsTextFieldState = restore(map[groupingNumDigitsTextFieldStateKey]!!) as TextFieldState,
        )
    }
)

@Composable
fun rememberNumberFormatDemoControl(
    state: NumberFormatDemoState,
    onValueChange: (NumberFormat) -> Unit = { state.numberFormat = it },
): NumberFormatDemoControl {
    val onValueChange by rememberUpdatedState(onValueChange)
    return remember(state, onValueChange) {
        NumberFormatDemoControl(
            state = state,
            onValueChange = onValueChange,
        )
    }
}

enum class DigitGroupingType {
    Uniform,
    None,
}

@Stable
class NumberFormatDemoControl(
    val state: NumberFormatDemoState = NumberFormatDemoState(),
    val onValueChange: (NumberFormat) -> Unit = { state.numberFormat = it },
    val includeTextFieldControl: Boolean = true,
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

    val minNumDecimalValuesControl = Control.TextField(
        name = "Min num decimal values",
        textFieldState = state.minNumDecimalValuesTextFieldState,
        onValueChange = { newValue ->
            val min = newValue.toIntOrNull() ?: state.numberFormat.minNumDecimalValues
            val max = state.numberFormat.maxNumDecimalValues
            val newState = state.numberFormat.copy(
                numDecimalValuesRange = min..max,
            )
            onValueChange(newState)
        },
        keyboardOptions = {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            )
        },
        inputTransformation = {
            InputTransformation.byValue { _, proposed ->
                proposed.filter { it.isDigit() }
            }
        }
    )

    val maxNumDecimalValuesControl = Control.TextField(
        name = "Max num decimal values",
        textFieldState = state.maxNumDecimalValuesTextFieldState,
        onValueChange = { newValue ->
            val min = state.numberFormat.minNumDecimalValues
            val max = newValue.toIntOrNull() ?: state.numberFormat.maxNumDecimalValues
            val newState = state.numberFormat.copy(
                numDecimalValuesRange = min..max,
            )
            onValueChange(newState)
        },
        keyboardOptions = {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            )
        },
        inputTransformation = {
            InputTransformation.byValue { _, proposed ->
                proposed.filter { it.isDigit() }
            }
        }
    )

    val positiveSignControl = Control.TextField(
        name = "Positive sign",
        textFieldState = state.positiveSignTextFieldState,
        onValueChange = { newValue ->
            val newState = state.numberFormat.copy(
                positiveSign = newValue,
            )
            onValueChange(newState)
        },
    )

    val negativeSignControl = Control.TextField(
        name = "Negative sign",
        textFieldState = state.negativeSignTextFieldState,
        onValueChange = { newValue ->
            val newState = state.numberFormat.copy(
                negativeSign = newValue,
            )
            onValueChange(newState)
        },
    )

    val groupingTypeControl = enumControl(
        name = "Type",
        values = { DigitGroupingType.entries },
        selectedValue = { state.intGroupingType },
        onValueChange = { newValue ->
            val numDigits = state.groupingNumDigitsTextFieldState.text.toString().toIntOrNull() ?: 0
            val newState = state.numberFormat.copy(
                intGrouping = when (newValue) {
                    DigitGroupingType.Uniform -> IntGrouping.Uniform(
                        numDigits = numDigits,
                        separator = when (val grouping = state.numberFormat.intGrouping) {
                            is IntGrouping.Uniform -> grouping.separator
                            is IntGrouping.None -> ','
                        }
                    )

                    DigitGroupingType.None -> IntGrouping.None
                }
            )
            onValueChange(newState)
        },
    )

    val groupingSeparatorControl = Control.CharField(
        name = "Separator",
        value = {
            when (val grouping = state.numberFormat.intGrouping) {
                is IntGrouping.Uniform -> grouping.separator
                is IntGrouping.None -> ','
            }
        },
        onValueChange = { newValue ->
            val newState = state.numberFormat.copy(
                intGrouping = when (val grouping = state.numberFormat.intGrouping) {
                    is IntGrouping.Uniform -> grouping.copy(
                        separator = newValue,
                    )
                    is IntGrouping.None -> IntGrouping.None
                },
            )
            onValueChange(newState)
            onValueChange(newState)
        },
    )

    val groupingNumDigitsControl = Control.TextField(
        name = "Num digits",
        textFieldState = state.groupingNumDigitsTextFieldState,
        onValueChange = { newValue ->
            val numDigits = newValue.toIntOrNull() ?: return@TextField
            val newState = state.numberFormat.copy(
                intGrouping = when (val grouping = state.numberFormat.intGrouping) {
                    is IntGrouping.Uniform -> grouping.copy(
                        numDigits = numDigits,
                    )
                    is IntGrouping.None -> IntGrouping.None
                },
            )
            onValueChange(newState)
        },
        keyboardOptions = {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            )
        },
        inputTransformation = {
            InputTransformation.byValue { _, proposed ->
                proposed.filter { it.isDigit() }
            }
        }
    )

    val groupingControls = Control.ControlColumn(
        name = "Grouping",
        controls = {
            buildList {
                add(groupingTypeControl)
                when (state.intGroupingType) {
                    DigitGroupingType.Uniform -> addAll(
                        listOf(
                            groupingSeparatorControl,
                            groupingNumDigitsControl,
                        )
                    )

                    DigitGroupingType.None -> Unit
                }
            }.toPersistentList()
        }
    )

    val decimalSeparatorControl = Control.CharField(
        name = "Decimal separator",
        value = { state.numberFormat.decimalSeparator },
        onValueChange = { newValue ->
            val newState = state.numberFormat.copy(
                decimalSeparator = newValue,
            )
            onValueChange(newState)
        },
    )

    val controls: PersistentList<Control> = buildList {
        if (includeTextFieldControl) {
            add(demoTextFieldControl)
        }
        addAll(
            listOf(
                minNumDecimalValuesControl,
                maxNumDecimalValuesControl,
                positiveSignControl,
                negativeSignControl,
                groupingControls,
                decimalSeparatorControl,
            )
        )
    }.toPersistentList()
}
