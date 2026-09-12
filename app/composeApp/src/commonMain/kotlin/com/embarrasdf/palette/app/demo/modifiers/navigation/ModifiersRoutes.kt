package com.embarrasdf.palette.app.demo.modifiers.navigation

import com.embarrasdf.palette.app.demo.modifiers.DemoModifier
import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ModifiersRoute : NavKey

@Serializable
@SerialName("modifiers")
data object ModifiersGraph : ModifiersRoute, NavGraphRoute {
    override val pathSegment = "modifiers".toPathSegment()
}

@Serializable
@SerialName("modifier-catalog")
data object ModifierCatalogRoute : ModifiersRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("modifier")
data class ModifierRoute(
    override val ordinal: Int,
) : ModifiersRoute, EnumNavKey<DemoModifier> {
    override val entries = DemoModifier.entries

    val modifier get() = value
    constructor(modifier: DemoModifier) : this(modifier.ordinal)
    constructor(pathSegment: PathSegment) : this(modifier = pathSegment.toEnumEntry(DemoModifier.entries))
}
