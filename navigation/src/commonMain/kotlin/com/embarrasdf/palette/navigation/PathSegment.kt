package com.embarrasdf.palette.navigation

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class PathSegment(val value: String) {
    companion object {
        val Wildcard = PathSegment("*")
    }

    override fun toString(): String {
        return value
    }
}

fun String.toPathSegment() = PathSegment(
    value = this.replace(Regex("([a-z])([A-Z])"), "$1-$2").lowercase(),
)
