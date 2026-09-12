package com.embarrasdf.palette.formats.core

import androidx.annotation.CheckResult
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.delete
import androidx.compose.ui.text.input.KeyboardType
import kotlin.math.min

class NumberFormatInputTransformation(
    numberFormat: NumberFormat,
    private val maxNumDecimalValues: Int? = null,
) : InputTransformation {

    private val decimalSeparator = numberFormat.decimalSeparator

    override val keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
    )

    override fun TextFieldBuffer.transformInput() {
        filterChars()
        filterConsecutiveDecimals()

        val (intPart, decimalPart) = splitNumberParts(
            numberString = asCharSequence().toString(),
            decimalSeparator = decimalSeparator,
        )

        val numSeparators = asCharSequence().count { it == decimalSeparator }
        if (numSeparators > 1) {
            // Instead of rejecting changes with multiple decimals, recalculate according to the
            // first one.
            replace(intPart.length + 1, length, decimalPart ?: "")
        }

        val filteredIntPart = filterIntPart(intPart, hasDecimalPart = decimalPart != null)

        filterDecimalPart(decimalPart, startIndex = filteredIntPart.length + 1)
    }

    private fun TextFieldBuffer.filterChars() {
        val proposed = asCharSequence()
        if (proposed.any { !it.isDigit() && it != decimalSeparator }) {
            // Reject changes for any non-digit, non-decimal characters
            revertAllChanges()
        }
    }

    private fun TextFieldBuffer.filterConsecutiveDecimals() {
        val proposed = asCharSequence()
        var prevChar = proposed.firstOrNull()
        for (char in proposed.drop(1)) {
            if (prevChar == decimalSeparator && char == decimalSeparator) {
                // Reject changes for consecutive decimals
                revertAllChanges()
                return
            }
            prevChar = char
        }
    }

    @CheckResult
    private fun TextFieldBuffer.filterIntPart(
        intPart: String,
        hasDecimalPart: Boolean,
    ): String {
        var mutableIntPart = intPart

        if (mutableIntPart.startsWith('0')) {
            // Allow single leading zero. Replace leading zero if followed by another digit.
            val newIntPart = normalizeIntegerPart(mutableIntPart)
            replace(0, mutableIntPart.length, newIntPart)
            mutableIntPart = newIntPart
        }

        if (mutableIntPart.isEmpty() && hasDecimalPart) {
            // Prefill 0 when only decimal part is entered
            val newIntPart = "0"
            replace(0, mutableIntPart.length, newIntPart)
            mutableIntPart = newIntPart
        }

        return mutableIntPart
    }

    private fun TextFieldBuffer.filterDecimalPart(
        decimalPart: String?,
        startIndex: Int,
    ) {
        if (decimalPart == null) return
        if (maxNumDecimalValues == null) return

        if (decimalPart.length > maxNumDecimalValues) {
            // Replace chars one-by-one so the cursor advances as expected
            for (index in startIndex until min(maxNumDecimalValues, decimalPart.length)) {
                replace(index, index + 1, decimalPart[index].toString())
            }
            delete(startIndex + maxNumDecimalValues, length)
        }
    }
}
