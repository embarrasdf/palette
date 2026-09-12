package com.embarrasdf.palette.app.demo.formats.datetime.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface DateTimeFormatsRoute : NavKey

@Serializable
@SerialName("dateTime")
data object DateTimeFormatsGraph : DateTimeFormatsRoute, NavGraphRoute {
    override val pathSegment = "datetime".toPathSegment()
}

@Serializable
@SerialName("datetime-catalog")
data object DateTimeFormatCatalogRoute : DateTimeFormatsRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("datetime-format")
data class DateTimeFormatRoute(
    override val ordinal: Int,
) : DateTimeFormatsRoute, EnumNavKey<DateTimeFormat> {
    override val entries = DateTimeFormat.entries

    val format get() = value
    constructor(format: DateTimeFormat) : this(format.ordinal)
    constructor(pathSegment: PathSegment) : this(format = pathSegment.toEnumEntry(DateTimeFormat.entries))
}
