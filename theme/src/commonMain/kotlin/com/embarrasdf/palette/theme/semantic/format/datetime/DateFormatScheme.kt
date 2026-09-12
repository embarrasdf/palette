package com.embarrasdf.palette.theme.semantic.format.datetime

import com.embarrasdf.palette.formats.datetime.DateFormatValue

data class DateFormatScheme(
    val default: DateFormatValue,
    val long: DateFormatValue,
    val short: DateFormatValue,
)

val PaletteDateFormatScheme = DateFormatScheme(
    default = DateFormatValue.MDY,
    long = DateFormatValue.MDYLong,
    short = DateFormatValue.MDYShort,
)
