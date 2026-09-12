package com.embarrasdf.palette.theme.semantic.format.datetime

import com.embarrasdf.palette.formats.datetime.DateTimeFormatValue

data class DateTimeFormatScheme(
    val default: DateTimeFormatValue,
    val long: DateTimeFormatValue,
    val short: DateTimeFormatValue,
)

val PaletteDateTimeFormatScheme = DateTimeFormatScheme(
    default = DateTimeFormatValue.MDYContinentalShort,
    long = DateTimeFormatValue.MDYContinental,
    short = DateTimeFormatValue.MDYContinentalShort,
)
