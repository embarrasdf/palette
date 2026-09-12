package com.embarrasdf.palette.app.demo.components.media.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface MediaComponentsRoute : NavKey

@Serializable
@SerialName("media")
data object MediaComponentsGraph : MediaComponentsRoute, NavGraphRoute {
    override val pathSegment = "media".toPathSegment()
}

@Serializable
@SerialName("media-catalog")
data object MediaComponentCatalogRoute : MediaComponentsRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("media-component")
data class MediaComponentRoute(
    override val ordinal: Int,
) : MediaComponentsRoute, EnumNavKey<MediaComponent> {
    override val entries = MediaComponent.entries

    val component get() = value
    constructor(component: MediaComponent) : this(component.ordinal)
    constructor(pathSegment: PathSegment) : this(component = pathSegment.toEnumEntry(MediaComponent.entries))
}
