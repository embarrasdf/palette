package com.embarrasdf.palette.theme.semantic.format.core

import com.embarrasdf.palette.formats.core.NumberFormat

data class NumberFormatScheme(
    val default: NumberFormat,
    val currency: NumberFormat,
)

fun NumberFormatScheme.update(
    token: NumberFormatToken,
    value: NumberFormat,
) = this.copy(
    default = if (token == NumberFormatToken.Default) value else this.default,
    currency = if (token == NumberFormatToken.Currency) value else this.currency,
)

val PaletteNumberFormatScheme = NumberFormatScheme(
    default = NumberFormatToken.Default.toFormat(),
    currency = NumberFormatToken.Currency.toFormat(),
)
