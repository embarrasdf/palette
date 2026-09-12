package com.embarrasdf.palette.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NavGraphBuilderTest {

    @Test
    fun `creates graph with single route`() {
        val navGraph = navGraph(root = Graph1, start = Route1) {
            route(Route1)
        }

        val node = navGraph.findNode(Graph1::class)
        assertNotNull(node)
        assertEquals(Graph1.pathSegment, node.pathSegment)
        assertNull(node.parent)
        assertEquals(1, node.children.size)
    }

    @Test
    fun `graph resolves to its start route`() {
        val navGraph = navGraph(root = Graph1, start = Route1) {
            route(Route1)
        }

        assertEquals(Route1, navGraph.resolve(Graph1))
        assertEquals(1, navGraph.findNode(Graph1::class)?.children?.size)
    }

    @Test
    fun `multiple routes at same level`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            route(Route2)
        }

        val rootNode = navGraph.nodes.firstOrNull()
        assertNotNull(rootNode)
        assertEquals(2, rootNode.children.size)

        val route1Node = rootNode.children.firstOrNull { it.pathSegment.value == Route1.pathSegment.value }
        val route2Node = rootNode.children.firstOrNull { it.pathSegment.value == Route2.pathSegment.value }
        assertNotNull(route1Node)
        assertNotNull(route2Node)
    }

    @Test
    fun `nested creates parent-child relationship`() {
        val navGraph = navGraph(root = Graph1, start = Route1) {
            route(Route1)
            navGraph(root = Graph2, start = Route2) {
                route(Route2)
            }
        }

        val parentNode = navGraph.nodes.firstOrNull { it.pathSegment.value == Graph1.pathSegment.value }
        assertNotNull(parentNode)
        assertNull(parentNode.parent)
        assertEquals(2, parentNode.children.size) // route1 + graph2

        val childNode = parentNode.children.firstOrNull { it.pathSegment.value == Graph2.pathSegment.value }
        assertNotNull(childNode)
        assertEquals(Graph1, childNode.parent)
        assertEquals(1, childNode.children.size) // route2
    }

    @Test
    fun `creates wildcard route with wildcard path segment`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            wildcardRoute<TestRoute> { segment ->
                TestRoute(segment.value)
            }
            route(Route1)
        }

        val rootNode = navGraph.nodes.firstOrNull()
        assertNotNull(rootNode)

        val wildcardNode = rootNode.children.firstOrNull()
        assertNotNull(wildcardNode)
        assertEquals(PathSegment.Wildcard, wildcardNode.pathSegment)
    }

    @Test
    fun `wildcard matches any segment`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            wildcardRoute<TestRoute> { segment ->
                TestRoute(segment.value)
            }
            route(Route1)
        }

        val route1 = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/anything")
        assertEquals(TestRoute("anything"), route1)

        val route2 = navGraph.parseDeeplinkToNavKey("${RootRoute.pathSegment}/something-else")
        assertEquals(TestRoute("something-else"), route2)
    }

    @Test
    fun `wildcard route with children does not call parser with Wildcard`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            wildcardRoute<TestRoute>(
                children = {
                    route(Route1)
                }
            ) { segment ->
                require(segment != PathSegment.Wildcard) {
                    "Parser should not be called with PathSegment.Wildcard"
                }
                TestRoute(segment.value)
            }
        }

        val rootNode = navGraph.nodes.firstOrNull()
        assertNotNull(rootNode)
        assertEquals(1, rootNode.children.size)

        val wildcardNode = rootNode.children.firstOrNull()
        assertNotNull(wildcardNode)
        assertEquals(PathSegment.Wildcard, wildcardNode.pathSegment)

        assertEquals(1, wildcardNode.children.size)
        val childNode = wildcardNode.children.firstOrNull()
        assertNotNull(childNode)
        assertEquals(Route1.pathSegment, childNode.pathSegment)

        assertEquals(RootRoute, childNode.parent)
    }

    @Test
    fun `wildcard route with nested graph builds correct structure`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            wildcardRoute<TestRoute>(
                children = {
                    navGraph(root = Graph1, start = Route1) {
                        route(Route1)
                        route(Route2)
                    }
                }
            ) { segment ->
                require(segment != PathSegment.Wildcard) {
                    "Parser should not be called with PathSegment.Wildcard"
                }
                TestRoute(segment.value)
            }
        }

        val rootNode = navGraph.nodes.firstOrNull()
        assertNotNull(rootNode)
        assertEquals(1, rootNode.children.size)

        val wildcardNode = rootNode.children.firstOrNull()
        assertNotNull(wildcardNode)
        assertEquals(PathSegment.Wildcard, wildcardNode.pathSegment)

        assertEquals(1, wildcardNode.children.size)

        val graphNode = wildcardNode.children.firstOrNull { it.pathSegment.value == Graph1.pathSegment.value }
        assertNotNull(graphNode)
        assertEquals(Graph1.pathSegment, graphNode.pathSegment)
        assertEquals(RootRoute, graphNode.parent) // Parent should be RootRoute, not null

        assertEquals(2, graphNode.children.size)

        val route1Node = graphNode.children.firstOrNull { it.pathSegment.value == Route1.pathSegment.value }
        val route2Node = graphNode.children.firstOrNull { it.pathSegment.value == Route2.pathSegment.value }
        assertNotNull(route1Node)
        assertNotNull(route2Node)
        assertEquals(Graph1, route1Node.parent) // Parent should be the graph
        assertEquals(Graph1, route2Node.parent) // Parent should be the graph
    }

    @Test
    fun `dynamic-start graph node has pathSegment from root`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = { Route1 }) {
                route(Route1)
            }
        }

        val node = navGraph.findNode(Graph1::class)
        assertNotNull(node)
        assertEquals(Graph1.pathSegment, node.pathSegment)
    }

    @Test
    fun `dynamic-start graph node is a graph`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = { Route1 }) {
                route(Route1)
            }
        }

        val node = navGraph.findNode(Graph1::class)
        assertNotNull(node)
        assertTrue(node.isGraph)
    }

    @Test
    fun `parameterized graph node has wildcard pathSegment`() {
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

        val node = navGraph.findNode(ParamGraph::class)
        assertNotNull(node)
        assertEquals(PathSegment.Wildcard, node.pathSegment)
    }

    @Test
    fun `parameterized graph node is a graph`() {
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

        val node = navGraph.findNode(ParamGraph::class)
        assertNotNull(node)
        assertTrue(node.isGraph)
    }

    @Test
    fun `nested graph with empty path segment throws`() {
        assertFailsWith<IllegalArgumentException> {
            navGraph(root = RootRoute, start = Route1) {
                navGraph(root = EmptyRoute, start = Route1) {
                    route(Route1)
                }
            }
        }
    }

    @Test
    fun `leaf route with empty path segment throws`() {
        assertFailsWith<IllegalArgumentException> {
            navGraph(root = RootRoute, start = Route1) {
                route(EmptyLeafRoute)
            }
        }
    }

    @Test
    fun `top-level graph root with empty path segment is allowed`() {
        // Empty segment is only valid at the outermost graph root
        val navGraph = navGraph(root = EmptyRoute, start = Route1) {
            route(Route1)
        }
        assertNotNull(navGraph.findNode(Route1::class))
    }
}
