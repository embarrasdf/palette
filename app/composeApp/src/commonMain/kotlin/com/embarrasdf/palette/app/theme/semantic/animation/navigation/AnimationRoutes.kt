package com.embarrasdf.palette.app.theme.semantic.animation.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AnimationRoute : NavKey

@Serializable
@SerialName("animation")
data object AnimationGraph : AnimationRoute, NavGraphRoute {
    override val pathSegment = "animation".toPathSegment()
}

@Serializable
@SerialName("animation-catalog")
data object AnimationCatalogRoute : AnimationRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("transition")
data object TransitionRoute : AnimationRoute {
    override val pathSegment = "transition".toPathSegment()
}
