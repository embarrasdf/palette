package com.embarrasdf.palette.components.demo.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.allCaps
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.TextField
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.control.paddingValuesControls
import com.embarrasdf.palette.components.demo.util.KeyboardCapitalizationSaver
import com.embarrasdf.palette.components.demo.util.KeyboardTypeSaver
import com.embarrasdf.palette.components.demo.util.onlyDigits
import com.embarrasdf.palette.components.util.PaddingValuesSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.formats.core.format
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.core.copy
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TextFieldDemo(
    modifier: Modifier = Modifier,
    state: TextFieldDemoState = rememberTextFieldDemoState(),
    control: TextFieldDemoControl = rememberTextFieldDemoControl(state = state),
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize(),
    ) {
        TextFieldDemo(
            state = state,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = PaletteTheme.semantic.dimension.spacing.medium)
        )
    }
}

@Composable
fun DemoScope.TextFieldDemo(
    modifier: Modifier = Modifier,
    state: TextFieldDemoState = rememberTextFieldDemoState(),
    control: TextFieldDemoControl = rememberTextFieldDemoControl(state = state),
) {
    LaunchedEffect(maxWidth) {
        control.onSizeChanged(maxWidth)
    }

    LaunchedEffect(state.textStyleDemoState.textStyle.format) {
        val currentText = state.textFieldState.text.toString()
        val formatted = state.textStyleDemoState.textStyle.format.format(currentText)
        if (formatted != currentText) {
            state.textFieldState.edit {
                replace(0, length, formatted)
            }
        }
    }

    TextField(
        state = state.textFieldState,
        style = PaletteTheme.component.core.textField.copy(
            textStyle = state.textStyleDemoState.textStyle,
            contentPadding = state.contentPadding,
        ),
        enabled = state.enabled,
        lineLimits = when (state.lineLimits) {
            LineLimits.SingleLine -> TextFieldLineLimits.SingleLine
            LineLimits.Multiline -> TextFieldLineLimits.MultiLine(
                minHeightInLines = state.minHeightInLines,
                maxHeightInLines = state.maxHeightInLines,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = state.keyboardType,
            autoCorrectEnabled = state.autoCorrectEnabled,
            capitalization = state.keyboardCapitalization,
            showKeyboardOnFocus = state.showKeyboardOnFocus,
        ),
        inputTransformation = when (state.inputTransformation) {
            InputTransformations.None -> null
            InputTransformations.AllCaps -> InputTransformation.allCaps(Locale.current)
            InputTransformations.OnlyDigits -> InputTransformation.onlyDigits()
        },
        modifier = modifier
            .width(state.width)
    )
}

private val keyboardTypes = persistentListOf(
    KeyboardType.Text,
    KeyboardType.Number,
    KeyboardType.Ascii,
    KeyboardType.Decimal,
    KeyboardType.Email,
    KeyboardType.NumberPassword,
    KeyboardType.Password,
    KeyboardType.Phone,
    KeyboardType.Unspecified,
    KeyboardType.Uri,
)

private val keyboardCapitalizations = persistentListOf(
    KeyboardCapitalization.Characters,
    KeyboardCapitalization.None,
    KeyboardCapitalization.Sentences,
    KeyboardCapitalization.Unspecified,
    KeyboardCapitalization.Words,
)

enum class LineLimits {
    SingleLine,
    Multiline,
}

enum class InputTransformations {
    None,
    AllCaps,
    OnlyDigits,
}

@Composable
fun rememberTextFieldDemoState(
    initialText: String = "Hello world",
    textStyleInitial: TextStyle = TextStyleDemoDefault.copy(
        color = PaletteTheme.semantic.color.onSurface,
    ),
    contentPaddingInitial: PaddingValues = PaletteTheme.component.core.textField.contentPadding,
): TextFieldDemoState {
    return rememberSaveable(saver = TextFieldDemoStateSaver) {
        TextFieldDemoState(
            initialText = initialText,
            textStyleInitial = textStyleInitial,
            contentPaddingInitial = contentPaddingInitial,
        )
    }
}

@Stable
class TextFieldDemoState(
    initialText: String,
    textStyleInitial: TextStyle = TextStyleDemoDefault,
    maxWidthInitial: Dp = 0.dp,
    widthInitial: Dp = 200.dp,
    enabledInitial: Boolean = true,
    keyboardTypeInitial: KeyboardType = KeyboardType.Unspecified,
    keyboardCapitalizationInitial: KeyboardCapitalization = KeyboardCapitalization.Unspecified,
    autoCorrectEnabledInitial: Boolean = true,
    showKeyboardOnFocusInitial: Boolean = false,
    lineLimitsInitial: LineLimits = LineLimits.SingleLine,
    minHeightInLinesInitial: Int = 1,
    maxHeightInLinesInitial: Int = Int.MAX_VALUE,
    inputTransformationInitial: InputTransformations = InputTransformations.None,
    contentPaddingInitial: PaddingValues = PaddingValues(),
) {
    internal val textFieldState = TextFieldState(initialText = initialText)
    val text = snapshotFlow { textFieldState.text.toString() }

    val textStyleDemoState = TextStyleDemoState(
        textStyleInitial = textStyleInitial,
        demoTextFieldState = textFieldState,
    )

    var maxWidth by mutableStateOf(maxWidthInitial)
        internal set
    var width by mutableStateOf(widthInitial)
        internal set

    var enabled by mutableStateOf(enabledInitial)
        internal set

    var keyboardType by mutableStateOf(keyboardTypeInitial)
        internal set
    var keyboardCapitalization by mutableStateOf(keyboardCapitalizationInitial)
        internal set
    var autoCorrectEnabled by mutableStateOf(autoCorrectEnabledInitial)
        internal set
    var showKeyboardOnFocus by mutableStateOf(showKeyboardOnFocusInitial)
        internal set

    var lineLimits by mutableStateOf(lineLimitsInitial)
        internal set
    var minHeightInLines by mutableIntStateOf(minHeightInLinesInitial)
        internal set
    var maxHeightInLines by mutableIntStateOf(maxHeightInLinesInitial)
        internal set

    var inputTransformation by mutableStateOf(inputTransformationInitial)
        internal set

    var contentPadding by mutableStateOf(contentPaddingInitial)
        internal set
}

private const val textKey = "text"
private const val widthKey = "width"
private const val maxWidthKey = "maxWidth"
private const val enabledKey = "enabled"
private const val styleKey = "style"
private const val keyboardTypeKey = "keyboardType"
private const val keyboardCapitalizationKey = "keyboardCapitalization"
private const val autoCorrectEnabledKey = "autoCorrectEnabled"
private const val showKeyboardOnFocusKey = "showKeyboardOnFocus"
private const val lineLimitsKey = "lineLimits"
private const val minHeightInLinesKey = "minHeightInLines"
private const val maxHeightInLinesKey = "maxHeightInLines"
private const val inputTransformationKey = "inputTransformation"
private const val contentPaddingKey = "contentPadding"

val TextFieldDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            textKey to value.textFieldState.text.toString(),
            widthKey to value.width.value,
            maxWidthKey to value.maxWidth.value,
            enabledKey to value.enabled,
            styleKey to save(value.textStyleDemoState, TextStyleDemoStateSaver, this),
            keyboardTypeKey to save(value.keyboardType, KeyboardTypeSaver, this),
            keyboardCapitalizationKey to save(
                value = value.keyboardCapitalization,
                saver = KeyboardCapitalizationSaver,
                scope = this,
            ),
            autoCorrectEnabledKey to value.autoCorrectEnabled,
            showKeyboardOnFocusKey to value.showKeyboardOnFocus,
            lineLimitsKey to value.lineLimits.name,
            minHeightInLinesKey to value.minHeightInLines,
            maxHeightInLinesKey to value.maxHeightInLines,
            inputTransformationKey to value.inputTransformation.name,
            contentPaddingKey to save(value.contentPadding, PaddingValuesSaver, this),
        )
    },
    restore = { map ->
        val textStyleDemoState: TextStyleDemoState = restore(map[styleKey], TextStyleDemoStateSaver)!!

        TextFieldDemoState(
            initialText = map[textKey] as String,
            textStyleInitial = textStyleDemoState.textStyle,
            widthInitial = (map[widthKey] as Float).dp,
            maxWidthInitial = (map[maxWidthKey] as Float).dp,
            enabledInitial = map[enabledKey] as Boolean,
            keyboardTypeInitial = restore(map[keyboardTypeKey], KeyboardTypeSaver)!!,
            keyboardCapitalizationInitial = restore(map[keyboardCapitalizationKey], KeyboardCapitalizationSaver)!!,
            autoCorrectEnabledInitial = map[autoCorrectEnabledKey] as Boolean,
            showKeyboardOnFocusInitial = map[showKeyboardOnFocusKey] as Boolean,
            lineLimitsInitial = LineLimits.valueOf(map[lineLimitsKey] as String),
            minHeightInLinesInitial = map[minHeightInLinesKey] as Int,
            maxHeightInLinesInitial = map[maxHeightInLinesKey] as Int,
            inputTransformationInitial = InputTransformations.valueOf(map[inputTransformationKey] as String),
            contentPaddingInitial = restore(map[contentPaddingKey], PaddingValuesSaver)!!,
        )
    }
)

