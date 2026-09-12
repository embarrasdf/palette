package com.embarrasdf.palette.app.demo.components.color.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ColorComponentsRoute : NavKey

@Serializable
@SerialName("color-components")
data object ColorComponentsGraph : ColorComponentsRoute, NavGraphRoute {
    override val pathSegment = "color".toPathSegment()
}

@Serializable
@SerialName("color-component-catalog")
data object ColorComponentCatalogRoute : ColorComponentsRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("color-component")
data class ColorComponentRoute(
    override val ordinal: Int,
) : ColorComponentsRoute, EnumNavKey<ColorComponent> {
    override val entries = ColorComponent.entries

    val component get() = value
    constructor(component: ColorComponent) : this(component.ordinal)
    constructor(pathSegment: PathSegment) : this(component = pathSegment.toEnumEntry(ColorComponent.entries))
}
