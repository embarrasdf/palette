package com.embarrasdf.palette.app.demo.formats.datetime.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.formats.datetime.DateTimeFormatScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.dateTimeFormatsNavGraph() = navGraph(
    root = DateTimeFormatsGraph,
    start = DateTimeFormatCatalogRoute,
) {
    route(DateTimeFormatCatalogRoute)
    wildcardRoute<DateTimeFormatRoute> { pathSegment ->
        DateTimeFormatRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.dateTimeFormatsEntryProvider(
    navController: NavController,
) {
    catalogEntry<DateTimeFormatCatalogRoute, DateTimeFormat>(
        onItemClick = { format ->
            navController.navigate(DateTimeFormatRoute(format))
        },
        title = "DateTime",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<DateTimeFormatRoute> {
        DateTimeFormatScreen(
            format = it.format,
            onNavigateUp = navController::goBack,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
