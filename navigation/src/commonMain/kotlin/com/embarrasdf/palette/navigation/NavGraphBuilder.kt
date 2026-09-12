package com.embarrasdf.palette.navigation

import kotlinx.serialization.serializer

inline fun <reified T : NavGraphRoute> navGraph(
    root: T,
    start: NavKey,
    noinline block: NavGraphBuilder.() -> Unit = {},
): NavGraph {
    return NavGraphBuilder().apply {
        navGraph(
            root = root,
            start = start,
            children = block,
        )
    }.build(root)
}

class NavGraphBuilder(
    val parent: NavKey? = null,
) {
    val nodes = mutableListOf<NavGraphNode>()

    inline fun <reified T : NavGraphRoute> navGraph(
        root: T,
        start: NavKey,
        children: NavGraphBuilder.() -> Unit = {}
    ) {
        require(parent == null || root.pathSegment.value.isNotEmpty()) {
            "${T::class.simpleName} has an empty path segment. Only the top-level graph root may have an empty path segment."
        }

        val childrenBuilder = NavGraphBuilder(parent = root)
        childrenBuilder.children()

        val startRouteClass = start::class
        require(childrenBuilder.nodes.findNode(startRouteClass) != null) {
            "${T::class.simpleName} start (${startRouteClass.simpleName}) is not registered in this graph"
        }

        nodes.add(
            NavGraphNode(
                pathSegment = root.pathSegment,
                navKeyClass = T::class,
                serializer = serializer<T>(),
                parser = { root },
                parent = parent,
                children = childrenBuilder.nodes,
                startRouteFactory = { start },
            )
        )
    }

    inline fun <reified T : NavGraphRoute> navGraph(
        root: T,
        noinline start: (T) -> NavKey,
        children: NavGraphBuilder.() -> Unit = {},
    ) {
        require(parent == null || root.pathSegment.value.isNotEmpty()) {
            "${T::class.simpleName} has an empty path segment. Only the top-level graph root may have an empty path segment."
        }

        val childrenBuilder = NavGraphBuilder(parent = root)
        childrenBuilder.children()

        nodes.add(
            NavGraphNode(
                pathSegment = root.pathSegment,
                navKeyClass = T::class,
                serializer = serializer<T>(),
                parser = { root },
                parent = parent,
                children = childrenBuilder.nodes,
                startRouteFactory = { route -> start(route as T) },
            )
        )
    }

    inline fun <reified T : NavGraphRoute> navGraph(
        noinline root: (PathSegment) -> T?,
        noinline start: (T) -> NavKey,
        children: NavGraphBuilder.() -> Unit = {},
    ) {
        val childrenBuilder = NavGraphBuilder(parent = parent)
        childrenBuilder.children()

        nodes.add(
            NavGraphNode(
                pathSegment = PathSegment.Wildcard,
                navKeyClass = T::class,
                serializer = serializer<T>(),
                parser = { seg -> root(seg) },
                parent = parent,
                children = childrenBuilder.nodes,
                startRouteFactory = { route -> start(route as T) },
            )
        )
    }

    inline fun <reified T : NavKey> route(
        pathSegment: PathSegment,
        noinline parser: (PathSegment) -> NavKey?,
        children: NavGraphBuilder.() -> Unit = {}
    ) {
        require(pathSegment.value.isNotEmpty()) {
            "${T::class.simpleName} has an empty path segment and cannot be registered as a route."
        }

        val childrenBuilder = NavGraphBuilder(parent = parser(pathSegment))
        childrenBuilder.children()

        nodes.add(
            NavGraphNode(
                pathSegment = pathSegment,
                navKeyClass = T::class,
                serializer = serializer<T>(),
                parser = parser,
                parent = parent,
                children = childrenBuilder.nodes,
            )
        )
    }

    inline fun <reified T : NavKey> wildcardRoute(
        children: NavGraphBuilder.() -> Unit = {},
        noinline parser: (PathSegment) -> NavKey?
    ) {
        val pathSegment = PathSegment.Wildcard

        // For children of wildcard routes, use the current parent (the container)
        // since wildcard routes don't have a single concrete instance to use as parent
        val childrenBuilder = NavGraphBuilder(parent = parent)
        childrenBuilder.children()

        nodes.add(
            NavGraphNode(
                pathSegment = pathSegment,
                navKeyClass = T::class,
                serializer = serializer<T>(),
                parser = parser,
                parent = parent,
                children = childrenBuilder.nodes,
            )
        )
    }

    inline fun <reified T : NavKey> route(
        navKey: T,
        children: NavGraphBuilder.() -> Unit = {}
    ) {
        route<T>(
            pathSegment = navKey.pathSegment,
            parser = { navKey },
            children = children,
        )
    }

    fun build(root: NavKey): NavGraph = NavGraph(root = root, nodes = nodes)
}
