package com.embarrasdf.palette.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("test-route")
data class TestRoute(
    override val pathSegment: PathSegment,
    @Transient val parent: NavKey? = null,
) : NavKey

fun TestRoute(value: String, parent: NavKey? = null) = TestRoute(PathSegment(value), parent)

@Serializable
@SerialName("root-route")
data object RootRoute : NavGraphRoute {
    override val pathSegment = PathSegment("root")
}

@Serializable
@SerialName("graph1")
data object Graph1 : NavGraphRoute {
    override val pathSegment = PathSegment("graph1")
}

@Serializable
@SerialName("graph2")
data object Graph2 : NavGraphRoute {
    override val pathSegment = PathSegment("graph2")
}

@Serializable
@SerialName("graph3")
data object Graph3 : NavGraphRoute {
    override val pathSegment = PathSegment("graph3")
}

@Serializable
@SerialName("circular-graph")
data object CircularGraph : NavGraphRoute {
    override val pathSegment = PathSegment("circular")
}

@Serializable
@SerialName("test-route-wildcard")
data object TestRouteWildcard : NavKey {
    override val pathSegment = PathSegment.Wildcard
}

@Serializable
@SerialName("route1")
data object Route1 : NavKey {
    override val pathSegment = PathSegment("route1")
}

@Serializable
@SerialName("route2")
data object Route2 : NavKey {
    override val pathSegment = PathSegment("route2")
}

@Serializable
@SerialName("route3")
data object Route3 : NavKey {
    override val pathSegment = PathSegment("route3")
}

@Serializable
@SerialName("empty-route")
data object EmptyRoute : NavGraphRoute {
    override val pathSegment = PathSegment("")
}

@Serializable
@SerialName("empty-leaf-route")
data object EmptyLeafRoute : NavKey {
    override val pathSegment = PathSegment("")
}

@Serializable
@SerialName("param-graph")
data class ParamGraph(val id: String) : NavGraphRoute {
    override val pathSegment = PathSegment(id)
}

@Serializable
@SerialName("param-route")
data class ParamRoute(val id: String) : NavKey {
    override val pathSegment = PathSegment(id)
}
