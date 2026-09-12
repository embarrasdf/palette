package com.embarrasdf.palette.app.demo.formats.core.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class CoreFormat : CatalogItem {
    Number,
    Text,
    ;

    override val title = this.name
}
