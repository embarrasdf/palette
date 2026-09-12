package com.embarrasdf.palette.app.demo.components.geometry.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface GeometryComponentsRoute : NavKey

@Serializable
@SerialName("geometry")
data object GeometryComponentsGraph : GeometryComponentsRoute, NavGraphRoute {
    override val pathSegment = "geometry".toPathSegment()
}

@Serializable
@SerialName("geometry-component-catalog")
data object GeometryComponentCatalogRoute : GeometryComponentsRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("geometry-component")
data class GeometryComponentRoute(
    override val ordinal: Int,
) : GeometryComponentsRoute, EnumNavKey<GeometryComponent> {
    override val entries = GeometryComponent.entries

    val component get() = value
    constructor(component: GeometryComponent) : this(component.ordinal)
    constructor(pathSegment: PathSegment) : this(component = pathSegment.toEnumEntry(GeometryComponent.entries))
}
