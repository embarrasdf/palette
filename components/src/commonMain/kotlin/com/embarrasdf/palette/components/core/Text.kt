package com.embarrasdf.palette.components.core

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.formats.core.TextFormat
import com.embarrasdf.palette.formats.core.format
import androidx.compose.ui.text.TextStyle as ComposeTextStyle

data class TextStyle(
    val composeTextStyle: ComposeTextStyle = ComposeTextStyle(),
    val format: TextFormat = TextFormat(),
)

fun TextStyle.copy(
    color: Color = this.composeTextStyle.color,
    textAlign: TextAlign = this.composeTextStyle.textAlign,
) = copy(
    composeTextStyle = this.composeTextStyle.copy(
        color = color,
        textAlign = textAlign,
    ),
)

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(),
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    autoSize: TextAutoSize? = null,
) {
    val formattedText = remember(text, style.format) { style.format.format(text) }
    val color = style.composeTextStyle.color
    BasicText(
        text = formattedText,
        modifier = modifier,
        style = style.composeTextStyle.copy(color = color),
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        autoSize = autoSize,
    )
}

@Composable
fun Text(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    autoSize: TextAutoSize? = null,
) {
    val color = style.composeTextStyle.color
    BasicText(
        text = text,
        modifier = modifier,
        style = style.composeTextStyle.copy(color = color),
        onTextLayout = onTextLayout,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        autoSize = autoSize,
    )
}

@Preview
@Composable
private fun Preview() {
    Surface {
        Text(
            text = "Hello world",
        )
    }
}
