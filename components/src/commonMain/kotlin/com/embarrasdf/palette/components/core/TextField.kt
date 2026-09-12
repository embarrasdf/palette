package com.embarrasdf.palette.components.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.embarrasdf.palette.formats.core.inputTransformation

data class TextFieldStyle(
    val textStyle: TextStyle = TextStyle(),
    val cursorBrush: Brush = SolidColor(Color.Unspecified),
    val borderStroke: BorderStroke = BorderStroke(1.dp, Color.Unspecified),
    val contentPadding: PaddingValues = PaddingValues(8.dp),
)

@Composable
fun TextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    style: TextFieldStyle = TextFieldStyle(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    decorator: TextFieldDecorator? = null,
    scrollState: ScrollState = rememberScrollState(),
) {
    val textStyle = style.textStyle
    val resolvedDecorator = decorator
        ?: TextFieldDefaults.textFieldDecorator(style.borderStroke, style.contentPadding)
    val color = textStyle.composeTextStyle.color

    val formatInputTransformation = textStyle.format.inputTransformation
    val combinedInputTransformation = when {
        inputTransformation != null -> inputTransformation.then(formatInputTransformation)
        else -> formatInputTransformation
    }

    BasicTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        inputTransformation = combinedInputTransformation,
        textStyle = textStyle.composeTextStyle.copy(color = color),
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = style.cursorBrush,
        outputTransformation = outputTransformation,
        decorator = resolvedDecorator,
        scrollState = scrollState,
    )
}

object TextFieldDefaults {
    val CursorBrush: Brush = SolidColor(Color.Unspecified)

    val BorderStroke: BorderStroke = BorderStroke(
        width = 1.dp,
        color = Color.Unspecified,
    )

    val Padding = PaddingValues(8.dp)

    @Composable
    fun textFieldDecorator(
        borderStroke: BorderStroke = BorderStroke,
        padding: PaddingValues = Padding,
    ): TextFieldDecorator = TextFieldDecorator { innerTextField ->
        Box(
            modifier = Modifier
                .border(borderStroke)
                .padding(padding),
        ) {
            innerTextField()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Surface {
        TextField(
            state = rememberTextFieldState("text"),
        )
    }
}
