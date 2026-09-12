package com.embarrasdf.palette.app.demo.components.money.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface MoneyComponentsRoute : NavKey

@Serializable
@SerialName("money-components")
data object MoneyComponentsGraph : MoneyComponentsRoute, NavGraphRoute {
    override val pathSegment = "money".toPathSegment()
}

@Serializable
@SerialName("money-catalog")
data object MoneyComponentCatalogRoute : MoneyComponentsRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("money-component")
data class MoneyComponentRoute(
    override val ordinal: Int,
) : MoneyComponentsRoute, EnumNavKey<MoneyComponent> {
    override val entries = MoneyComponent.entries

    val component get() = value
    constructor(component: MoneyComponent) : this(component.ordinal)
    constructor(pathSegment: PathSegment) : this(component = pathSegment.toEnumEntry(MoneyComponent.entries))
}
