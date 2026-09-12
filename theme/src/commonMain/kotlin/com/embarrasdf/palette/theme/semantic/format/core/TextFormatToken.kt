package com.embarrasdf.palette.theme.semantic.format.core

import com.embarrasdf.palette.formats.core.Capitalization
import com.embarrasdf.palette.formats.core.TextFormat
import com.embarrasdf.palette.formats.core.TextFormatReplacements

enum class TextFormatToken {
    Body,
    Display,
    Headline,
    Label,
    Title,
}

fun TextFormatToken.toFormat(): TextFormat {
    val defaultPunctuationReplacements = mapOf(
        TextFormatReplacements.UNICODE_ELLIPSES,
        TextFormatReplacements.UNICODE_EM_DASH,
        TextFormatReplacements.NEWLINE_UNICODE_BULLET
    )
    return when (this) {
        TextFormatToken.Body -> TextFormat(
            capitalization = Capitalization.None,
            wordDelimiter = " ",
            replacements = defaultPunctuationReplacements,
        )
        TextFormatToken.Display -> TextFormat(
            capitalization = Capitalization.Sentence,
            wordDelimiter = " ",
            replacements = defaultPunctuationReplacements,
        )
        TextFormatToken.Headline -> TextFormat(
            capitalization = Capitalization.Sentence,
            wordDelimiter = " ",
            replacements = defaultPunctuationReplacements,
        )
        TextFormatToken.Label -> TextFormat(
            capitalization = Capitalization.Title,
            wordDelimiter = " ",
            replacements = defaultPunctuationReplacements,
        )
        TextFormatToken.Title -> TextFormat(
            capitalization = Capitalization.Title,
            wordDelimiter = " ",
            replacements = defaultPunctuationReplacements,
        )
    }
}

fun TextFormatToken.toFormat(scheme: TextFormatScheme): TextFormat {
    return when (this) {
        TextFormatToken.Body -> scheme.body
        TextFormatToken.Display -> scheme.display
        TextFormatToken.Headline -> scheme.headline
        TextFormatToken.Label -> scheme.label
        TextFormatToken.Title -> scheme.title
    }
}
