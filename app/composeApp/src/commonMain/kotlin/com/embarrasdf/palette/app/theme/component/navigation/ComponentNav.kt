package com.embarrasdf.palette.app.theme.component.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.component.ComponentItem
import com.embarrasdf.palette.app.theme.component.core.navigation.CoreGraph
import com.embarrasdf.palette.app.theme.component.core.navigation.coreEntryProvider
import com.embarrasdf.palette.app.theme.component.core.navigation.coreNavGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.componentNavGraph() = navGraph(
    root = ComponentGraph,
    start = ComponentCatalogRoute,
) {
    route(ComponentCatalogRoute)
    coreNavGraph()
}

fun EntryProviderScope<NavKey>.componentEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<ComponentCatalogRoute, ComponentItem>(
        onItemClick = { item ->
            when (item) {
                ComponentItem.Core -> navController.navigate(CoreGraph)
            }
        },
        title = "Component",
        onNavigateUp = navController::goBack,
    )
    coreEntryProvider(navController, themeController)
}
