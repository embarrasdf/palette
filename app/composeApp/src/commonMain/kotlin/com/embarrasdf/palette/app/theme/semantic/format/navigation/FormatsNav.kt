package com.embarrasdf.palette.app.theme.semantic.format.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.format.MoneyFormatScreen
import com.embarrasdf.palette.app.theme.semantic.format.NumberFormatScreen
import com.embarrasdf.palette.app.theme.semantic.format.TextFormatScreen
import com.embarrasdf.palette.app.theme.semantic.format.datetime.navigation.DateTimeFormatGraph
import com.embarrasdf.palette.app.theme.semantic.format.datetime.navigation.dateTimeFormatEntryProvider
import com.embarrasdf.palette.app.theme.semantic.format.datetime.navigation.dateTimeFormatNavGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.formatNavGraph() = navGraph(
    root = FormatsGraph,
    start = FormatCatalogRoute,
) {
    route(FormatCatalogRoute)
    route(NumberFormatRoute)
    route(MoneyFormatRoute)
    route(TextFormatRoute)

    dateTimeFormatNavGraph()
}

fun EntryProviderScope<NavKey>.formatsEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<FormatCatalogRoute, Format>(
        onItemClick = { format ->
            when (format) {
                Format.DateTime -> navController.navigate(DateTimeFormatGraph)
                Format.Money -> navController.navigate(MoneyFormatRoute)
                Format.Number -> navController.navigate(NumberFormatRoute)
                Format.Text -> navController.navigate(TextFormatRoute)
            }
        },
        title = "Format",
        onNavigateUp = navController::goBack,
    )

    entry<MoneyFormatRoute> {
        MoneyFormatScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<NumberFormatRoute> {
        NumberFormatScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<TextFormatRoute> {
        TextFormatScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    dateTimeFormatEntryProvider(navController, themeController)
}
