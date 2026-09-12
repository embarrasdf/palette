package com.embarrasdf.palette.formats.demo.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextFieldState.Saver.restore
import androidx.compose.foundation.text.input.TextFieldState.Saver.save
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
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.formats.core.Capitalization
import com.embarrasdf.palette.formats.core.TextFormat
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun TextFormatDemo(
    state: TextFormatDemoState = rememberTextFormatDemoState(),
    control: TextFormatDemoControl = rememberTextFormatDemoControl(state),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        TextFormatDemo(
            state = state,
        )
    }
}

@Composable
fun DemoScope.TextFormatDemo(
    state: TextFormatDemoState,
    modifier: Modifier = Modifier,
) {
    Text(
        text = state.text,
        style = PaletteTheme.component.core.text.headline.copy(
            format = state.textFormat
        ),
        modifier = modifier.align(Alignment.Center)
    )
}

@Composable
fun rememberTextFormatDemoState(
    textFormat: TextFormat = TextFormat(),
    demoTextFieldState: TextFieldState = TextFieldState("Sphinx of black quartz, judge my vow"),
): TextFormatDemoState {
    return rememberSaveable(
        textFormat,
        demoTextFieldState,
        saver = TextFormatDemoStateSaver(),
    ) {
        TextFormatDemoState(
            textFormatInitial = textFormat,
            demoTextFieldState = demoTextFieldState,
        )
    }
}

@Stable
class TextFormatDemoState(
    textFormatInitial: TextFormat = TextFormat(),
    val demoTextFieldState: TextFieldState = TextFieldState("Sphinx of black quartz, judge my vow"),
    val wordDelimiterTextFieldState: TextFieldState = TextFieldState(
        initialText = textFormatInitial.wordDelimiter,
    ),
) {
    var textFormat by mutableStateOf(textFormatInitial)
        internal set

    val capitalizeFirstChar: Boolean
        get() = (textFormat.capitalization as? Capitalization.Alternating)?.capitalizeFirstChar ?: false

    val capitalization: Capitalization.Key
        get() = Capitalization.toKey(textFormat.capitalization)

    val text by derivedStateOf {
        // Formatted by Text component
        demoTextFieldState.text.toString()
    }
}

private const val demoTextFieldStateKey = "demoTextFieldState"
private const val wordDelimiterTextFieldStateKey = "wordDelimiterTextFieldState"

fun TextFormatDemoStateSaver() = mapSaverSafe(
    save = { state ->
        mapOf(
            demoTextFieldStateKey to save(state.demoTextFieldState),
            wordDelimiterTextFieldStateKey to save(state.wordDelimiterTextFieldState),
        )
    },
    restore = { map ->
        TextFormatDemoState(
            demoTextFieldState = restore(map[demoTextFieldStateKey]!!) as TextFieldState,
            wordDelimiterTextFieldState = restore(map[wordDelimiterTextFieldStateKey]!!) as TextFieldState,
        )
    }
)

@Composable
fun rememberTextFormatDemoControl(
    state: TextFormatDemoState,
    onValueChange: (TextFormat) -> Unit = { state.textFormat = it },
): TextFormatDemoControl {
    val onValueChange by rememberUpdatedState(onValueChange)
    return remember(state, onValueChange) {
        TextFormatDemoControl(
            state = state,
            onValueChange = onValueChange,
        )
    }
}

@Stable
class TextFormatDemoControl(
    val state: TextFormatDemoState = TextFormatDemoState(),
    val onValueChange: (TextFormat) -> Unit = { state.textFormat = it },
    val includeTextFieldControl: Boolean = true,
) {
    val demoTextFieldControl = Control.TextField(
        name = "Text",
        includeLabel = false,
        textFieldState = state.demoTextFieldState,
    )
    val capitalizationControl = enumControl(
        name = "Capitalization",
        values = { Capitalization.Key.entries },
        selectedValue = { Capitalization.toKey(state.textFormat.capitalization) },
        onValueChange = { newKey ->
            val newState = state.textFormat.copy(
                capitalization = Capitalization.fromKey(newKey, state.capitalizeFirstChar)
            )
            onValueChange(newState)
        },
    )

    val capitalizeFirstCharControl = Control.Toggle(
        name = "Capitalize first char",
        value = { state.capitalizeFirstChar },
        onValueChange = { newValue ->
            val newState = state.textFormat.copy(
                capitalization = Capitalization.Alternating(newValue),
            )
            onValueChange(newState)
        },
    )

    val wordDelimiterControl = Control.TextField(
        name = "Word delimiter",
        textFieldState = state.wordDelimiterTextFieldState,
        onValueChange = { newValue ->
            val newState = state.textFormat.copy(wordDelimiter = newValue)
            onValueChange(newState)
        },
    )

    val replacementsControl = Control.DynamicList(
        name = "Replacements",
        items = { state.textFormat.replacements.entries.map { it.key to it.value } },
        onItemsChange = { pairs ->
            val newMap = pairs.toMap()
            val newState = state.textFormat.copy(replacements = newMap)
            onValueChange(newState)
        },
        newItemDefault = { "" to "" },
        createControl = { pair, onChange ->
            val keyFieldState = remember(pair.first) { TextFieldState(pair.first) }
            val valueFieldState = remember(pair.second) { TextFieldState(pair.second) }

            Control.ControlRow(
                controls = {
                    persistentListOf(
                        Control.TextField(
                            name = "From",
                            includeLabel = false,
                            textFieldState = keyFieldState,
                            onValueChange = { newKey ->
                                onChange(newKey to valueFieldState.text.toString())
                            },
                        ),
                        Control.TextField(
                            name = "To",
                            includeLabel = false,
                            textFieldState = valueFieldState,
                            onValueChange = { newValue ->
                                onChange(keyFieldState.text.toString() to newValue)
                            },
                        )
                    )
                }
            )
        },
        expandedInitial = false,
        indent = true,
    )

    val capitalizationControls = Control.ControlColumn(
        name = "Capitalization",
        controls = {
            buildList {
                add(capitalizationControl)
                if (state.capitalization == Capitalization.Key.Alternating) {
                    add(capitalizeFirstCharControl)
                }
            }.toPersistentList()
        }
    )

    val controls: PersistentList<Control> = buildList {
        if (includeTextFieldControl) {
            add(demoTextFieldControl)
        }
        add(capitalizationControls)
        add(wordDelimiterControl)
        add(replacementsControl)
    }.toPersistentList()

    fun updateFormat(format: TextFormat) {
        state.textFormat = format
    }
}
