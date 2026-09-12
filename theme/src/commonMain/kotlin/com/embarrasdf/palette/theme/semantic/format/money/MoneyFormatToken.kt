package com.embarrasdf.palette.theme.semantic.format.money

import com.embarrasdf.palette.formats.money.MoneyFormat
import com.embarrasdf.palette.theme.semantic.format.core.PaletteNumberFormatScheme

enum class MoneyFormatToken {
    Default,
}

fun MoneyFormatToken.toFormat(): MoneyFormat {
    return when (this) {
        MoneyFormatToken.Default -> MoneyFormat(
            currencySymbol = "$",
            numberFormat = PaletteNumberFormatScheme.currency,
        )
    }
}
