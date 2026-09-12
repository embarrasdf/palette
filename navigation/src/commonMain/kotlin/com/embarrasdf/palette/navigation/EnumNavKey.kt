package com.embarrasdf.palette.navigation

import androidx.compose.runtime.Stable
import kotlin.enums.EnumEntries

@Stable
interface EnumNavKey<T : Enum<T>> : NavKey {
    val ordinal: Int
    val entries: EnumEntries<T>

    val value: T
        get() = entries[ordinal]

    override val pathSegment: PathSegment
        get() = value.name.toPathSegment()
}

fun <T : Enum<T>> PathSegment.toEnumEntry(entries: EnumEntries<T>): T {
    if (this == PathSegment.Wildcard) error("Cannot convert wildcard to enum entry")
    return entries.find { it.name.toPathSegment() == this }
        ?: error("Invalid enum path segment: $this")
}
