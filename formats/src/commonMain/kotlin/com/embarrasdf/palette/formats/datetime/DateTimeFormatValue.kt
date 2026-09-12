package com.embarrasdf.palette.formats.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

enum class DateTimeFormatValue {
    MDYContinental,
    MDYContinentalShort,
    YMDContinental,
    YMDContinentalShort,
    ContinentalMDY,
    ContinentalYMD,
}

fun DateTimeFormatValue.toFormat(): DateTimeFormat<LocalDateTime> = when (this) {
    DateTimeFormatValue.MDYContinental -> LocalDateTime.Formats.MDYContinental
    DateTimeFormatValue.MDYContinentalShort -> LocalDateTime.Formats.MDYContinentalShort
    DateTimeFormatValue.YMDContinental -> LocalDateTime.Formats.YMDContinental
    DateTimeFormatValue.YMDContinentalShort -> LocalDateTime.Formats.YMDContinentalShort
    DateTimeFormatValue.ContinentalMDY -> LocalDateTime.Formats.ContinentalMDY
    DateTimeFormatValue.ContinentalYMD -> LocalDateTime.Formats.ContinentalYMD
}
