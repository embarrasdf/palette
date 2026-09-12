package com.embarrasdf.palette.app.theme.semantic.dimension.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface DimensionRoute : NavKey

@Serializable
@SerialName("dimension")
data object DimensionGraph : DimensionRoute, NavGraphRoute {
    override val pathSegment = "dimension".toPathSegment()
}

@Serializable
@SerialName("dimension-catalog")
data object DimensionCatalogRoute : DimensionRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("spacing")
data object SpacingRoute : DimensionRoute {
    override val pathSegment = "spacing".toPathSegment()
}

@Serializable
@SerialName("padding")
data object PaddingRoute : DimensionRoute {
    override val pathSegment = "padding".toPathSegment()
}

@Serializable
@SerialName("size")
data object SizeRoute : DimensionRoute {
    override val pathSegment = "size".toPathSegment()
}
