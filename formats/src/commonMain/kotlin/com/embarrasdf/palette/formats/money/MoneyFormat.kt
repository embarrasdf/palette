package com.embarrasdf.palette.formats.money

import com.embarrasdf.palette.formats.core.NumberFormat
import com.embarrasdf.palette.formats.core.format

data class MoneyFormat(
    val currencySymbol: String? = "$",
    val numberFormat: NumberFormat = NumberFormat(),
)

fun MoneyFormat.update(
    currencySymbol: String? = null,
    numberFormat: NumberFormat? = null,
): MoneyFormat = this.copy(
    currencySymbol = currencySymbol ?: this.currencySymbol,
    numberFormat = numberFormat ?: this.numberFormat,
)

fun MoneyFormat.format(
    amount: Double,
): String {
    return format(amount.toString())
}

fun MoneyFormat.format(
    amount: String,
): String {
    val formattedNumber = numberFormat.format(amount)

    return if (currencySymbol != null) {
        "$currencySymbol$formattedNumber"
    } else {
        formattedNumber
    }
}
