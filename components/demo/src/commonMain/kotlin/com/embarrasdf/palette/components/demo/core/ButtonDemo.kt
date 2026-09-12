package com.embarrasdf.palette.components.demo.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.control.paddingValuesControls
import com.embarrasdf.palette.components.util.PaddingValuesSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.core.ButtonStyleToken
import com.embarrasdf.palette.theme.semantic.color.toColor
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ButtonDemo(
    state: ButtonDemoState = rememberButtonDemoState(),
    control: ButtonDemoControl = rememberButtonDemoControl(state),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        ButtonDemo(
            state = state,
            control = control,
        )
    }
}

@Composable
fun DemoScope.ButtonDemo(
    modifier: Modifier = Modifier,
    state: ButtonDemoState = rememberButtonDemoState(),
    control: ButtonDemoControl = rememberButtonDemoControl(state),
) {
    LaunchedEffect(control, maxWidth) {
        control.onButtonSizeChanged(maxWidth)
    }
    // A colored button container needs matching content color; the demo, as the caller, picks it
    // per variant — the same way content on a Surface is colored by whoever places it.
    val contentColor = when (state.style) {
        ButtonStyleToken.Primary -> ColorToken.OnPrimary
        ButtonStyleToken.Secondary -> ColorToken.Secondary
        ButtonStyleToken.Tertiary -> ColorToken.Primary
    }.toColor()
    LaunchedEffect(state.style, contentColor) {
        state.textDemoState.textStyleDemoState.color = contentColor
    }
    val style = state.buttonStyle ?: PaletteTheme.component.core.button[state.style]
    Button(
        onClick = {},
        style = style.copy(contentPadding = state.contentPadding),
        enabled = state.enabled,
        modifier = modifier
            .width(state.width)
            .align(Alignment.Center)
            .padding(PaletteTheme.semantic.dimension.spacing.medium)
            .semantics { contentDescription = "Demo Button" }
    ) {
        this@ButtonDemo.TextDemo(
            state = state.textDemoState,
            control = control.textDemoControl,
        )
    }
}

@Composable
fun rememberButtonDemoState(
    buttonStyleInitial: ButtonStyle? = null,
    contentPaddingInitial: PaddingValues = PaddingValues(
        horizontal = PaletteTheme.semantic.dimension.spacing.large,
        vertical = PaletteTheme.semantic.dimension.spacing.medium,
    ),
): ButtonDemoState = rememberSaveable(saver = ButtonDemoStateSaver) {
    ButtonDemoState(
        buttonStyleInitial = buttonStyleInitial,
        contentPaddingInitial = contentPaddingInitial,
    )
}

@Stable
class ButtonDemoState(
    styleInitial: ButtonStyleToken = ButtonStyleToken.Primary,
    enabledInitial: Boolean = true,
    maxWidthInitial: Dp = 0.dp,
    widthInitial: Dp = 200.dp,
    contentPaddingInitial: PaddingValues = PaddingValues(),
    buttonStyleInitial: ButtonStyle? = null,
    val textDemoState: TextDemoState = TextDemoState(
        initialText = "Button",
        textAlignInitial = TextAlign.Center,
    ),
) {
    var enabled by mutableStateOf(enabledInitial)
        internal set
    var style by mutableStateOf(styleInitial)
        internal set
    var buttonStyle by mutableStateOf(buttonStyleInitial)
        internal set
    var maxWidth by mutableStateOf(maxWidthInitial)
        internal set
    var width by mutableStateOf(widthInitial)
        internal set
    var contentPadding by mutableStateOf(contentPaddingInitial)
        internal set
}

private const val enabledKey = "enabled"
private const val styleKey = "style"
private const val maxWidthKey = "maxWidth"
private const val widthKey = "width"
private const val contentPaddingKey = "contentPadding"
private const val textDemoStateKey = "textDemoState"

val ButtonDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            enabledKey to value.enabled,
            styleKey to value.style,
            maxWidthKey to value.maxWidth.value,
            widthKey to value.width.value,
            contentPaddingKey to save(value.contentPadding, PaddingValuesSaver, this),
            textDemoStateKey to save(value.textDemoState, TextDemoStateSaver, this),
        )
    },
    restore = { map ->
        ButtonDemoState(
            enabledInitial = map[enabledKey] as Boolean,
            styleInitial = map[styleKey] as ButtonStyleToken,
            maxWidthInitial = (map[maxWidthKey] as Float).dp,
            widthInitial = (map[widthKey] as Float).dp,
            contentPaddingInitial = restore(map[contentPaddingKey], PaddingValuesSaver)!!,
            textDemoState = restore(map[textDemoStateKey], TextDemoStateSaver)!!
        )
    },
)

@Composable
fun rememberButtonDemoControl(
    state: ButtonDemoState,
): ButtonDemoControl = remember(state) { ButtonDemoControl(state) }

@Stable
class ButtonDemoControl(
    val state: ButtonDemoState,
) {
    val styleTokenControl = enumControl(
        name = "Token",
        values = { ButtonStyleToken.entries },
        selectedValue = { state.style },
        onValueChange = { state.style = it },
    )

    val enabledControl = Control.Toggle(
        name = "Enabled",
        value = { state.enabled },
        onValueChange = { state.enabled = it },
    )

    val widthControl = Control.Slider(
        name = "Width",
        value = { state.width.value },
        onValueChange = { state.width = it.dp },
        valueRange = { 0f..state.maxWidth.value },
    )

    val contentPaddingControl = paddingValuesControls(
        name = "Content padding",
        value = { state.contentPadding },
        onValueChange = { state.contentPadding = it },
    )

    val styleControls = Control.ControlColumn(
        name = "Style",
        indent = true,
        expandedInitial = true,
        controls = {
            persistentListOf(
                styleTokenControl,
                contentPaddingControl,
            )
        },
    )

    val textDemoControl = TextDemoControl(state.textDemoState)
    val textDemoControls = Control.ControlColumn(
        name = "Text",
        indent = true,
        controls = { textDemoControl.controls },
    )

    val controls = persistentListOf(
        enabledControl,
        styleControls,
        widthControl,
        textDemoControls,
    )

    fun updateStyle(buttonStyle: ButtonStyle?) {
        state.buttonStyle = buttonStyle
    }

    fun onButtonSizeChanged(width: Dp) {
        if (state.maxWidth == 0.dp) {
            state.width = width
        }
        state.maxWidth = width
        if (state.width > state.maxWidth) {
            state.width = state.maxWidth
        }

        if (state.textDemoState.maxWidth == 0.dp) {
            state.textDemoState.width = width
        }
        state.textDemoState.maxWidth = width
        if (state.textDemoState.width > state.textDemoState.maxWidth) {
            state.textDemoState.width = state.textDemoState.maxWidth
        }
    }
}
