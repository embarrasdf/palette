package com.embarrasdf.palette.formats.core

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

class TextFormatInputTransformation(
    private val textFormat: TextFormat,
) : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val currentText = asCharSequence().toString()
        if (currentText.isEmpty()) return

        val formatted = textFormat.format(currentText)
        if (formatted != currentText) {
            replace(0, length, formatted)
        }
    }
}

val TextFormat.inputTransformation: InputTransformation
    get() = TextFormatInputTransformation(this)
