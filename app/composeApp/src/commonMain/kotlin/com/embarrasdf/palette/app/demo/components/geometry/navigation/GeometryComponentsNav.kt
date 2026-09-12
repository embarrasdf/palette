package com.embarrasdf.palette.app.demo.components.geometry.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.components.geometry.GeometryComponentScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder

fun NavGraphBuilder.geometryComponentsNavGraph() = navGraph(
    root = GeometryComponentsGraph,
    start = GeometryComponentCatalogRoute,
) {
    route(GeometryComponentCatalogRoute)
    wildcardRoute<GeometryComponentRoute> { pathSegment ->
        GeometryComponentRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.geometryComponentsEntryProvider(
    navController: NavController,
) {
    catalogEntry<GeometryComponentCatalogRoute, GeometryComponent>(
        onItemClick = { component ->
            navController.navigate(GeometryComponentRoute(component))
        },
        title = "Geometry",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<GeometryComponentRoute> {
        GeometryComponentScreen(
            component = it.component,
            onNavigateUp = navController::goBack,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
