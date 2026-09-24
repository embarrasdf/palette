package com.embarrasdf.palette.app.theme.semantic.animation.navigation.finite

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.animation.AnimationDefaultScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.finiteNavGraph() = navGraph(
    root = FiniteGraph,
    start = FiniteCatalogRoute,
) {
    route(FiniteCatalogRoute)
    route(DefaultRoute)
}

fun EntryProviderScope<NavKey>.finiteEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<FiniteCatalogRoute, FiniteItem>(
        onItemClick = { item ->
            when (item) {
                FiniteItem.Default -> navController.navigate(DefaultRoute)
            }
        },
        title = "Finite",
        onNavigateUp = navController::goBack,
    )

    entry<DefaultRoute> {
        AnimationDefaultScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }
}
