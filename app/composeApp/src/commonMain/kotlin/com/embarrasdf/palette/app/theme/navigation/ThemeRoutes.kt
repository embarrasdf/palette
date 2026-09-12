package com.embarrasdf.palette.app.theme.navigation

import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ThemeRoute : NavKey

@Serializable
@SerialName("theme")
data object ThemeGraph : ThemeRoute, NavGraphRoute {
    override val pathSegment = "theme".toPathSegment()
}

@Serializable
@SerialName("theme-catalog")
data object ThemeCatalogRoute : ThemeRoute {
    override val pathSegment = "catalog".toPathSegment()
}
