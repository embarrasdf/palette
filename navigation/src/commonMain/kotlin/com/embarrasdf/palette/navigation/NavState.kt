package com.embarrasdf.palette.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey as Navigation3NavKey
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json

@Composable
fun rememberNavState(
    navGraph: NavGraph,
    jsonConfig: NavKeyJsonBuilder.() -> Unit = {},
    initialDeeplink: String? = null,
    buildSyntheticBackStack: Boolean = true,
    onWouldBecomeEmpty: () -> Unit = {},
): NavState {
    val json = rememberNavKeyJson(navGraph, jsonConfig)

    val initialBackStack = remember {
        if (initialDeeplink == null) return@remember listOf(navGraph.startRoute)

        if (buildSyntheticBackStack) {
            navGraph.parseDeeplinkToBackStack(initialDeeplink)
        } else {
            listOf(navGraph.parseDeeplinkToNavKey(initialDeeplink) ?: navGraph.startRoute)
        }
    }

    val backStack = rememberNavBackStack(
        json = json,
        initialBackStack = initialBackStack,
    )

    val currentOnWouldBecomeEmpty by rememberUpdatedState(onWouldBecomeEmpty)

    return remember(backStack, navGraph) {
        NavState(
            backStack = backStack,
            navGraph = navGraph,
            onWouldBecomeEmpty = currentOnWouldBecomeEmpty,
        )
    }
}

@Stable
class NavState(
    val backStack: SnapshotStateList<NavKey>,
    val navGraph: NavGraph,
    val onWouldBecomeEmpty: () -> Unit = {},
) {
    val currentRoute: NavKey?
        get() = backStack.lastOrNull()

    val previousRoute: NavKey?
        get() = backStack.getOrNull(backStack.size - 2)

    fun navigate(
        route: NavKey,
        replace: Boolean = false,
    ) {
        val resolvedRoute = navGraph.resolve(route)
        if (replace && backStack.isNotEmpty()) {
            backStack[backStack.lastIndex] = resolvedRoute
        } else {
            backStack.add(resolvedRoute)
        }
    }

    fun goBack() {
        if (backStack.size <= 1) {
            onWouldBecomeEmpty()
            return
        }
        backStack.removeLastOrNull()
    }

    fun popUpTo(route: NavKey, inclusive: Boolean = false): Boolean {
        val index = backStack.indexOfLast { it::class == route::class }
        if (index == -1) return false

        val targetIndex = if (inclusive) index else index + 1
        if (targetIndex >= backStack.size) return false

        val itemsToRemove = backStack.size - targetIndex
        repeat(itemsToRemove) {
            backStack.removeLastOrNull()
        }

        if (backStack.isEmpty()) {
            onWouldBecomeEmpty()
        }

        return true
    }

    fun navigateUp(): Boolean {
        val currentRoute = this.currentRoute ?: return false

        // Find the first ancestor that resolves to a different route
        var currentNode = navGraph.findNode(currentRoute::class)
        while (currentNode != null) {
            val parent = currentNode.parent ?: break

            val resolvedParent = navGraph.resolve(parent)

            // If the parent resolves to a different route, navigate there
            if (resolvedParent::class != currentRoute::class) {
                // If the resolved parent is already the previous route in the backstack,
                // just pop the current route to reveal it
                if (resolvedParent::class == previousRoute?.let { it::class }) {
                    goBack()
                    return true
                }

                // Otherwise, navigate to the parent with replace
                navigate(parent, replace = true)
                return true
            }

            // Otherwise, keep going up the hierarchy
            currentNode = navGraph.findNode(parent::class)
        }

        // Route not found in graph or parent chain exhausted, fall back to goBack()
        goBack()
        return backStack.isNotEmpty()
    }
}

// navigation3's rememberNavBackStack doesn't expose the SnapshotStateList which is required for
// ChronologicalBrowserNavigation
@Composable
private fun rememberNavBackStack(
    json: Json,
    initialBackStack: List<NavKey>,
): SnapshotStateList<NavKey> = rememberSaveable(
    saver = listSaver(
        save = { list ->
            list.toList().map { key ->
                json.encodeToString(PolymorphicSerializer(Navigation3NavKey::class), key)
            }
        },
        restore = { saved ->
            @Suppress("UNCHECKED_CAST")
            mutableStateListOf(*saved.map { s ->
                json.decodeFromString(PolymorphicSerializer(Navigation3NavKey::class), s) as NavKey
            }.toTypedArray())
        },
    ),
) { mutableStateListOf(*initialBackStack.toTypedArray()) }
