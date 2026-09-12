package com.embarrasdf.palette.app.demo.formats.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.formats.core.navigation.CoreFormatsGraph
import com.embarrasdf.palette.app.demo.formats.core.navigation.coreFormatsEntryProvider
import com.embarrasdf.palette.app.demo.formats.core.navigation.coreFormatsNavGraph
import com.embarrasdf.palette.app.demo.formats.datetime.navigation.DateTimeFormatsGraph
import com.embarrasdf.palette.app.demo.formats.datetime.navigation.dateTimeFormatsEntryProvider
import com.embarrasdf.palette.app.demo.formats.datetime.navigation.dateTimeFormatsNavGraph
import com.embarrasdf.palette.app.demo.formats.money.navigation.MoneyFormatsGraph
import com.embarrasdf.palette.app.demo.formats.money.navigation.moneyFormatsEntryProvider
import com.embarrasdf.palette.app.demo.formats.money.navigation.moneyFormatsNavGraph
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.formatsNavGraph() = navGraph(
    root = FormatsGraph,
    start = FormatCatalogRoute,
) {
    route(FormatCatalogRoute)
    coreFormatsNavGraph()
    dateTimeFormatsNavGraph()
    moneyFormatsNavGraph()
}

fun EntryProviderScope<NavKey>.formatsEntryProvider(
    navController: NavController,
) {
    catalogEntry<FormatCatalogRoute, FormatCategory>(
        onItemClick = { category ->
            val targetRoute = when (category) {
                FormatCategory.Core -> CoreFormatsGraph
                FormatCategory.DateTime -> DateTimeFormatsGraph
                FormatCategory.Money -> MoneyFormatsGraph
            }
            navController.navigate(targetRoute)
        },
        title = "Formats",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )

    coreFormatsEntryProvider(navController)
    dateTimeFormatsEntryProvider(navController)
    moneyFormatsEntryProvider(navController)
}
