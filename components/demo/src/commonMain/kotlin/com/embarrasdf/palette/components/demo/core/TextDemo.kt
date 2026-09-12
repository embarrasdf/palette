package com.embarrasdf.palette.components.demo.core

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TextDemo(
    modifier: Modifier = Modifier,
    state: TextDemoState = rememberTextDemoState(),
    control: TextDemoControl = rememberTextDemoControl(state),
) {
    Demo(
        controls = control.controls,
        modifier = modifier.fillMaxSize()
    ) {
        TextDemo(
            state = state,
            control = control,
        )
    }
}

@Composable
fun DemoScope.TextDemo(
    modifier: Modifier = Modifier,
    state: TextDemoState = rememberTextDemoState(),
    control: TextDemoControl = rememberTextDemoControl(state),
) {
    val text by state.text.collectAsState(initial = state.text.toString())

    LaunchedEffect(control, this@TextDemo.maxWidth) {
        control.onSizeChanged(this@TextDemo.maxWidth)
    }
    Text(
        text = text,
        style = with(state.textStyleDemoState.textStyle) {
            copy(
                composeTextStyle = composeTextStyle.copy(
                    color = composeTextStyle.color.takeOrElse {
                        PaletteTheme.semantic.color.onSurface
                    },
                    textAlign = state.textAlign.toCompose(),
                    lineHeightStyle = TextDemoState.lineHeightStyleDefault.copy(
                        alignment = state.lineHeightAlignment.toCompose(),
                        trim = state.lineHeightTrim.toCompose(),
                        mode = state.lineHeightMode.toCompose(),
                    )
                ),
            )
        },
        autoSize = if (state.autoSize) TextAutoSize.StepBased() else null,
        softWrap = state.softWrap,
        overflow = when (state.overflow) {
            Overflow.Clip -> TextOverflow.Clip
            Overflow.Ellipsis -> TextOverflow.Ellipsis
            Overflow.Visible -> TextOverflow.Visible
            Overflow.StartEllipsis -> TextOverflow.StartEllipsis
            Overflow.MiddleEllipsis -> TextOverflow.MiddleEllipsis
        },
        modifier = modifier
            .align(Alignment.Center)
            .width(state.width)
            .padding(vertical = PaletteTheme.semantic.dimension.spacing.medium)
            .then(
                if (state.showBorder) Modifier.border(
                    1.dp,
                    PaletteTheme.semantic.color.primary
                )
                else Modifier
            )
    )
}

enum class TextAlign {
    Start,
    Center,
    End,
    Justify,
}

enum class Overflow {
    Clip,
    Ellipsis,
    Visible,
    StartEllipsis,
    MiddleEllipsis,
}

enum class LineHeightAlignment {
    Top,
    Center,
    Proportional,
    Bottom,
}

enum class LineHeightTrim {
    FirstLineTop,
    LastLineBottom,
    Both,
    None,
}

enum class LineHeightMode {
    Fixed,
    Minimum,
}


@Composable
fun rememberTextDemoState(
    initialText: String = "Hello world",
    textStyleInitial: TextStyle = TextStyleDemoDefault.copy(color = PaletteTheme.semantic.color.onSurface),
) = rememberSaveable(saver = TextDemoStateSaver) {
    TextDemoState(initialText, textStyleInitial = textStyleInitial)
}

