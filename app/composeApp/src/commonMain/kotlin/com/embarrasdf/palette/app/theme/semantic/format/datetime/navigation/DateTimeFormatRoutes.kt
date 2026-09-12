package com.embarrasdf.palette.app.theme.semantic.format.datetime.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface DateTimeFormatRoute : NavKey

@Serializable
@SerialName("datetime-graph")
data object DateTimeFormatGraph : DateTimeFormatRoute, NavGraphRoute {
    override val pathSegment = "datetime".toPathSegment()
}

@Serializable
@SerialName("datetime-theme")
data object DateTimeFormatCatalogRoute : DateTimeFormatRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("dateTimeFormat")
data class DateTimeFormatItemRoute(
    override val ordinal: Int,
) : EnumNavKey<DateTimeFormatCatalogItem>, DateTimeFormatRoute {
    override val entries = DateTimeFormatCatalogItem.entries

    constructor(item: DateTimeFormatCatalogItem) : this(item.ordinal)
    constructor(pathSegment: PathSegment) : this(item = pathSegment.toEnumEntry(DateTimeFormatCatalogItem.entries))
}
