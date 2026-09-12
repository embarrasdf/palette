package com.embarrasdf.palette.app.demo.formats.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.formats.core.CoreFormatScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder

fun NavGraphBuilder.coreFormatsNavGraph() = navGraph(
    root = CoreFormatsGraph,
    start = CoreFormatCatalogRoute,
) {
    route(CoreFormatCatalogRoute)
    wildcardRoute<CoreFormatRoute> { pathSegment ->
        CoreFormatRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.coreFormatsEntryProvider(
    navController: NavController,
) {
    catalogEntry<CoreFormatCatalogRoute, CoreFormat>(
        onItemClick = { format ->
            navController.navigate(CoreFormatRoute(format))
        },
        title = "Core",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<CoreFormatRoute> {
        CoreFormatScreen(
            format = it.format,
            onNavigateUp = navController::goBack,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
