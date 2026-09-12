package com.embarrasdf.palette.app.demo.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.demo.components.Component
import com.embarrasdf.palette.app.demo.components.auth.navigation.AuthComponentsGraph
import com.embarrasdf.palette.app.demo.components.auth.navigation.authComponentsEntryProvider
import com.embarrasdf.palette.app.demo.components.auth.navigation.authComponentsNavGraph
import com.embarrasdf.palette.app.demo.components.color.navigation.ColorComponentsGraph
import com.embarrasdf.palette.app.demo.components.color.navigation.colorComponentsEntryProvider
import com.embarrasdf.palette.app.demo.components.color.navigation.colorComponentsNavGraph
import com.embarrasdf.palette.app.demo.components.core.navigation.CoreComponentsGraph
import com.embarrasdf.palette.app.demo.components.core.navigation.coreComponentsEntryProvider
import com.embarrasdf.palette.app.demo.components.core.navigation.coreComponentsNavGraph
import com.embarrasdf.palette.app.demo.components.geometry.navigation.GeometryComponentsGraph
import com.embarrasdf.palette.app.demo.components.geometry.navigation.geometryComponentsEntryProvider
import com.embarrasdf.palette.app.demo.components.geometry.navigation.geometryComponentsNavGraph
import com.embarrasdf.palette.app.demo.components.media.navigation.MediaComponentsGraph
import com.embarrasdf.palette.app.demo.components.media.navigation.mediaComponentsEntryProvider
import com.embarrasdf.palette.app.demo.components.media.navigation.mediaComponentsNavGraph
import com.embarrasdf.palette.app.demo.components.money.navigation.MoneyComponentsGraph
import com.embarrasdf.palette.app.demo.components.money.navigation.moneyComponentsEntryProvider
import com.embarrasdf.palette.app.demo.components.money.navigation.moneyComponentsNavGraph
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.app.theme.navigation.ThemeGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.componentsNavGraph() = navGraph(
    root = ComponentsGraph,
    start = ComponentCatalogRoute,
) {
    route(ComponentCatalogRoute)
    authComponentsNavGraph()
    colorComponentsNavGraph()
    coreComponentsNavGraph()
    geometryComponentsNavGraph()
    mediaComponentsNavGraph()
    moneyComponentsNavGraph()
}

fun EntryProviderScope<NavKey>.componentsEntryProvider(
    navController: NavController,
) {
    catalogEntry<ComponentCatalogRoute, Component>(
        onItemClick = { component ->
            val targetRoute = when (component) {
                Component.Auth -> AuthComponentsGraph
                Component.Color -> ColorComponentsGraph
                Component.Core -> CoreComponentsGraph
                Component.Geometry -> GeometryComponentsGraph
                Component.Media -> MediaComponentsGraph
                Component.Money -> MoneyComponentsGraph
            }
            navController.navigate(targetRoute)
        },
        title = "Components",
        onNavigateUp = navController::navigateUp,
        actions = {
            ThemeButton(
                onClick = { navController.navigate(ThemeGraph) },
            )
        },
    )
    authComponentsEntryProvider(navController)
    colorComponentsEntryProvider(navController)
    coreComponentsEntryProvider(navController)
    geometryComponentsEntryProvider(navController)
    mediaComponentsEntryProvider(navController)
    moneyComponentsEntryProvider(navController)
}
