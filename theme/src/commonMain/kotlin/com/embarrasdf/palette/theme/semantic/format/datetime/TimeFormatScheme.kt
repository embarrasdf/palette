package com.embarrasdf.palette.theme.semantic.format.datetime

import com.embarrasdf.palette.formats.datetime.TimeFormatValue

data class TimeFormatScheme(
    val default: TimeFormatValue,
    val long: TimeFormatValue,
    val short: TimeFormatValue,
)

val PaletteTimeFormatScheme = TimeFormatScheme(
    default = TimeFormatValue.HMAmPmPadZero,
    long = TimeFormatValue.HMSAmPmPadZero,
    short = TimeFormatValue.HMAmPmPadZero,
)
