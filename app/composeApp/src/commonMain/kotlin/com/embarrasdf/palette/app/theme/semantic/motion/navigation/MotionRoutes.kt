package com.embarrasdf.palette.app.theme.semantic.motion.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface MotionRoute : NavKey

@Serializable
@SerialName("motion")
data object MotionGraph : MotionRoute, NavGraphRoute {
    override val pathSegment = "motion".toPathSegment()
}

@Serializable
@SerialName("motion-catalog")
data object MotionCatalogRoute : MotionRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("motion-infinite")
data object InfiniteRoute : MotionRoute {
    override val pathSegment = "infinite".toPathSegment()
}

@Serializable
@SerialName("motion-transition")
data object TransitionRoute : MotionRoute {
    override val pathSegment = "transition".toPathSegment()
}
