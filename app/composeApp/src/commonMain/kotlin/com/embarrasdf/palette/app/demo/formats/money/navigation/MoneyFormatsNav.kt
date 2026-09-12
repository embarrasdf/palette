package com.embarrasdf.palette.app.demo.formats.money.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.formats.money.MoneyFormatScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder

fun NavGraphBuilder.moneyFormatsNavGraph() = navGraph(
    root = MoneyFormatsGraph,
    start = MoneyFormatCatalogRoute,
) {
    route(MoneyFormatCatalogRoute)
    wildcardRoute<MoneyFormatRoute> { pathSegment ->
        MoneyFormatRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.moneyFormatsEntryProvider(
    navController: NavController,
) {
    catalogEntry<MoneyFormatCatalogRoute, MoneyFormat>(
        onItemClick = { format ->
            navController.navigate(MoneyFormatRoute(format))
        },
        title = "Money",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<MoneyFormatRoute> {
        MoneyFormatScreen(
            format = it.format,
            onNavigateUp = navController::goBack,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
