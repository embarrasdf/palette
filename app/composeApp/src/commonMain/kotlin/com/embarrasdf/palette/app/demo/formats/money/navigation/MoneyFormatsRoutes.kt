package com.embarrasdf.palette.app.demo.formats.money.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface MoneyFormatsRoute : NavKey

@Serializable
@SerialName("money-formats")
data object MoneyFormatsGraph : MoneyFormatsRoute, NavGraphRoute {
    override val pathSegment = "money".toPathSegment()
}

@Serializable
@SerialName("money-format-catalog")
data object MoneyFormatCatalogRoute : MoneyFormatsRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("money-format")
data class MoneyFormatRoute(
    override val ordinal: Int,
) : MoneyFormatsRoute, EnumNavKey<MoneyFormat> {
    override val entries = MoneyFormat.entries

    val format get() = value
    constructor(format: MoneyFormat) : this(format.ordinal)
    constructor(pathSegment: PathSegment) : this(format = pathSegment.toEnumEntry(MoneyFormat.entries))
}
