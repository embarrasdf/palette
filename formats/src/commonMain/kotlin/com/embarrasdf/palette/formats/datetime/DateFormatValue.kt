package com.embarrasdf.palette.formats.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat

enum class DateFormatValue {
    YMD,
    YMDShort,
    MDY,
    MDYShort,
    MDYLong,
}

fun DateFormatValue.toFormat(): DateTimeFormat<LocalDate> = when (this) {
    DateFormatValue.YMD -> LocalDate.Formats.YMD
    DateFormatValue.YMDShort -> LocalDate.Formats.YMDShort
    DateFormatValue.MDY -> LocalDate.Formats.MDY
    DateFormatValue.MDYShort -> LocalDate.Formats.MDYShort
    DateFormatValue.MDYLong -> LocalDate.Formats.MDYLong
}
