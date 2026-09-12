package com.embarrasdf.palette.formats.core

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

class TextFormatOutputTransformation(
    private val textFormat: TextFormat,
) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        val currentText = asCharSequence().toString()
        if (currentText.isEmpty()) return

        val formatted = textFormat.format(currentText)
        if (formatted != currentText) {
            replace(0, length, formatted)
        }
    }
}

val TextFormat.outputTransformation: OutputTransformation
    get() = TextFormatOutputTransformation(this)
