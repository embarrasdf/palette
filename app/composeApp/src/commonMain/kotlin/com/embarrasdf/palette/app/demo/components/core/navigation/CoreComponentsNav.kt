package com.embarrasdf.palette.app.demo.components.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.components.core.CoreComponentScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder

fun NavGraphBuilder.coreComponentsNavGraph() = navGraph(
    root = CoreComponentsGraph,
    start = CoreComponentCatalogRoute,
) {
    route(CoreComponentCatalogRoute)
    wildcardRoute<CoreComponentRoute> { pathSegment ->
        CoreComponentRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.coreComponentsEntryProvider(
    navController: NavController,
) {
    catalogEntry<CoreComponentCatalogRoute, CoreComponent>(
        onItemClick = { component ->
            navController.navigate(CoreComponentRoute(component))
        },
        title = "Core",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<CoreComponentRoute> {
        CoreComponentScreen(
            component = it.component,
            onNavigateUp = navController::goBack,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
