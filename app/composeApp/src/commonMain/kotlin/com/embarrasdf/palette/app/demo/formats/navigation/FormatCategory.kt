package com.embarrasdf.palette.app.demo.formats.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class FormatCategory : CatalogItem {
    Core,
    DateTime,
    Money,
    ;

    override val title = this.name
}
