package com.embarrasdf.palette.components.demo.util

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope

// Accepts what an Android Bundle can hold, so a value the platform can't save fails here too.
private val PlatformSaverScope = SaverScope { it.isPlatformSaveable() }

private fun Any?.isPlatformSaveable(): Boolean = when (this) {
    null, is Boolean, is Number, is String, is Char, is Enum<*> -> true
    is List<*> -> all { it.isPlatformSaveable() }
    else -> false
}

fun <T : Any> Saver<T, Any>.saveAndRestore(value: T): T {
    val saved = requireNotNull(with(this) { PlatformSaverScope.save(value) }) { "Saved nothing" }
    check(saved.isPlatformSaveable()) { "Saved a value the platform can't hold: $saved" }
    return requireNotNull(restore(saved)) { "Restored nothing" }
}
