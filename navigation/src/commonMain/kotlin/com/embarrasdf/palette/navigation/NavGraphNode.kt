package com.embarrasdf.palette.navigation

import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

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
