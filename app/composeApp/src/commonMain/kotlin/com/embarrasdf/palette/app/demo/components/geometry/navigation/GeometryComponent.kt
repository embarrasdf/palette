package com.embarrasdf.palette.app.demo.components.geometry.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class GeometryComponent : CatalogItem {
    CurveStitch,
    Grid,
    Sphere,
    ;

    override val title = this.name
}
