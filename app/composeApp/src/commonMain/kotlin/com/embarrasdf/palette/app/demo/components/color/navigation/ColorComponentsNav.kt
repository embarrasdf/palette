package com.embarrasdf.palette.app.demo.components.color.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.components.color.ColorComponentScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder

fun NavGraphBuilder.colorComponentsNavGraph() = navGraph(
    root = ColorComponentsGraph,
    start = ColorComponentCatalogRoute,
) {
    route(ColorComponentCatalogRoute)
    wildcardRoute<ColorComponentRoute> { pathSegment ->
        ColorComponentRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.colorComponentsEntryProvider(
    navController: NavController,
) {
    catalogEntry<ColorComponentCatalogRoute, ColorComponent>(
        onItemClick = { component ->
            navController.navigate(ColorComponentRoute(component))
        },
        title = "Color",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<ColorComponentRoute> {
        ColorComponentScreen(
            component = it.component,
            onNavigateUp = navController::goBack,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
