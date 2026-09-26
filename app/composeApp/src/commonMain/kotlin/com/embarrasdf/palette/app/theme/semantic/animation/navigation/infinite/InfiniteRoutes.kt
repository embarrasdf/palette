package com.embarrasdf.palette.app.theme.semantic.animation.navigation.infinite

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface InfiniteRoute : NavKey

@Serializable
@SerialName("animation-infinite")
data object InfiniteGraph : InfiniteRoute, NavGraphRoute {
    override val pathSegment = "infinite".toPathSegment()
}

@Serializable
@SerialName("animation-infinite-catalog")
data object InfiniteCatalogRoute : InfiniteRoute {
    override val pathSegment = "catalog".toPathSegment()
}
