package com.embarrasdf.palette.app.theme.semantic.animation.navigation.infinite

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.infiniteNavGraph() = navGraph(
    root = InfiniteGraph,
    start = InfiniteCatalogRoute,
) {
    route(InfiniteCatalogRoute)
}

fun EntryProviderScope<NavKey>.infiniteEntryProvider(
    navController: NavController,
) {
    catalogEntry<InfiniteCatalogRoute, InfiniteItem>(
        onItemClick = { },
        title = "Infinite",
        onNavigateUp = navController::goBack,
    )
}
