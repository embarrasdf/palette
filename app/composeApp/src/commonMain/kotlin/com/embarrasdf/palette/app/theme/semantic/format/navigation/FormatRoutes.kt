package com.embarrasdf.palette.app.theme.semantic.format.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface FormatRoute : NavKey

@Serializable
@SerialName("formats")
data object FormatsGraph : FormatRoute, NavGraphRoute {
    override val pathSegment = "formats".toPathSegment()
}

@Serializable
@SerialName("format-catalog")
data object FormatCatalogRoute : FormatRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("moneyFormat")
data object MoneyFormatRoute : FormatRoute {
    override val pathSegment = "money".toPathSegment()
}

@Serializable
@SerialName("numberFormat")
data object NumberFormatRoute : FormatRoute {
    override val pathSegment = "number".toPathSegment()
}

@Serializable
@SerialName("textFormat")
data object TextFormatRoute : FormatRoute {
    override val pathSegment = "text".toPathSegment()
}
