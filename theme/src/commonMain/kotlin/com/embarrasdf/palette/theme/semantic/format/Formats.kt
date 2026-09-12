package com.embarrasdf.palette.theme.semantic.format

import com.embarrasdf.palette.theme.semantic.format.core.NumberFormatScheme
import com.embarrasdf.palette.theme.semantic.format.core.PaletteNumberFormatScheme
import com.embarrasdf.palette.theme.semantic.format.core.TextFormatScheme
import com.embarrasdf.palette.theme.semantic.format.core.PaletteTextFormatScheme
import com.embarrasdf.palette.theme.semantic.format.datetime.DateTimeFormats
import com.embarrasdf.palette.theme.semantic.format.datetime.PaletteDateTimeFormats
import com.embarrasdf.palette.theme.semantic.format.money.MoneyFormatScheme
import com.embarrasdf.palette.theme.semantic.format.money.PaletteMoneyFormatScheme

data class Formats(
    val dateTimeFormats: DateTimeFormats,
    val moneyFormats: MoneyFormatScheme,
    val numberFormats: NumberFormatScheme,
    val textFormats: TextFormatScheme,
)

val PaletteFormats = Formats(
    dateTimeFormats = PaletteDateTimeFormats,
    moneyFormats = PaletteMoneyFormatScheme,
    numberFormats = PaletteNumberFormatScheme,
    textFormats = PaletteTextFormatScheme,
)
