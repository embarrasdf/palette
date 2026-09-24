package com.embarrasdf.palette.app.theme.semantic.animation.navigation.finite

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface FiniteRoute : NavKey

@Serializable
@SerialName("animation-finite")
data object FiniteGraph : FiniteRoute, NavGraphRoute {
    override val pathSegment = "finite".toPathSegment()
}

@Serializable
@SerialName("animation-finite-catalog")
data object FiniteCatalogRoute : FiniteRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("animation-default")
data object DefaultRoute : FiniteRoute {
    override val pathSegment = "default".toPathSegment()
}
