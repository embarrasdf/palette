package com.embarrasdf.palette.app.demo.formats.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface FormatsRoute : NavKey

@Serializable
@SerialName("demo-formats")
data object FormatsGraph : FormatsRoute, NavGraphRoute {
    override val pathSegment = "formats".toPathSegment()
}

@Serializable
@SerialName("demo-format-catalog")
data object FormatCatalogRoute : FormatsRoute {
    override val pathSegment = "catalog".toPathSegment()
}
