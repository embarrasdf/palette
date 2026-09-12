package com.embarrasdf.palette.theme.semantic.format.core

import com.embarrasdf.palette.formats.core.NumberFormat

enum class NumberFormatToken {
    Default,
    Currency,
}

fun NumberFormatToken.toFormat(): NumberFormat {
    return when (this) {
        NumberFormatToken.Default -> NumberFormat()
        NumberFormatToken.Currency -> NumberFormat(
            numDecimalValuesRange = 2..2,
        )
    }
}
