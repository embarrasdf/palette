package com.embarrasdf.palette.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class NavGraphToNavKeyTest {

    @Test
    fun `with single segment returns single route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val segments = listOf(RootRoute.pathSegment, Route1.pathSegment)
        val route = segments.toNavKey(navGraph)

        assertEquals(Route1, route)
    }

    @Test
    fun `skips graph containers and only includes final destination`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route2)
                navGraph(root = Graph2, start = Route2) {
                    route(Route2)
                    route(Route1)
                }
            }
        }

        val segments = listOf(
            RootRoute.pathSegment,
            Graph1.pathSegment,
            Graph2.pathSegment,
            Route1.pathSegment
        )
        val route = segments.toNavKey(navGraph)

        assertEquals(Route1, route)
    }

    @Test
    fun `includes leaf nodes even if not final segment`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            route(Route2)
        }

        val segments1 = listOf(RootRoute.pathSegment, Route1.pathSegment)
        val route1 = segments1.toNavKey(navGraph)
        assertEquals(Route1, route1)

        val segments2 = listOf(RootRoute.pathSegment, Route2.pathSegment)
        val route2 = segments2.toNavKey(navGraph)
        assertEquals(Route2, route2)
    }

    @Test
    fun `returns null for unmatched segments`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val segments = listOf(PathSegment("nonexistent"))
        val route = segments.toNavKey(navGraph)

        assertEquals(null, route)
    }

    @Test
    fun `with wildcard route matches any segment`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            wildcardRoute<TestRoute> { segment ->
                TestRoute(segment.value)
            }
            route(Route1)
        }

        val segments = listOf(RootRoute.pathSegment, PathSegment("anything"))
        val route = segments.toNavKey(navGraph)

        assertEquals(TestRoute("anything"), route)
    }
}
