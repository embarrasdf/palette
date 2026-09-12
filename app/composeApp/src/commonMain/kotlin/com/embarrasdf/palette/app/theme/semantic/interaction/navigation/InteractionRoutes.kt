package com.embarrasdf.palette.app.theme.semantic.interaction.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface InteractionRoute : NavKey

@Serializable
@SerialName("interaction")
data object InteractionGraph : InteractionRoute, NavGraphRoute {
    override val pathSegment = "interaction".toPathSegment()
}

@Serializable
@SerialName("interaction-catalog")
data object InteractionCatalogRoute : InteractionRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("indication")
data object IndicationRoute : InteractionRoute {
    override val pathSegment = "indication".toPathSegment()
}
