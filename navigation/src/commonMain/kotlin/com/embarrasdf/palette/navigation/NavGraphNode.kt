package com.embarrasdf.palette.navigation

import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer

data class NavGraphNode(
    val pathSegment: PathSegment,
    val navKeyClass: KClass<out NavKey>,
    val serializer: KSerializer<out NavKey>,
    val parser: (PathSegment) -> NavKey?,
    val parent: NavKey?,
    val children: List<NavGraphNode>,
    val startRouteFactory: ((NavKey) -> NavKey)? = null,
) {
    val isGraph: Boolean get() = startRouteFactory != null
}
