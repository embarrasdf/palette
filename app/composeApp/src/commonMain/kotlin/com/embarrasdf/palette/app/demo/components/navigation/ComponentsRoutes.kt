package com.embarrasdf.palette.app.demo.components.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ComponentsRoute : NavKey

@Serializable
@SerialName("components")
data object ComponentsGraph : ComponentsRoute, NavGraphRoute {
    override val pathSegment = "components".toPathSegment()
}

@Serializable
@SerialName("component-catalog")
data object ComponentCatalogRoute : ComponentsRoute {
    override val pathSegment = "catalog".toPathSegment()
}
