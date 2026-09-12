package com.embarrasdf.palette.theme.semantic.format.money

import com.embarrasdf.palette.formats.money.MoneyFormat

data class MoneyFormatScheme(
    val default: MoneyFormat,
)

fun MoneyFormatToken.toFormat(moneyFormats: MoneyFormatScheme): MoneyFormat {
    return when (this) {
        MoneyFormatToken.Default -> moneyFormats.default
    }
}

fun MoneyFormatScheme.copy(
    token: MoneyFormatToken,
    value: MoneyFormat,
) = this.copy(
    default = if (token == MoneyFormatToken.Default) value else this.default,
)

val PaletteMoneyFormatScheme = MoneyFormatScheme(
    default = MoneyFormatToken.Default.toFormat(),
)
