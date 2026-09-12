package com.embarrasdf.palette.app.theme.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeItem
import com.embarrasdf.palette.app.theme.component.navigation.ComponentGraph
import com.embarrasdf.palette.app.theme.component.navigation.componentEntryProvider
import com.embarrasdf.palette.app.theme.component.navigation.componentNavGraph
import com.embarrasdf.palette.app.theme.primitive.navigation.PrimitiveGraph
import com.embarrasdf.palette.app.theme.primitive.navigation.primitiveEntryProvider
import com.embarrasdf.palette.app.theme.primitive.navigation.primitiveNavGraph
import com.embarrasdf.palette.app.theme.semantic.navigation.SemanticGraph
import com.embarrasdf.palette.app.theme.semantic.navigation.semanticEntryProvider
import com.embarrasdf.palette.app.theme.semantic.navigation.semanticNavGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.themeNavGraph() = navGraph(
    root = ThemeGraph,
    start = ThemeCatalogRoute,
) {
    route(ThemeCatalogRoute)
    primitiveNavGraph()
    semanticNavGraph()
    componentNavGraph()
}

fun EntryProviderScope<NavKey>.themeEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<ThemeCatalogRoute, ThemeItem>(
        onItemClick = { item ->
            when (item) {
                ThemeItem.Primitive -> navController.navigate(PrimitiveGraph)
                ThemeItem.Semantic -> navController.navigate(SemanticGraph)
                ThemeItem.Component -> navController.navigate(ComponentGraph)
            }
        },
        title = "Theme",
        onNavigateUp = navController::goBack,
    )

    primitiveEntryProvider(navController, themeController)
    semanticEntryProvider(navController, themeController)
    componentEntryProvider(navController, themeController)
}
