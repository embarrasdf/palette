package com.embarrasdf.palette.app.demo.components.auth.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class AuthComponent : CatalogItem {
    Button,
    ;

    override val title = this.name
}
