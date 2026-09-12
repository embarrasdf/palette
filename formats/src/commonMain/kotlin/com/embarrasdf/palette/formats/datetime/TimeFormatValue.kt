package com.embarrasdf.palette.formats.datetime

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat

enum class TimeFormatValue {
    HMContinental,
    HMAmPmPadZero,
    HMSContinental,
    HMSAmPmPadZero,
}

fun TimeFormatValue.toFormat(): DateTimeFormat<LocalTime> = when (this) {
    TimeFormatValue.HMContinental -> LocalTime.Formats.HMContinental
    TimeFormatValue.HMAmPmPadZero -> LocalTime.Formats.HMAmPmPadZero
    TimeFormatValue.HMSContinental -> LocalTime.Formats.HMSContinental
    TimeFormatValue.HMSAmPmPadZero -> LocalTime.Formats.HMSAmPmPadZero
}
