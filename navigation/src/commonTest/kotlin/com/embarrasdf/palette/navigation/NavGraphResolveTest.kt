package com.embarrasdf.palette.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class NavGraphResolveTest {

    @Test
    fun `graph route resolves to start route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
            }
        }

        val resolved = navGraph.resolve(Graph1)

        assertEquals(Route1, resolved)
    }

    @Test
    fun `non-graph route returns route as-is`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val resolved = navGraph.resolve(Route1)

        assertEquals(Route1, resolved)
    }

    @Test
    fun `nested graph resolves recursively to leaf start route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph3, start = Graph1) {
                navGraph(root = Graph1, start = Route1) {
                    route(Route1)
                }
            }
        }

        val resolved = navGraph.resolve(Graph3)

        assertEquals(Route1, resolved)
    }

    @Test
    fun `route not in graph returns route as-is`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val resolved = navGraph.resolve(Route2)

        assertEquals(Route2, resolved)
    }

    @Test
    fun `graph with circular start route returns route to prevent infinite loop`() {
        val navGraph = navGraph(root = CircularGraph, start = CircularGraph) {
            route(CircularGraph)
        }

        val resolved = navGraph.resolve(CircularGraph)

        assertEquals(CircularGraph, resolved)
    }

    @Test
    fun `dynamic-start graph resolves via start factory`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            navGraph(root = Graph1, start = { Route2 }) {
                route(Route2)
            }
        }

        val resolved = navGraph.resolve(Graph1)

        assertEquals(Route2, resolved)
    }

    @Test
    fun `parameterized graph resolves to start route`() {
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

        val resolved = navGraph.resolve(ParamGraph(id = "abc123"))

        assertEquals(ParamRoute(id = "abc123"), resolved)
    }
}
