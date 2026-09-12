package com.embarrasdf.palette.app.demo.components.core.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class CoreComponent : CatalogItem {
    Button,
    Icon,
    Slider,
    Text,
    TextField,
    ;

    override val title = this.name
}
