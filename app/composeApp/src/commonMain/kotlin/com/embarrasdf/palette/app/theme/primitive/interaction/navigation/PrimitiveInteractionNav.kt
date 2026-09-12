package com.embarrasdf.palette.app.theme.primitive.interaction.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.primitive.interaction.IndicationScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.primitiveInteractionNavGraph() = navGraph(
    root = PrimitiveInteractionGraph,
    start = PrimitiveInteractionCatalogRoute,
) {
    route(PrimitiveInteractionCatalogRoute)
    route(PrimitiveIndicationRoute)
}

fun EntryProviderScope<NavKey>.primitiveInteractionEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<PrimitiveInteractionCatalogRoute, PrimitiveInteraction>(
        onItemClick = { interaction ->
            when (interaction) {
                PrimitiveInteraction.Indication -> navController.navigate(PrimitiveIndicationRoute)
            }
        },
        title = "Interaction",
        onNavigateUp = navController::goBack,
    )

    entry<PrimitiveIndicationRoute> {
        IndicationScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }
}
