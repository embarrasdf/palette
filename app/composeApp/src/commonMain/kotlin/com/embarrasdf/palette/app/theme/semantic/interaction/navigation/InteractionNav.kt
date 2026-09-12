package com.embarrasdf.palette.app.theme.semantic.interaction.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.interaction.IndicationScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.interactionNavGraph() = navGraph(
    root = InteractionGraph,
    start = InteractionCatalogRoute,
) {
    route(InteractionCatalogRoute)
    route(IndicationRoute)
}

fun EntryProviderScope<NavKey>.interactionEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<InteractionCatalogRoute, Interaction>(
        onItemClick = { interaction ->
            when (interaction) {
                Interaction.Indication -> navController.navigate(IndicationRoute)
            }
        },
        title = "Interaction",
        onNavigateUp = navController::goBack,
    )

    entry<IndicationRoute> {
        IndicationScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }
}
