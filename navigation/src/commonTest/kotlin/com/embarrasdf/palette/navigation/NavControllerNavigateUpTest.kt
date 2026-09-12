package com.embarrasdf.palette.navigation

import androidx.compose.runtime.mutableStateListOf
import kotlin.test.Test
import kotlin.test.assertEquals

class NavControllerNavigateUpTest {

    @Test
    fun `with valid parent navigates to parent with replace`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
                route(Route2)
            }
        }

        val backStack = mutableStateListOf<NavKey>(Route2)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateUp()

        assertEquals(1, backStack.size)
        assertEquals(Route1, backStack.last())
    }

    @Test
    fun `without parent calls onWouldBecomeEmpty`() {
        data class UnregisteredRoute(override val pathSegment: PathSegment) : NavKey

        val route = UnregisteredRoute(PathSegment("unregistered"))

        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val backStack = mutableStateListOf<NavKey>(route)
        var onWouldBecomeEmptyCalled = false
        val navState = NavState(
            backStack = backStack,
            navGraph = navGraph,
            onWouldBecomeEmpty = { onWouldBecomeEmptyCalled = true }
        )
        val navController = NavController(navState)

        navController.navigateUp()

        assertEquals(1, backStack.size)
        assertEquals(true, onWouldBecomeEmptyCalled)
    }

    @Test
    fun `skips intermediate graph containers that resolve to same route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph2, start = Route2) {
                navGraph(root = Graph1, start = Route1) {
                    route(Route1)
                    route(Route2)
                }
            }
        }

        val backStack = mutableStateListOf<NavKey>(Route2)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateUp()

        assertEquals(1, backStack.size)
        assertEquals(Route1, backStack.last())
    }

    @Test
    fun `from middle of stack replaces current route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
                navGraph(root = Graph2, start = Route2) {
                    route(Route2)
                    route(Route3)
                }
            }
        }

        val backStack = mutableStateListOf<NavKey>(Route1, Route3)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateUp()

        assertEquals(2, backStack.size)
        assertEquals(Route1, backStack[0])
        assertEquals(Route2, backStack[1])
    }

    @Test
    fun `when parent is in backstack pops current route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
                route(Route2)
            }
        }

        val backStack = mutableStateListOf<NavKey>(Route1, Route2)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateUp()

        assertEquals(1, backStack.size)
        assertEquals(Route1, backStack[0])
    }

    @Test
    fun `when parent not in backstack replaces current route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
                route(Route2)
            }
        }

        val backStack = mutableStateListOf<NavKey>(Route2)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateUp()

        assertEquals(1, backStack.size)
        assertEquals(Route1, backStack[0])
    }
}
