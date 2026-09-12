package com.embarrasdf.palette.formats.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

enum class InstantFormatValue {
    MDYContinental,
    MDYContinentalShort,
    YMDContinental,
    YMDContinentalShort,
    ContinentalMDY,
    ContinentalYMD,
}

fun InstantFormatValue.toFormat(): DateTimeFormat<LocalDateTime> = when (this) {
    InstantFormatValue.MDYContinental -> LocalDateTime.Formats.MDYContinental
    InstantFormatValue.MDYContinentalShort -> LocalDateTime.Formats.MDYContinentalShort
    InstantFormatValue.YMDContinental -> LocalDateTime.Formats.YMDContinental
    InstantFormatValue.YMDContinentalShort -> LocalDateTime.Formats.YMDContinentalShort
    InstantFormatValue.ContinentalMDY -> LocalDateTime.Formats.ContinentalMDY
    InstantFormatValue.ContinentalYMD -> LocalDateTime.Formats.ContinentalYMD
}
