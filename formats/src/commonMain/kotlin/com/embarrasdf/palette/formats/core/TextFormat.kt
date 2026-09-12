package com.embarrasdf.palette.formats.core

data class TextFormat(
    val capitalization: Capitalization = Capitalization.Sentence,
    val wordDelimiter: String = " ",
    val replacements: Map<String, String> = emptyMap(),
)

sealed class Capitalization {
    data object None : Capitalization()
    data object Sentence : Capitalization()
    data object Title : Capitalization()
    data object Uppercase : Capitalization()
    data object Lowercase : Capitalization()
    data object ReverseSentence : Capitalization()
    data class Alternating(val capitalizeFirstChar: Boolean = true) : Capitalization()

    enum class Key {
        None,
        Sentence,
        Title,
        Uppercase,
        Lowercase,
        ReverseSentence,
        Alternating,
    }

    companion object {
        fun toKey(value: Capitalization): Key = when (value) {
            None -> Key.None
            Sentence -> Key.Sentence
            Title -> Key.Title
            Uppercase -> Key.Uppercase
            Lowercase -> Key.Lowercase
            ReverseSentence -> Key.ReverseSentence
            is Alternating -> Key.Alternating
        }

        fun fromKey(
            key: Key,
            capitalizeFirstChar: Boolean = true,
        ): Capitalization = when (key) {
            Key.None -> None
            Key.Sentence -> Sentence
            Key.Title -> Title
            Key.Uppercase -> Uppercase
            Key.Lowercase -> Lowercase
            Key.ReverseSentence -> ReverseSentence
            Key.Alternating -> Alternating(capitalizeFirstChar)
        }
    }
}

object TextFormatReplacements {
    val UNICODE_ELLIPSES = "..." to "\u2026"
    val UNICODE_EM_DASH = "--" to "\u2014"
    val NEWLINE_UNICODE_BULLET = "\n* " to "\n\u2022 "
}

fun TextFormat.format(
    string: String,
): String {
    if (string.isEmpty()) return string

    if (wordDelimiter == " ") {
        val formatted = when (capitalization) {
            Capitalization.None -> string
            Capitalization.Sentence -> string.titlecaseFirstChar()
            Capitalization.Title -> {
                val words = string.split(' ')
                if (words.size == 1) string.titlecaseFirstChar()
                else words.joinToString(wordDelimiter) { it.titlecaseFirstChar() }
            }
            Capitalization.Uppercase -> string.uppercase()
            Capitalization.Lowercase -> string.lowercase()
            Capitalization.ReverseSentence -> string.split(' ').formatReverseSentenceCase(wordDelimiter)
            is Capitalization.Alternating -> string.formatAlternatingCase(capitalization.capitalizeFirstChar)
        }
        return formatted.applyReplacements(replacements)
    }

    val words = string.split(" ")
    if (words.isEmpty()) return ""

    val formatted = when (capitalization) {
        Capitalization.None -> words.joinToString(wordDelimiter)
        Capitalization.Sentence -> {
            val firstWord = words[0].titlecaseFirstChar()
            if (words.size == 1) firstWord
            else buildString {
                append(firstWord)
                for (i in 1 until words.size) {
                    append(wordDelimiter)
                    append(words[i])
                }
            }
        }
        Capitalization.Title -> words.joinToString(wordDelimiter) { it.titlecaseFirstChar() }
        Capitalization.Uppercase -> words.joinToString(wordDelimiter) { it.uppercase() }
        Capitalization.Lowercase -> words.joinToString(wordDelimiter) { it.lowercase() }
        Capitalization.ReverseSentence -> words.formatReverseSentenceCase(wordDelimiter)
        is Capitalization.Alternating -> words.joinToString(wordDelimiter).formatAlternatingCase(capitalization.capitalizeFirstChar)
    }

    return formatted.applyReplacements(replacements)
}

private fun List<String>.formatReverseSentenceCase(delimiter: String): String {
    if (isEmpty()) return ""
    val firstWord = this[0].lowercaseFirstChar()
    if (size == 1) return firstWord
    return buildString {
        append(firstWord)
        for (i in 1 until size) {
            append(delimiter)
            append(this@formatReverseSentenceCase[i].titlecaseFirstChar())
        }
    }
}

private fun String.titlecaseFirstChar() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
private fun String.lowercaseFirstChar() = replaceFirstChar { if (it.isUpperCase()) it.lowercase() else it.toString() }

private fun String.formatAlternatingCase(capitalizeFirstChar: Boolean): String {
    if (isEmpty()) return this

    val result = StringBuilder()
    var letterIndex = 0

    for (char in this) {
        if (char.isWhitespace()) {
            result.append(char)
        } else {
            val shouldCapitalize = if (capitalizeFirstChar) {
                letterIndex % 2 == 0
            } else {
                letterIndex % 2 == 1
            }
            result.append(if (shouldCapitalize) char.uppercase() else char.lowercase())
            letterIndex++
        }
    }

    return result.toString()
}

private fun String.applyReplacements(replacements: Map<String, String>): String {
    if (replacements.isEmpty()) return this

    var result = this
    for ((from, to) in replacements) {
        result = result.replace(from, to)
    }
    return result
}
