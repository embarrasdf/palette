package com.embarrasdf.palette.navigation

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

internal fun List<PathSegment>.toNavKey(navGraph: NavGraph): NavKey? = navGraph.matchRoute(
    nodes = navGraph.nodes,
    segments = this,
    segmentIndex = 0,
)

private fun NavGraph.matchRoute(
    nodes: List<NavGraphNode>,
    segments: List<PathSegment>,
    segmentIndex: Int,
): NavKey? {
    if (segmentIndex >= segments.size) return null

    for (node in nodes) {
        val match = matchNode(
            node = node,
            segments = segments,
            segmentIndex = segmentIndex,
        )
        if (match != null) return match
    }

    return null
}

private fun NavGraph.matchNode(
    node: NavGraphNode,
    segments: List<PathSegment>,
    segmentIndex: Int,
): NavKey? {
    val segment = segments[segmentIndex]

    // Skip nodes with empty path segments - search their children directly
    if (node.pathSegment.value.isEmpty() && node.children.isNotEmpty()) {
        return matchRoute(
            nodes = node.children,
            segments = segments,
            segmentIndex = segmentIndex,
        )
    }

    val isMatch = node.pathSegment == PathSegment.Wildcard || node.pathSegment == segment
    if (!isMatch) return null

    val parsed = node.parser(segment)

    if (parsed != null) {
        val isLastSegment = segmentIndex == segments.size - 1
        if (isLastSegment || node.children.isEmpty()) {
            return resolve(parsed)
        }
    }

    if (node.children.isNotEmpty()) {
        val childMatch = matchRoute(
            nodes = node.children,
            segments = segments,
            segmentIndex = segmentIndex + 1,
        )
        if (childMatch != null) return childMatch
    }

    return null
}
