package com.embarrasdf.palette.app.demo.components.auth.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.components.auth.AuthComponentScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder

fun NavGraphBuilder.authComponentsNavGraph() = navGraph(
    root = AuthComponentsGraph,
    start = AuthComponentCatalogRoute,
) {
    route(AuthComponentCatalogRoute)
    wildcardRoute<AuthComponentRoute> { pathSegment ->
        AuthComponentRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.authComponentsEntryProvider(
    navController: NavController,
) {
    catalogEntry<AuthComponentCatalogRoute, AuthComponent>(
        onItemClick = { component ->
            navController.navigate(AuthComponentRoute(component))
        },
        title = "Auth",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<AuthComponentRoute> {
        AuthComponentScreen(
            component = it.component,
            onNavigateUp = navController::navigateUp,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
