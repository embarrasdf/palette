package com.embarrasdf.palette.app.demo.components.money.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.components.money.MoneyComponentScreen
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.NavGraphBuilder

fun NavGraphBuilder.moneyComponentsNavGraph() = navGraph(
    root = MoneyComponentsGraph,
    start = MoneyComponentCatalogRoute,
) {
    route(MoneyComponentCatalogRoute)
    wildcardRoute<MoneyComponentRoute> { pathSegment ->
        MoneyComponentRoute(pathSegment)
    }
}

fun EntryProviderScope<NavKey>.moneyComponentsEntryProvider(
    navController: NavController,
) {
    catalogEntry<MoneyComponentCatalogRoute, MoneyComponent>(
        onItemClick = { component ->
            navController.navigate(MoneyComponentRoute(component))
        },
        title = "Money",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    entry<MoneyComponentRoute> {
        MoneyComponentScreen(
            component = it.component,
            onNavigateUp = navController::goBack,
            onThemeClick = {
                navController.navigate(ThemeGraph)
            },
        )
    }
}
