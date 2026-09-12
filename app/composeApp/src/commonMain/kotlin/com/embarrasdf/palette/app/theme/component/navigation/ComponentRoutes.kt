package com.embarrasdf.palette.app.theme.component.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ComponentRoute : NavKey

@Serializable
@SerialName("theme-component")
data object ComponentGraph : ComponentRoute, NavGraphRoute {
    override val pathSegment = "component".toPathSegment()
}

@Serializable
@SerialName("theme-component-catalog")
data object ComponentCatalogRoute : ComponentRoute {
    override val pathSegment = "catalog".toPathSegment()
}
