package com.embarrasdf.palette.components.demo.core

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.ColorSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.formats.demo.core.TextFormatDemoControl
import com.embarrasdf.palette.formats.demo.core.TextFormatDemoState
import com.embarrasdf.palette.formats.demo.core.TextFormatDemoStateSaver
import kotlinx.collections.immutable.persistentListOf

val TextStyleDemoDefault = TextStyle(
    composeTextStyle = ComposeTextStyleDemoDefault,
)

@Composable
fun rememberTextStyleDemoState(
    textStyleInitial: TextStyle = TextStyleDemoDefault,
    demoTextFieldState: TextFieldState = TextFieldState("Hello world"),
) = rememberSaveable(saver = TextStyleDemoStateSaver) {
    TextStyleDemoState(
        textStyleInitial = textStyleInitial,
        demoTextFieldState = demoTextFieldState,
    )
}

@Stable
class TextStyleDemoState(
    textStyleInitial: TextStyle = TextStyleDemoDefault,
    val demoTextFieldState: TextFieldState = TextFieldState("Hello world"),
) {
    val composeTextStyleDemoState = ComposeTextStyleDemoState(
        composeTextStyleInitial = textStyleInitial.composeTextStyle,
    )

    val textFormatDemoState = TextFormatDemoState(
        textFormatInitial = textStyleInitial.format,
        demoTextFieldState = demoTextFieldState,
    )

    var color by mutableStateOf(textStyleInitial.composeTextStyle.color)

    val textStyle: TextStyle by derivedStateOf {
        TextStyle(
            composeTextStyle = composeTextStyleDemoState.composeTextStyle.copy(color = color),
            format = textFormatDemoState.textFormat,
        )
    }
}

private const val composeTextStyleDemoStateKey = "composeTextStyleDemoState"
private const val textFormatDemoStateKey = "textFormatDemoState"
private const val colorKey = "color"

val TextStyleDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            composeTextStyleDemoStateKey to save(
                value.composeTextStyleDemoState,
                ComposeTextStyleDemoStateSaver,
                this
            ),
            textFormatDemoStateKey to save(
                value.textFormatDemoState,
                TextFormatDemoStateSaver(),
                this
            ),
            // Only persist a specified color; Color.Unspecified would round-trip to transparent.
            colorKey to save(value.color.takeIf { it.isSpecified }, ColorSaver, this),
        )
    },
    restore = { map ->
        val composeTextStyleDemoState: ComposeTextStyleDemoState = restore(
            map[composeTextStyleDemoStateKey],
            ComposeTextStyleDemoStateSaver
        )!!

        val textFormatDemoState: TextFormatDemoState = restore(
            map[textFormatDemoStateKey],
            TextFormatDemoStateSaver()
        )!!

        TextStyleDemoState(
            textStyleInitial = TextStyle(
                composeTextStyle = composeTextStyleDemoState.composeTextStyle.copy(
                    color = restore(map[colorKey], ColorSaver) ?: Color.Unspecified,
                ),
                format = textFormatDemoState.textFormat,
            ),
        )
    }
)

@Composable
fun rememberTextStyleDemoControl(
    state: TextStyleDemoState,
    includeTextFieldControl: Boolean = false,
) = remember(state, includeTextFieldControl) {
    TextStyleDemoControl(
        state = state,
        includeTextFieldControl = includeTextFieldControl,
    )
}

@Stable
class TextStyleDemoControl(
    private val state: TextStyleDemoState,
    includeTextFieldControl: Boolean = false,
) {
    val composeTextStyleControl = ComposeTextStyleDemoControl(
        state = state.composeTextStyleDemoState,
    )

    val composeTextStyleControlColumn = Control.ControlColumn(
        name = "ComposeTextStyle",
        controls = { composeTextStyleControl.controls },
    )

    val colorControl = Control.Color(
        name = "Color",
        color = { state.color },
        onColorChange = { state.color = it },
    )

    val textFormatControl = TextFormatDemoControl(
        state = state.textFormatDemoState,
        includeTextFieldControl = includeTextFieldControl,
    )

    val textFormatControlColumn = Control.ControlColumn(
        name = "TextFormat",
        controls = { textFormatControl.controls },
    )

    val controls = persistentListOf(
        composeTextStyleControlColumn,
        colorControl,
        textFormatControlColumn,
    )

    fun updateStyle(textStyle: TextStyle) {
        composeTextStyleControl.updateTextStyle(textStyle.composeTextStyle)
        textFormatControl.updateFormat(textStyle.format)
    }
}
