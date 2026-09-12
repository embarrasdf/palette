package com.embarrasdf.palette.theme.semantic.format.datetime

data class DateTimeFormats(
    val dateFormatScheme: DateFormatScheme,
    val dateTimeFormatScheme: DateTimeFormatScheme,
    val instantFormatScheme: InstantFormatScheme,
    val timeFormatScheme: TimeFormatScheme,
)

val PaletteDateTimeFormats = DateTimeFormats(
    dateFormatScheme = PaletteDateFormatScheme,
    dateTimeFormatScheme = PaletteDateTimeFormatScheme,
    instantFormatScheme = PaletteInstantFormatScheme,
    timeFormatScheme = PaletteTimeFormatScheme,
)
