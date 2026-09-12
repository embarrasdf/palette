package com.embarrasdf.palette.app.demo.components

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class Component : CatalogItem {
    Auth,
    Color,
    Core,
    Geometry,
    Media,
    Money,
    ;

    override val title = this.name
}
