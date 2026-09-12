package com.embarrasdf.palette.app.demo.components.auth.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AuthComponentsRoute : NavKey

@Serializable
@SerialName("auth")
data object AuthComponentsGraph : AuthComponentsRoute, NavGraphRoute {
    override val pathSegment = "auth".toPathSegment()
}

@Serializable
@SerialName("auth-component-catalog")
data object AuthComponentCatalogRoute : AuthComponentsRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("auth-component")
data class AuthComponentRoute(
    override val ordinal: Int,
) : AuthComponentsRoute, EnumNavKey<AuthComponent> {
    override val entries = AuthComponent.entries

    val component get() = value
    constructor(component: AuthComponent) : this(component.ordinal)
    constructor(pathSegment: PathSegment) : this(component = pathSegment.toEnumEntry(AuthComponent.entries))
}