@Stable
class TextDemoState(
    initialText: String = "Hello world",
    textStyleInitial: TextStyle = TextStyleDemoDefault,
    textAlignInitial: TextAlign = TextAlign.Center,
    lineHeightAlignmentInitial: LineHeightAlignment = lineHeightAlignmentDefault,
    lineHeightTrimInitial: LineHeightTrim = lineHeightTrimDefault,
    lineHeightModeInitial: LineHeightMode = lineHeightModeDefault,
    maxWidthInitial: Dp = 0.dp,
    widthInitial: Dp = maxWidthInitial,
    autoSizeInitial: Boolean = false,
    softWrapInitial: Boolean = false,
    showBorderInitial: Boolean = false,
    overflowInitial: Overflow = Overflow.Clip,
) {
    internal val textFieldState = TextFieldState(initialText = initialText)
    val text get() = snapshotFlow { textFieldState.text.toString() }

    val textStyleDemoState = TextStyleDemoState(
        textStyleInitial = textStyleInitial,
        demoTextFieldState = textFieldState,
    )

    var textAlign by mutableStateOf(textAlignInitial)
        internal set

    var lineHeightAlignment by mutableStateOf(lineHeightAlignmentInitial)
        internal set
    var lineHeightTrim by mutableStateOf(lineHeightTrimInitial)
        internal set

    var lineHeightMode by mutableStateOf(lineHeightModeInitial)
        internal set

    var maxWidth by mutableStateOf(maxWidthInitial)
        internal set
    var width by mutableStateOf(widthInitial)
        internal set

    var autoSize by mutableStateOf(autoSizeInitial)
        internal set
    var softWrap by mutableStateOf(softWrapInitial)
        internal set
    var showBorder by mutableStateOf(showBorderInitial)
        internal set
    var overflow by mutableStateOf(overflowInitial)
        internal set

    companion object {
        val lineHeightStyleDefault = LineHeightStyle.Default
        val lineHeightAlignmentDefault =
            lineHeightStyleDefault.alignment.toDomain() ?: LineHeightAlignment.Proportional
        val lineHeightTrimDefault = lineHeightStyleDefault.trim.toDomain() ?: LineHeightTrim.Both
        val lineHeightModeDefault = lineHeightStyleDefault.mode.toDomain() ?: LineHeightMode.Fixed
    }
}

private const val textStyleDemoKey = "textStyleDemo"
private const val textAlignKey = "textAlign"
private const val lineHeightAlignmentKey = "lineHeightAlignment"
private const val lineHeightTrimKey = "lineHeightTrim"
private const val lineHeightModeKey = "lineHeightMode"
private const val maxWidthKey = "maxWidth"
private const val widthKey = "width"
private const val autoSizeKey = "autoSize"
private const val softWrapKey = "softWrap"
private const val showBorderKey = "showBorder"
private const val overflowKey = "overflow"

val TextDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            textStyleDemoKey to save(value.textStyleDemoState, TextStyleDemoStateSaver, this),
            textAlignKey to value.textAlign.name,
            lineHeightAlignmentKey to value.lineHeightAlignment.name,
            lineHeightTrimKey to value.lineHeightTrim.name,
            lineHeightModeKey to value.lineHeightMode.name,
            maxWidthKey to value.maxWidth.value,
            widthKey to value.width.value,
            autoSizeKey to value.autoSize,
            softWrapKey to value.softWrap,
            showBorderKey to value.showBorder,
            overflowKey to value.overflow.name,
        )
    },
    restore = { map ->
        val textStyleDemoState: TextStyleDemoState = restore(map[textStyleDemoKey], TextStyleDemoStateSaver)!!

        TextDemoState(
            textStyleInitial = textStyleDemoState.textStyle,
            textAlignInitial = TextAlign.valueOf(map[textAlignKey] as String),
            lineHeightAlignmentInitial =
                LineHeightAlignment.valueOf(map[lineHeightAlignmentKey] as String),
            lineHeightTrimInitial =
                LineHeightTrim.valueOf(map[lineHeightTrimKey] as String),
            lineHeightModeInitial =
                LineHeightMode.valueOf(map[lineHeightModeKey] as String),
            maxWidthInitial = (map[maxWidthKey] as Float).dp,
            widthInitial = (map[widthKey] as Float).dp,
            autoSizeInitial = map[autoSizeKey] as Boolean,
            softWrapInitial = map[softWrapKey] as Boolean,
            showBorderInitial = map[showBorderKey] as Boolean,
            overflowInitial = Overflow.valueOf(map[overflowKey] as String),
        )
    }
)

@Composable
fun rememberTextDemoControl(
    textDemoState: TextDemoState,
) = remember(textDemoState) {
    TextDemoControl(textDemoState)
}

