package com.embarrasdf.palette.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class NavGraphParseDeeplinkToNavKeyTest {

    @Test
    fun `with single segment`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Route1.pathSegment}")

        assertEquals(Route1, route)
    }

    @Test
    fun `with multiple segments`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            route(Route2)
        }

        val route1 = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Route1.pathSegment}")
        assertEquals(Route1, route1)

        val route2 = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Route2.pathSegment}")
        assertEquals(Route2, route2)
    }

    @Test
    fun `filters empty segments`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        // Leading, trailing, and consecutive slashes
        val route = navGraph.parseDeeplinkToNavKey("/${RootRoute.pathSegment}//${Route1.pathSegment}//")

        assertEquals(Route1, route)
    }

    @Test
    fun `with no matches returns null`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val route = navGraph.parseDeeplinkToNavKey("nonexistent/path")

        assertEquals(null, route)
    }

    @Test
    fun `with graph containers returns only final destination`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route2)
                navGraph(root = Graph2, start = Route2) {
                    route(Route2)
                    route(Route1)
                }
            }
        }

        val deeplink = "${RootRoute.pathSegment}/${Graph1.pathSegment}/${Graph2.pathSegment}/${Route1.pathSegment}"
        val route = navGraph.parseDeeplinkToNavKey(deeplink)

        assertEquals(Route1, route)
    }

    @Test
    fun `when ending at graph root resolves to start route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            navGraph(root = Graph2, start = Route2) {
                route(Route2)
            }
        }

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Graph2.pathSegment}")

        assertEquals(Route2, route)
    }

    @Test
    fun `when ending at nested graph root resolves recursively to leaf start route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph3, start = Graph1) {
                navGraph(root = Graph1, start = Route1) {
                    route(Route1)
                }
            }
        }

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Graph3.pathSegment}")

        assertEquals(Route1, route)
    }

    @Test
    fun `with empty root path segment skips root in parsing`() {
        val emptyRoot = EmptyRoute
        val child = TestRoute("child", parent = emptyRoot)

        val navGraph = navGraph(root = emptyRoot, start = Route1) {
            route(child)
            route(Route1)
        }

        val route = navGraph.parseDeeplinkToNavKey(child.pathSegment.toString())

        assertEquals(child, route)
    }

    @Test
    fun `with empty root path segment excludes root from deeplink generation`() {
        val emptyRoot = EmptyRoute
        val child = TestRoute("child", parent = emptyRoot)

        val navGraph = navGraph(root = emptyRoot, start = Route1) {
            route(child)
            route(Route1)
        }

        val deeplink = child.toDeeplink(navGraph)

        assertEquals(child.pathSegment.toString(), deeplink)
    }

    @Test
    fun `wildcard route with children parses deeplink to child correctly`() {
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

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/dynamic-value/${Route1.pathSegment}")

        assertEquals(Route1, route)
    }

    @Test
    fun `wildcard route with children parses deeplink to wildcard route correctly`() {
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

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/dynamic-value")

        assertEquals(TestRoute("dynamic-value"), route)
    }

    @Test
    fun `wildcard route with nested graph parses deeplink correctly`() {
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

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/dynamic/${Graph1.pathSegment}/${Route2.pathSegment}")

        assertEquals(Route2, route)
    }

    @Test
    fun `wildcard route with nested graph ending at graph resolves to start route`() {
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

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/dynamic/${Graph1.pathSegment}")

        assertEquals(Route1, route)
    }

    @Test
    fun `normalizes uppercase to lowercase`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment.value.uppercase()}/${Route1.pathSegment.value.uppercase()}")

        assertEquals(Route1, route)
    }

    @Test
    fun `normalizes mixed case to lowercase`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val route = navGraph.parseDeeplinkToNavKey("Root/Route1")

        assertEquals(Route1, route)
    }

    @Test
    fun `parameterized graph resolves id segment to start route`() {
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

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/abc123")

        assertEquals(ParamRoute(id = "abc123"), route)
    }

    @Test
    fun `static route registered before wildcardRoute takes priority for exact match`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = { Route1 }) {
                route(Route1) // registered before wildcard
                wildcardRoute<TestRoute> { seg -> TestRoute(seg.value) }
            }
        }

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Graph1.pathSegment}/${Route1.pathSegment}")

        assertEquals(Route1, route)
    }

    @Test
    fun `wildcardRoute matches non-static segments when static route also registered`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = { Route1 }) {
                route(Route1) // registered before wildcard
                wildcardRoute<TestRoute> { seg -> TestRoute(seg.value) }
            }
        }

        val route = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Graph1.pathSegment}/dynamic-value")

        assertEquals(TestRoute("dynamic-value"), route)
    }

    @Test
    fun `static siblings still resolve when parameterized graph is registered`() {
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

        assertEquals(Route1, navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Route1.pathSegment}"))
        assertEquals(Route2, navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/${Route2.pathSegment}"))
    }
}
