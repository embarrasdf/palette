package com.embarrasdf.palette.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.reflect.KClass
import androidx.navigation3.runtime.NavKey as Navigation3NavKey

@Composable
fun rememberNavKeyJson(
    navGraph: NavGraph,
    builder: NavKeyJsonBuilder.() -> Unit = {},
): Json {
    val config = NavKeyJsonBuilder().apply(builder).toConfig()
    return remember(navGraph, config) {
        Json(from = config.from ?: Json.Default) {
            serializersModule = navGraph.toSerializersModule()
            ignoreUnknownKeys = config.ignoreUnknownKeys
            classDiscriminator = config.classDiscriminator
        }
    }
}

class NavKeyJsonBuilder {
    var from: Json? = null
    var ignoreUnknownKeys: Boolean = true
    var classDiscriminator: String = "type"
}

fun NavGraph.toSerializersModule(): SerializersModule = SerializersModule {
    polymorphic(Navigation3NavKey::class) {
        fun collect(nodes: List<NavGraphNode>) {
            nodes.forEach { node ->
                if (!node.isGraph) {
                    @Suppress("UNCHECKED_CAST")
                    subclass(
                        node.navKeyClass as KClass<NavKey>,
                        node.serializer as KSerializer<NavKey>,
                    )
                }
                collect(node.children)
            }
        }
        collect(this@toSerializersModule.nodes)
    }
}

private data class NavKeyJsonConfig(
    val from: Json?,
    val ignoreUnknownKeys: Boolean,
    val classDiscriminator: String,
)

private fun NavKeyJsonBuilder.toConfig() = NavKeyJsonConfig(
    from = from,
    ignoreUnknownKeys = ignoreUnknownKeys,
    classDiscriminator = classDiscriminator,
)
