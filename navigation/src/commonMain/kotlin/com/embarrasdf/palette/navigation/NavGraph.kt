package com.embarrasdf.palette.navigation

import androidx.compose.runtime.Stable
import kotlin.reflect.KClass

@Stable
data class NavGraph(
    val root: NavKey,
    val nodes: List<NavGraphNode>,
) {
    val startRoute: NavKey
        get() = resolve(root)
}

/**
 * Resolves a route to its start route if it's a graph, recursively.
 * If the route is not a graph or is already a leaf route, returns the route itself.
 */
fun NavGraph.resolve(route: NavKey): NavKey {
    fun resolve(current: NavKey): NavKey {
        if (current !is NavGraphRoute) return current
        val node = findNode(current::class) ?: return current
        val start = node.startRouteFactory?.invoke(current) ?: return current
        // Stop if the start route is the same as current route (prevents infinite loop)
        if (start::class == current::class) return current
        return resolve(start)
    }
    return resolve(route)
}

internal fun NavGraph.findNode(navKeyClass: KClass<out NavKey>): NavGraphNode? {
    return this.nodes.findNode(navKeyClass)
}

@PublishedApi
internal fun List<NavGraphNode>.findNode(navKeyClass: KClass<out NavKey>): NavGraphNode? {
    for (node in this) {
        if (node.navKeyClass == navKeyClass) return node
        val found = node.children.findNode(navKeyClass)
        if (found != null) return found
    }
    return null
}
