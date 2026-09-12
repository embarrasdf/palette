package com.embarrasdf.palette.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class NavGraphToDeeplinkTest {

    @Test
    fun `with single route generates correct deeplink`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val deeplink = Route1.toDeeplink(navGraph)

        assertEquals("${RootRoute.pathSegment}/${Route1.pathSegment}", deeplink)
    }

    @Test
    fun `with multiple routes at same level`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            route(Route2)
        }

        val deeplink1 = Route1.toDeeplink(navGraph)
        assertEquals("${RootRoute.pathSegment}/${Route1.pathSegment}", deeplink1)

        val deeplink2 = Route2.toDeeplink(navGraph)
        assertEquals("${RootRoute.pathSegment}/${Route2.pathSegment}", deeplink2)
    }

    @Test
    fun `with nested graph includes graph in path`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
                route(Route2)
            }
        }

        val deeplink = Route2.toDeeplink(navGraph)

        assertEquals("${RootRoute.pathSegment}/${Graph1.pathSegment}/${Route2.pathSegment}", deeplink)
    }

    @Test
    fun `with deeply nested graphs includes all parents in path`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph3, start = Graph1) {
                navGraph(root = Graph1, start = Route1) {
                    route(Route1)
                }
            }
        }

        val deeplink = Route1.toDeeplink(navGraph)

        assertEquals("${RootRoute.pathSegment}/${Graph3.pathSegment}/${Graph1.pathSegment}/${Route1.pathSegment}", deeplink)
    }

    @Test
    fun `leaf route with empty path segment throws`() {
        assertFailsWith<IllegalArgumentException> {
            navGraph(root = RootRoute, start = Route1) {
                navGraph(root = Graph1, start = Route1) {
                    route(Route1)
                    route(EmptyLeafRoute)
                }
            }
        }
    }

    @Test
    fun `with empty root path segment excludes root from deeplink`() {
        val emptyRoot = EmptyRoute
        val child = TestRoute("child", parent = emptyRoot)

        val navGraph = navGraph(root = emptyRoot, start = Route1) {
            route(child)
            route(Route1)
        }

        val deeplink = child.toDeeplink(navGraph)

        assertEquals("child", deeplink)
    }

    @Test
    fun `wildcard route with children generates deeplink correctly`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            wildcardRoute<TestRoute>(
                children = {
                    route(Route1)
                    route(Route2)
                }
            ) { segment ->
                TestRoute(segment.value)
            }
        }

        val deeplink = Route1.toDeeplink(navGraph)

        assertEquals("${RootRoute.pathSegment}/${Route1.pathSegment}", deeplink)
    }

    @Test
    fun `wildcard route with nested graph generates deeplink for child route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            wildcardRoute<TestRoute>(
                children = {
                    navGraph(root = Graph1, start = Route1) {
                        route(Route1)
                        route(Route2)
                    }
                }
            ) { segment ->
                TestRoute(segment.value)
            }
        }

        val deeplink = Route2.toDeeplink(navGraph)

        assertEquals("${RootRoute.pathSegment}/${Graph1.pathSegment}/${Route2.pathSegment}", deeplink)
    }

    @Test
    fun `parameterized graph instance produces id path`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            route(Route2)
            navGraph<ParamGraph>(
                root = { seg -> ParamGraph(id = seg.value) },
                start = { graph -> ParamRoute(id = graph.id) },
            ) {
                wildcardRoute<ParamRoute> { seg -> ParamRoute(id = seg.value) }
            }
        }

        val deeplink = ParamGraph(id = "abc123").toDeeplink(navGraph)

        assertEquals("${RootRoute.pathSegment}/abc123", deeplink)
    }
}
