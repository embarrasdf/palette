package com.embarrasdf.palette.app.demo.components.color.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class ColorComponent : CatalogItem {
    ColorPicker,
    ;

    override val title = this.name
}
