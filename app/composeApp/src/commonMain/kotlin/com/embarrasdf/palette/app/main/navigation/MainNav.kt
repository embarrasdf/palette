package com.embarrasdf.palette.app.main.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.components.navigation.ComponentsGraph
import com.embarrasdf.palette.app.demo.formats.navigation.FormatsGraph
import com.embarrasdf.palette.app.demo.modifiers.navigation.ModifiersGraph
import com.embarrasdf.palette.app.main.MainCatalogItem
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.mainNavGraph() = navGraph(
    root = MainGraph,
    start = MainCatalogRoute,
) {
    route(MainCatalogRoute)
}

fun EntryProviderScope<NavKey>.mainEntryProvider(
    navController: NavController,
) {
    catalogEntry<MainCatalogRoute, MainCatalogItem>(
        onItemClick = { item ->
            when (item) {
                MainCatalogItem.Components -> navController.navigate(ComponentsGraph)
                MainCatalogItem.Formats -> navController.navigate(FormatsGraph)
                MainCatalogItem.Modifiers -> navController.navigate(ModifiersGraph)
            }
        },
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )
}
