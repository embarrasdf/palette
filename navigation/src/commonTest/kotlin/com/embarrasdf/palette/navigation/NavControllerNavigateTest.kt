package com.embarrasdf.palette.navigation

import androidx.compose.runtime.mutableStateListOf
import kotlin.test.Test
import kotlin.test.assertEquals

class NavControllerNavigateTest {

    @Test
    fun `pushes route to back stack`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            route(Route2)
        }

        val backStack = mutableStateListOf<NavKey>(Route1)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigate(Route2)

        assertEquals(2, backStack.size)
        assertEquals(Route1, backStack[0])
        assertEquals(Route2, backStack[1])
    }

    @Test
    fun `with replace replaces current route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            route(Route1)
            route(Route2)
        }

        val backStack = mutableStateListOf<NavKey>(Route1)
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigate(Route2, replace = true)

        assertEquals(1, backStack.size)
        assertEquals(Route2, backStack[0])
    }

    @Test
    fun `resolves graph route to start route`() {
        val navGraph = navGraph(root = RootRoute, start = Route1) {
            navGraph(root = Graph1, start = Route1) {
                route(Route1)
            }
        }

        val backStack = mutableStateListOf<NavKey>()
        val navState = NavState(backStack, navGraph)
        val navController = NavController(navState)

        navController.navigate(Graph1)

        assertEquals(1, backStack.size)
        assertEquals(Route1, backStack[0])
    }
}
