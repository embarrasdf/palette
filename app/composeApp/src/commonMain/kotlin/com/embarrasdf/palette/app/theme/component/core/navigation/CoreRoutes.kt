package com.embarrasdf.palette.app.theme.component.core.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface CoreRoute : NavKey

@Serializable
@SerialName("theme-core")
data object CoreGraph : CoreRoute, NavGraphRoute {
    override val pathSegment = "core".toPathSegment()
}

@Serializable
@SerialName("theme-core-catalog")
data object CoreCatalogRoute : CoreRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("borderStyles")
data object BorderStylesRoute : CoreRoute {
    override val pathSegment = "border".toPathSegment()
}

@Serializable
@SerialName("buttonStyles")
data object ButtonStylesRoute : CoreRoute {
    override val pathSegment = "button".toPathSegment()
}

@Serializable
@SerialName("surfaceStyles")
data object SurfaceStylesRoute : CoreRoute {
    override val pathSegment = "surface".toPathSegment()
}

@Serializable
@SerialName("textStyles")
data object TextStylesRoute : CoreRoute {
    override val pathSegment = "text".toPathSegment()
}
