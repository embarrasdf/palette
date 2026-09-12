package com.embarrasdf.palette.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.embarrasdf.palette.app.demo.components.navigation.componentsEntryProvider
import com.embarrasdf.palette.app.demo.components.navigation.componentsNavGraph
import com.embarrasdf.palette.app.demo.formats.navigation.formatsEntryProvider
import com.embarrasdf.palette.app.demo.formats.navigation.formatsNavGraph
import com.embarrasdf.palette.app.demo.modifiers.navigation.modifiersEntryProvider
import com.embarrasdf.palette.app.demo.modifiers.navigation.modifiersNavGraph
import com.embarrasdf.palette.app.main.navigation.MainGraph
import com.embarrasdf.palette.app.main.navigation.mainEntryProvider
import com.embarrasdf.palette.app.main.navigation.mainNavGraph
import com.embarrasdf.palette.app.theme.navigation.themeEntryProvider
import com.embarrasdf.palette.app.theme.navigation.themeNavGraph
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.navGraph
import com.embarrasdf.palette.navigation.rememberNavController
import com.embarrasdf.palette.navigation.rememberNavState
import com.embarrasdf.palette.navigation.toPathSegment
import com.embarrasdf.palette.theme.control.ThemeController
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("palette")
data object PaletteGraph : NavGraphRoute {
    override val pathSegment = "".toPathSegment()
}

val PaletteNavGraph = navGraph(root = PaletteGraph, start = MainGraph) {
    mainNavGraph()
    componentsNavGraph()
    formatsNavGraph()
    modifiersNavGraph()
    themeNavGraph()
}

@Composable
fun PaletteNav(
    themeController: ThemeController,
    navController: NavController = rememberPaletteNavController(),
) {
    NavDisplay(
        backStack = navController.state.backStack,
        entryProvider = entryProvider {
            paletteEntryProvider(
                navController = navController,
                themeController = themeController,
            )
        },
        onBack = navController::goBack,
    )
}

@Composable
fun rememberPaletteNavController(
    initialDeeplink: String? = null,
    buildSyntheticBackStack: Boolean = true,
    onBackStackEmpty: () -> Unit = {},
) = rememberNavController(
    state = rememberPaletteNavState(
        initialDeeplink = initialDeeplink,
        buildSyntheticBackStack = buildSyntheticBackStack,
        onBackStackEmpty = onBackStackEmpty,
    ),
)

@Composable
fun rememberPaletteNavState(
    initialDeeplink: String? = null,
    buildSyntheticBackStack: Boolean = true,
    onBackStackEmpty: () -> Unit = {},
) = rememberNavState(
    navGraph = PaletteNavGraph,
    initialDeeplink = initialDeeplink,
    buildSyntheticBackStack = buildSyntheticBackStack,
    onWouldBecomeEmpty = onBackStackEmpty,
)

fun EntryProviderScope<NavKey>.paletteEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    mainEntryProvider(navController)
    componentsEntryProvider(navController)
    formatsEntryProvider(navController)
    modifiersEntryProvider(navController)
    themeEntryProvider(navController, themeController)
}
