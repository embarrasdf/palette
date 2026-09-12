package com.embarrasdf.palette.navigation

import androidx.compose.runtime.mutableStateListOf
import kotlin.test.Test
import kotlin.test.assertEquals

class NavControllerNavigateToDeeplinkTest {

    @Test
    fun `navigates to route from deeplink`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
                route(Route2)
            }
        }

        val backStack = mutableStateListOf<NavKey>(Route1)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateToDeeplink("${RootRoute.pathSegment}/${Graph1.pathSegment}/${Route2.pathSegment}")

        assertEquals(2, backStack.size)
        assertEquals(Route1, backStack[0])
        assertEquals(Route2, backStack[1])
    }

    @Test
    fun `with replace flag replaces current route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
                route(Route2)
            }
        }

        val backStack = mutableStateListOf<NavKey>(Route1)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateToDeeplink("${RootRoute.pathSegment}/${Graph1.pathSegment}/${Route2.pathSegment}", replace = true)

        assertEquals(1, backStack.size)
        assertEquals(Route2, backStack[0])
    }

    @Test
    fun `with invalid deeplink does nothing`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
        }

        val backStack = mutableStateListOf<NavKey>(Route1)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateToDeeplink("invalid/path")

        assertEquals(1, backStack.size)
        assertEquals(Route1, backStack[0])
    }

    @Test
    fun `resolves graph roots in deeplink`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            navGraph(root = Graph2, start = Route2) {
                route(Route2)
            }
        }

        val backStack = mutableStateListOf<NavKey>(RootRoute)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigateToDeeplink("${RootRoute.pathSegment}/${Graph2.pathSegment}")

        assertEquals(2, backStack.size)
        assertEquals(RootRoute, backStack[0])
        assertEquals(Route2, backStack[1])
    }
}
