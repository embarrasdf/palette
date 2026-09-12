package com.embarrasdf.palette.theme.semantic.format.datetime

import com.embarrasdf.palette.formats.datetime.InstantFormatValue

data class InstantFormatScheme(
    val default: InstantFormatValue,
    val long: InstantFormatValue,
    val short: InstantFormatValue,
)

val PaletteInstantFormatScheme = InstantFormatScheme(
    default = InstantFormatValue.MDYContinentalShort,
    long = InstantFormatValue.MDYContinental,
    short = InstantFormatValue.MDYContinentalShort,
)