@Composable
fun rememberTextFieldDemoControl(
    state: TextFieldDemoState = rememberTextFieldDemoState(),
) = remember(state) { TextFieldDemoControl(state) }

@Stable
class TextFieldDemoControl(
    val state: TextFieldDemoState,
) {
    val textFieldControl = Control.TextField(
        name = "Text",
        textFieldState = state.textFieldState,
        includeLabel = false,
    )

    val textStyleControl = TextStyleDemoControl(
        state = state.textStyleDemoState,
        includeTextFieldControl = false,
    )

    val textStyleControls = Control.ControlColumn(
        name = "TextStyle",
        controls = { textStyleControl.controls },
        indent = true,
        expandedInitial = false,
    )

    val widthControl = Control.Slider(
        name = "Width",
        value = { state.width.value },
        onValueChange = {
            state.width = it.dp
        },
        valueRange = { 0f..state.maxWidth.value },
    )

    val enabledControl = Control.Toggle(
        name = "Enabled",
        value = { state.enabled },
        onValueChange = {
            state.enabled = it
        }
    )

    val keyboardTypeControl = Control.Dropdown(
        name = "Keyboard type",
        values = {
            keyboardTypes.map {
                Control.Dropdown.DropdownItem(
                    name = it.toString(),
                    value = it,
                )
            }.toImmutableList()
        },
        selectedIndex = { keyboardTypes.indexOf(state.keyboardType) },
        onValueChange = {
            state.keyboardType = keyboardTypes[it]
        },
    )

    val keyboardCapitalizationControl = Control.Dropdown(
        name = "Keyboard capitalization",
        values = {
            keyboardCapitalizations.map {
                Control.Dropdown.DropdownItem(
                    name = it.toString(),
                    value = it,
                )
            }.toImmutableList()
        },
        selectedIndex = { keyboardCapitalizations.indexOf(state.keyboardCapitalization) },
        onValueChange = {
            state.keyboardCapitalization = keyboardCapitalizations[it]
        }
    )

    val autoCorrectEnabledControl = Control.Toggle(
        name = "Auto-correct",
        value = { state.autoCorrectEnabled },
        onValueChange = { state.autoCorrectEnabled = it }
    )

    val showKeyboardOnFocusControl = Control.Toggle(
        name = "Show keyboard on focus",
        value = { state.showKeyboardOnFocus },
        onValueChange = { state.showKeyboardOnFocus = it }
    )

    val lineLimitsControl = enumControl(
        name = "Line limits",
        values = { LineLimits.entries },
        selectedValue = { state.lineLimits },
        onValueChange = { state.lineLimits = it },
    )

    val minHeightInLinesControl = Control.Slider(
        name = "Min height in lines",
        value = { state.minHeightInLines.toFloat() },
        onValueChange = { state.minHeightInLines = it.toInt() },
        valueRange = {
            val max = if (state.maxHeightInLines == Int.MAX_VALUE) 100 else state.maxHeightInLines
            1f..max.toFloat()
        },
    )

    val maxHeightInLinesControl = Control.Slider(
        name = "Max height in lines",
        value = {
            if (state.maxHeightInLines == Int.MAX_VALUE) 100f else state.maxHeightInLines.toFloat()
        },
        onValueChange = { state.maxHeightInLines = if (it >= 100f) Int.MAX_VALUE else it.toInt() },
        valueRange = { state.minHeightInLines.toFloat()..100f },
    )

    val inputTransformationControl = enumControl(
        name = "Input Transformations",
        values = { InputTransformations.entries },
        selectedValue = { state.inputTransformation },
        onValueChange = { state.inputTransformation = it },
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
                contentPaddingControl,
            )
        },
    )

    val keyboardControls = persistentListOf(
        keyboardTypeControl,
        keyboardCapitalizationControl,
        autoCorrectEnabledControl,
        showKeyboardOnFocusControl,
    )

    val lineControls
        get() = if (state.lineLimits == LineLimits.Multiline) {
            persistentListOf(
                lineLimitsControl,
                minHeightInLinesControl,
                maxHeightInLinesControl,
            )
        } else persistentListOf(
            lineLimitsControl,
        )

    val controls
        get() = persistentListOf(
            textFieldControl,
            styleControls,
            textStyleControls,
            widthControl,
            enabledControl,
            *keyboardControls.toTypedArray(),
            *lineControls.toTypedArray(),
            inputTransformationControl,
        )

    fun onSizeChanged(width: Dp) {
        if (state.maxWidth == 0.dp) {
            state.width = width
        }
        state.maxWidth = width
        if (state.width > width) {
            state.width = width
        }
    }
}

