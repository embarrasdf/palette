package com.embarrasdf.palette.app.demo.formats.core.navigation

import com.embarrasdf.palette.navigation.EnumNavKey
import com.embarrasdf.palette.navigation.NavGraphRoute
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toEnumEntry
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface CoreFormatsRoute : NavKey

@Serializable
@SerialName("core-formats")
data object CoreFormatsGraph : CoreFormatsRoute, NavGraphRoute {
    override val pathSegment = "core".toPathSegment()
}

@Serializable
@SerialName("core-format-catalog")
data object CoreFormatCatalogRoute : CoreFormatsRoute {
    override val pathSegment = "catalog".toPathSegment()
}

@Serializable
@SerialName("core-format")
data class CoreFormatRoute(
    override val ordinal: Int,
) : CoreFormatsRoute, EnumNavKey<CoreFormat> {
    override val entries = CoreFormat.entries

    val format get() = value
    constructor(format: CoreFormat) : this(format.ordinal)
    constructor(pathSegment: PathSegment) : this(format = pathSegment.toEnumEntry(CoreFormat.entries))
}
