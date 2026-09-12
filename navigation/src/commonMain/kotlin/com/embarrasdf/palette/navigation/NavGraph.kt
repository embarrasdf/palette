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

fun NavKey.toDeeplink(tree: NavGraph): String {
    val node = tree.findNode(this::class) ?: return pathSegment.value

    val parentPath = node.parent?.toDeeplink(tree)

    return if (!parentPath.isNullOrEmpty()) {
        "$parentPath/${pathSegment.value}"
    } else {
        pathSegment.value
    }
}

fun NavKey.Companion.fromDeeplink(
    deeplink: String,
    navGraph: NavGraph,
): NavKey? {
    return navGraph.parseDeeplinkToNavKey(deeplink)
}

fun NavGraph.parseDeeplinkToNavKey(deeplink: String): NavKey? {
    val segments = deeplink.lowercase().split("/").filter { it.isNotEmpty() }.map(::PathSegment)
    return segments.toNavKey(this)
}

fun NavGraph.parseDeeplinkToBackStack(deeplink: String): List<NavKey> {
    val dest = parseDeeplinkToNavKey(deeplink) ?: return listOf(startRoute)
    return buildBackStack(dest)
}

private fun NavGraph.buildBackStack(dest: NavKey): List<NavKey> {
    var nodeToCheck: NavGraphNode? = findNode(dest::class)
    while (nodeToCheck != null) {
        val parent = nodeToCheck.parent ?: return listOf(dest)
        val resolvedParent = resolve(parent)
        if (resolvedParent::class != dest::class) {
            return buildBackStack(resolvedParent) + dest
        }
        // This parent resolves to dest itself (dest is the start of this graph) — climb higher
        nodeToCheck = findNode(parent::class)
    }
    return listOf(dest)
}

internal fun List<PathSegment>.toNavKey(
    navGraph: NavGraph,
): NavKey? {
    fun matchRoute(
        nodes: List<NavGraphNode>,
        segmentIndex: Int,
    ): NavKey? {
        if (segmentIndex >= this.size) return null

        val segment = this[segmentIndex]

        for (node in nodes) {
            // Skip nodes with empty path segments - search their children directly
            if (node.pathSegment.value.isEmpty() && node.children.isNotEmpty()) {
                val childMatch = matchRoute(node.children, segmentIndex)
                if (childMatch != null) return childMatch
                continue
            }

            val isMatch = node.pathSegment == PathSegment.Wildcard || node.pathSegment == segment
            if (!isMatch) continue

            val parsed = node.parser(segment)

            if (parsed != null) {
                val isLastSegment = segmentIndex == this@toNavKey.size - 1
                if (isLastSegment || node.children.isEmpty()) {
                    return navGraph.resolve(parsed)
                }
            }

            if (node.children.isNotEmpty()) {
                val childMatch = matchRoute(node.children, segmentIndex + 1)
                if (childMatch != null) return childMatch
            }
        }

        return null
    }

    return matchRoute(
        nodes = navGraph.nodes,
        segmentIndex = 0,
    )
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
