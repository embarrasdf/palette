package com.embarrasdf.palette.components.demo.util

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.text.input.KeyboardCapitalization

private enum class KeyboardCapitalizationEnum(
    val keyboardCapitalization: KeyboardCapitalization,
) {
    Unspecified(KeyboardCapitalization.Unspecified),
    None(KeyboardCapitalization.None),
    Characters(KeyboardCapitalization.Characters),
    Words(KeyboardCapitalization.Words),
    Sentences(KeyboardCapitalization.Sentences),
}

private fun KeyboardCapitalization.toEnum() =
    KeyboardCapitalizationEnum.entries.find { it.keyboardCapitalization == this }
        ?: KeyboardCapitalizationEnum.Unspecified

val KeyboardCapitalizationSaver = object : Saver<KeyboardCapitalization, Any> {
    override fun SaverScope.save(value: KeyboardCapitalization): Any? {
        return value.toEnum().name
    }

    override fun restore(value: Any): KeyboardCapitalization? {
        // Unknown names restore to null rather than throwing, like an absent value.
        return KeyboardCapitalizationEnum.entries.find { it.name == value }?.keyboardCapitalization
    }
}
