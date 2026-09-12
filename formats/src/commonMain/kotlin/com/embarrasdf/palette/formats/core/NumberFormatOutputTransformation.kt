package com.embarrasdf.palette.formats.core

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert

class NumberFormatOutputTransformation(
    private val numberFormat: NumberFormat,
): OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        when (val intGrouping = numberFormat.intGrouping) {
            IntGrouping.None -> return
            is IntGrouping.Uniform -> {
                if (length == 0 || intGrouping.numDigits <= 0) return

                val decimalPos = asCharSequence().indexOf(numberFormat.decimalSeparator)
                val intPartEnd = if (decimalPos >= 0) decimalPos else length

                if (intPartEnd <= intGrouping.numDigits) return

                val groupingSeparatorStr = intGrouping.separator.toString()
                for (index in intPartEnd - intGrouping.numDigits downTo 1 step intGrouping.numDigits) {
                    insert(index, groupingSeparatorStr)
                }
            }
        }
    }
}
