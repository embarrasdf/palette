package com.embarrasdf.palette.app.theme.primitive.interaction.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface PrimitiveInteractionRoute : NavKey

@Serializable
@SerialName("theme-primitive-interaction")
data object PrimitiveInteractionGraph : PrimitiveInteractionRoute, NavGraphRoute {
    override val pathSegment = "interaction".toPathSegment()
}

@Serializable
@SerialName("theme-primitive-interaction-catalog")
data object PrimitiveInteractionCatalogRoute : PrimitiveInteractionRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("theme-primitive-indication")
data object PrimitiveIndicationRoute : PrimitiveInteractionRoute {
    override val pathSegment = "indication".toPathSegment()
}
