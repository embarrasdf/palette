package com.embarrasdf.palette.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Composable
fun rememberNavController(state: NavState): NavController {
    return remember(state) { NavController(state) }
}

@Stable
class NavController(val state: NavState) {
    fun navigate(
        route: NavKey,
        replace: Boolean = false,
    ) {
        state.navigate(route, replace)
    }

    fun navigateToDeeplink(deeplink: String, replace: Boolean = false) {
        val route = NavKey.fromDeeplink(deeplink, state.navGraph) ?: return
        navigate(route, replace)
    }

    fun goBack() {
        state.goBack()
    }

    fun navigateUp(): Boolean {
        return state.navigateUp()
    }

    fun popUpTo(route: NavKey, inclusive: Boolean = false): Boolean {
        return state.popUpTo(route, inclusive)
    }
}
