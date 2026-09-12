package com.embarrasdf.palette.app.demo.components.media.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.components.media.MediaComponentScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder

fun NavGraphBuilder.mediaComponentsNavGraph() = navGraph(
    root = MediaComponentsGraph,
    start = MediaComponentCatalogRoute,
) {
    route(MediaComponentCatalogRoute)
    wildcardRoute<MediaComponentRoute> { pathSegment ->
        MediaComponentRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.mediaComponentsEntryProvider(
    navController: NavController,
) {
    catalogEntry<MediaComponentCatalogRoute, MediaComponent>(
        onItemClick = { component ->
            navController.navigate(MediaComponentRoute(component))
        },
        title = "Media",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<MediaComponentRoute> {
        MediaComponentScreen(
            component = it.component,
            onNavigateUp = navController::goBack,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
