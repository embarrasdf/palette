package com.embarrasdf.palette.app.theme.semantic.format.datetime.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.format.datetime.DateFormatSchemeScreen
import com.embarrasdf.palette.app.theme.semantic.format.datetime.DateTimeFormatSchemeScreen
import com.embarrasdf.palette.app.theme.semantic.format.datetime.InstantFormatSchemeScreen
import com.embarrasdf.palette.app.theme.semantic.format.datetime.TimeFormatSchemeScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.dateTimeFormatNavGraph() = navGraph(
    root = DateTimeFormatGraph,
    start = DateTimeFormatCatalogRoute,
) {
    route(DateTimeFormatCatalogRoute)
    wildcardRoute<DateTimeFormatItemRoute> { pathSegment ->
        DateTimeFormatItemRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.dateTimeFormatEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<DateTimeFormatCatalogRoute, DateTimeFormatCatalogItem>(
        onItemClick = { item ->
            navController.navigate(DateTimeFormatItemRoute(item.ordinal))
        },
        title = "DateTime",
        onNavigateUp = navController::goBack,
    )

    entry<DateTimeFormatItemRoute> { route ->
        val format = DateTimeFormatCatalogItem.entries[route.ordinal]
        when (format) {
            DateTimeFormatCatalogItem.Date -> DateFormatSchemeScreen(
                themeController = themeController,
                onNavigateUp = navController::goBack,
            )
            DateTimeFormatCatalogItem.DateTime -> DateTimeFormatSchemeScreen(
                themeController = themeController,
                onNavigateUp = navController::goBack,
            )
            DateTimeFormatCatalogItem.Instant -> InstantFormatSchemeScreen(
                themeController = themeController,
                onNavigateUp = navController::goBack,
            )
            DateTimeFormatCatalogItem.Time -> TimeFormatSchemeScreen(
                themeController = themeController,
                onNavigateUp = navController::goBack,
            )
        }
    }
}
