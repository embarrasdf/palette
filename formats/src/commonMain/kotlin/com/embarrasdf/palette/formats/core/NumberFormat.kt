package com.embarrasdf.palette.formats.core

data class NumberFormat(
    val numDecimalValuesRange: IntRange = 0..2,
    val positiveSign: String? = null,
    val negativeSign: String = "-",
    val decimalSeparator: Char = '.',
    val intGrouping: IntGrouping = IntGrouping.Uniform(numDigits = 3, separator = ','),
) {
    val minNumDecimalValues: Int
        get() = numDecimalValuesRange.first
    val maxNumDecimalValues: Int
        get() = numDecimalValuesRange.last
}

sealed class IntGrouping {
    data class Uniform(val numDigits: Int, val separator: Char) : IntGrouping()
    data object None : IntGrouping()
}

fun NumberFormat.format(
    amount: Double,
): String {
    return format(amount.toString())
}

private const val DOUBLE_VALUE_INFINITY_LOWERCASE = "infinity"
private const val DOUBLE_VALUE_NEGATIVE_INFINITY_LOWERCASE = "-infinity"
private const val DOUBLE_VALUE_NAN_LOWERCASE = "nan"
private const val DOUBLE_VALUE_SCIENTIFIC_NOTATION_CHAR_LOWERCASE = 'e'
private const val MINUS_SIGN_ASCII = '-'
private const val MINUS_SIGN_UNICODE = '−'
private const val PLUS_SIGN_ASCII = '+'
private const val PLUS_SIGN_UNICODE = '＋'

fun NumberFormat.format(
    amount: String,
): String {
    if (amount.isEmpty()) return ""

    val amountLower = amount.lowercase()
    if (amountLower.contains(DOUBLE_VALUE_SCIENTIFIC_NOTATION_CHAR_LOWERCASE) ||
        amountLower == DOUBLE_VALUE_INFINITY_LOWERCASE ||
        amountLower == DOUBLE_VALUE_NEGATIVE_INFINITY_LOWERCASE ||
        amountLower == DOUBLE_VALUE_NAN_LOWERCASE) {
        return amount
    }

    val isNegative = amount.startsWith(MINUS_SIGN_ASCII) || amount.startsWith(MINUS_SIGN_UNICODE)

    val absAmountStr = amount.trimStart(MINUS_SIGN_ASCII, MINUS_SIGN_UNICODE, PLUS_SIGN_ASCII, PLUS_SIGN_UNICODE)
    val numericValue = absAmountStr.toDoubleOrNull() ?: 0.0

    val sign = when {
        isNegative -> negativeSign
        numericValue == 0.0 -> ""
        else -> positiveSign.orEmpty()
    }

    val (rawIntPart, fracPart) = splitNumberParts(absAmountStr)
    val intPart = normalizeIntegerPart(rawIntPart)

    return format(
        sign = sign,
        intPart = intPart,
        fracPart = fracPart,
    )
}

fun NumberFormat.format(
    sign: String = "",
    intPart: String,
    fracPart: String? = null,
): String {
    val decimalPart = if (fracPart != null || minNumDecimalValues > 0) {
        val truncated = (fracPart ?: "").take(maxNumDecimalValues)

        // Trim trailing zeros but keep at least minNumDecimalValues
        val trimmed = truncated.trimEnd('0')
        if (trimmed.length < minNumDecimalValues) {
            trimmed.padEnd(minNumDecimalValues, '0')
        } else if (trimmed.isEmpty() && minNumDecimalValues == 0) {
            null
        } else {
            trimmed
        }
    } else {
        null
    }

    val formattedIntPart = applyIntGrouping(intPart, intGrouping)

    return if (decimalPart != null) {
        val decimalSeparatorStr = decimalSeparator.toString()
        "$sign$formattedIntPart$decimalSeparatorStr$decimalPart"
    } else {
        "$sign$formattedIntPart"
    }
}

internal fun splitNumberParts(
    numberString: String,
    decimalSeparator: Char = '.',
): Pair<String, String?> {
    val parts = numberString.split(decimalSeparator, '.')
    val intPart = parts.getOrNull(0) ?: ""
    val fracPart = parts.getOrNull(1)
    return intPart to fracPart
}

internal fun normalizeIntegerPart(intPart: String): String {
    return intPart.trimStart('0').ifEmpty { "0" }
}

private fun applyIntGrouping(
    intPart: String,
    intGrouping: IntGrouping,
): String {
    when (intGrouping) {
        is IntGrouping.None -> return intPart
        is IntGrouping.Uniform -> {
            if (intPart.isEmpty() || intGrouping.numDigits <= 0) return intPart

            return intPart.reversed()
                .chunked(intGrouping.numDigits)
                .joinToString(intGrouping.separator.toString())
                .reversed()
        }
    }
}
