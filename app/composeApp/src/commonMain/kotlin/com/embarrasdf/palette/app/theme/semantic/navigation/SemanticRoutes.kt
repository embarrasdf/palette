package com.embarrasdf.palette.app.theme.semantic.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface SemanticRoute : NavKey

@Serializable
@SerialName("theme-semantic")
data object SemanticGraph : SemanticRoute, NavGraphRoute {
    override val pathSegment = "semantic".toPathSegment()
}

@Serializable
@SerialName("theme-semantic-catalog")
data object SemanticCatalogRoute : SemanticRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("theme-color")
data object ColorRoute : SemanticRoute {
    override val pathSegment = "color".toPathSegment()
}

@Serializable
@SerialName("shape")
data object ShapeRoute : SemanticRoute {
    override val pathSegment = "shape".toPathSegment()
}

@Serializable
@SerialName("typography")
data object TypographyRoute : SemanticRoute {
    override val pathSegment = "typography".toPathSegment()
}