@Stable
class TextDemoControl(
    private var state: TextDemoState,
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

    val textAlignControl = enumControl(
        name = "Alignment",
        values = { TextAlign.entries },
        selectedValue = { state.textAlign },
        onValueChange = { state.textAlign = it },
    )

    val lineHeightAlignmentControl = enumControl(
        name = "Line height alignment",
        values = { LineHeightAlignment.entries },
        selectedValue = { state.lineHeightAlignment },
        onValueChange = { state.lineHeightAlignment = it },
    )

    val lineHeightTrimControl = enumControl(
        name = "Line height trim",
        values = { LineHeightTrim.entries },
        selectedValue = { state.lineHeightTrim },
        onValueChange = { state.lineHeightTrim = it },
    )

    val lineHeightModeControl = enumControl(
        name = "Line height mode",
        values = { LineHeightMode.entries },
        selectedValue = { state.lineHeightMode },
        onValueChange = { state.lineHeightMode = it },
    )

    val widthControl = Control.Slider(
        name = "Width",
        value = { state.width.value },
        onValueChange = {
            state.width = it.dp
        },
        valueRange = { 0f..state.maxWidth.value },
    )

    val autoSizeControl = Control.Toggle(
        name = "Auto-size text",
        value = { state.autoSize },
        onValueChange = {
            state.autoSize = it
        }
    )

    val softWrapControl = Control.Toggle(
        name = "Soft wrap",
        value = { state.softWrap },
        onValueChange = {
            state.softWrap = it
        }
    )

    val showBorderControl = Control.Toggle(
        name = "Show border",
        value = { state.showBorder },
        onValueChange = {
            state.showBorder = it
        }
    )

    val overflowControl = enumControl(
        name = "Overflow",
        values = { Overflow.entries },
        selectedValue = { state.overflow },
        onValueChange = { state.overflow = it },
    )

    val controls = persistentListOf(
        textFieldControl,
        textStyleControls,
        textAlignControl,
        lineHeightAlignmentControl,
        lineHeightTrimControl,
        lineHeightModeControl,
        widthControl,
        autoSizeControl,
        softWrapControl,
        showBorderControl,
        overflowControl,
    )

    fun onSizeChanged(width: Dp) {
        if (state.maxWidth == 0.dp) {
            state.width = width
        }
        state.maxWidth = width
        if (state.width > state.maxWidth) {
            state.width = state.maxWidth
        }
    }

    fun updateTextStyle(textStyle: TextStyle) {
        textStyleControl.updateStyle(textStyle)
    }
}

private fun TextAlign.toCompose() = when (this) {
    TextAlign.Start -> androidx.compose.ui.text.style.TextAlign.Start
    TextAlign.Center -> androidx.compose.ui.text.style.TextAlign.Center
    TextAlign.End -> androidx.compose.ui.text.style.TextAlign.End
    TextAlign.Justify -> androidx.compose.ui.text.style.TextAlign.Justify
}

private fun LineHeightAlignment.toCompose() = when (this) {
    LineHeightAlignment.Top -> LineHeightStyle.Alignment.Top
    LineHeightAlignment.Center -> LineHeightStyle.Alignment.Center
    LineHeightAlignment.Proportional -> LineHeightStyle.Alignment.Proportional
    LineHeightAlignment.Bottom -> LineHeightStyle.Alignment.Bottom
}

private fun LineHeightStyle.Alignment.toDomain() = when (this) {
    LineHeightStyle.Alignment.Top -> LineHeightAlignment.Top
    LineHeightStyle.Alignment.Center -> LineHeightAlignment.Center
    LineHeightStyle.Alignment.Proportional -> LineHeightAlignment.Proportional
    LineHeightStyle.Alignment.Bottom -> LineHeightAlignment.Bottom
    else -> null
}

private fun LineHeightTrim.toCompose() = when (this) {
    LineHeightTrim.FirstLineTop -> LineHeightStyle.Trim.FirstLineTop
    LineHeightTrim.LastLineBottom -> LineHeightStyle.Trim.LastLineBottom
    LineHeightTrim.Both -> LineHeightStyle.Trim.Both
    LineHeightTrim.None -> LineHeightStyle.Trim.None
}

private fun LineHeightStyle.Trim.toDomain() = when (this) {
    LineHeightStyle.Trim.FirstLineTop -> LineHeightTrim.FirstLineTop
    LineHeightStyle.Trim.LastLineBottom -> LineHeightTrim.LastLineBottom
    LineHeightStyle.Trim.Both -> LineHeightTrim.Both
    LineHeightStyle.Trim.None -> LineHeightTrim.None
    else -> null
}

private fun LineHeightMode.toCompose() = when (this) {
    LineHeightMode.Fixed -> LineHeightStyle.Mode.Fixed
    LineHeightMode.Minimum -> LineHeightStyle.Mode.Minimum
}

private fun LineHeightStyle.Mode.toDomain() = when (this) {
    LineHeightStyle.Mode.Fixed -> LineHeightMode.Fixed
    LineHeightStyle.Mode.Minimum -> LineHeightMode.Minimum
    else -> null
}
