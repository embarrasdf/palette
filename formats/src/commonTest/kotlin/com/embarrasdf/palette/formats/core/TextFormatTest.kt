package com.embarrasdf.palette.formats.core

import kotlin.test.Test
import kotlin.test.assertEquals

class TextFormatTest {

    private val testFormat = TextFormat(
        capitalization = Capitalization.Sentence,
        wordDelimiter = " ",
    )

    @Test
    fun sentenceCaseFormatting() {
        val format = testFormat.copy(capitalization = Capitalization.Sentence)

        val testCases = listOf(
            "hello world" to "Hello world",
            "HELLO WORLD" to "HELLO WORLD",
            "hello" to "Hello",
            "test string here" to "Test string here",
            "a" to "A",
            "" to "",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun titleCaseFormatting() {
        val format = testFormat.copy(capitalization = Capitalization.Title)

        val testCases = listOf(
            "hello world" to "Hello World",
            "hello world test" to "Hello World Test",
            "a b c" to "A B C",
            "test" to "Test",
            "" to "",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun uppercaseFormatting() {
        val format = testFormat.copy(capitalization = Capitalization.Uppercase)

        val testCases = listOf(
            "hello world" to "HELLO WORLD",
            "Hello World" to "HELLO WORLD",
            "test" to "TEST",
            "Test String" to "TEST STRING",
            "" to "",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun lowercaseFormatting() {
        val format = testFormat.copy(capitalization = Capitalization.Lowercase)

        val testCases = listOf(
            "HELLO WORLD" to "hello world",
            "Hello World" to "hello world",
            "TEST" to "test",
            "Test String" to "test string",
            "" to "",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun reverseSentenceCaseFormatting() {
        val format = testFormat.copy(capitalization = Capitalization.ReverseSentence)

        val testCases = listOf(
            "hello world" to "hello World",
            "test string" to "test String",
            "hello" to "hello",
            "a b c" to "a B C",
            "" to "",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun customWordDelimiter() {
        val format = testFormat.copy(
            capitalization = Capitalization.Title,
            wordDelimiter = "-",
        )

        val testCases = listOf(
            "hello world" to "Hello-World",
            "test string here" to "Test-String-Here",
            "hello-world" to "Hello-world", // Does not replace *by* word delimiter
            "test" to "Test",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun customWordDelimiterWithUnderscore() {
        val format = testFormat.copy(
            capitalization = Capitalization.Lowercase,
            wordDelimiter = "_",
        )

        val testCases = listOf(
            "hello world" to "hello_world",
            "Hello World" to "hello_world",
            "Test String" to "test_string",
            "test" to "test",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun replacements() {
        val format = testFormat.copy(
            capitalization = Capitalization.Sentence,
            replacements = mapOf(
                "..." to "\u2026",
                "--" to "\u2014",
                "'" to "\u2019",
            ),
        )

        val testCases = listOf(
            "hello..." to "Hello\u2026",
            "test--case" to "Test\u2014case",
            "it's working" to "It\u2019s working",
            "wait... it's here--really" to "Wait\u2026 it\u2019s here\u2014really",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun smartPunctuationPreset() {
        val format = testFormat.copy(
            capitalization = Capitalization.Title,
            replacements = mapOf(
                TextFormatReplacements.UNICODE_ELLIPSES,
                TextFormatReplacements.UNICODE_EM_DASH,
            ),
        )

        val testCases = listOf(
            "hello world..." to "Hello World\u2026",
            "wait -- really" to "Wait \u2014 Really",
            "that's -- great...right" to "That's \u2014 Great\u2026right",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun punctuationWithCustomDelimiter() {
        val format = testFormat.copy(
            capitalization = Capitalization.Lowercase,
            wordDelimiter = "_",
            replacements = mapOf("..." to "\u2026"),
        )

        val testCases = listOf(
            "hello world..." to "hello_world\u2026",
            "test ... case" to "test_\u2026_case",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun bulletPointReplacement() {
        val format = testFormat.copy(
            capitalization = Capitalization.Sentence,
            replacements = mapOf(
                "\n* " to "\n\u2022 ",
            ),
        )

        val testCases = listOf(
            "text\n* item one" to "Text\n\u2022 item one",
            "text\n* first item" to "Text\n\u2022 first item",
            "2 * 3 = 6" to "2 * 3 = 6", // Math operator not converted
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun smartPunctuationWithLists() {
        val format = testFormat.copy(
            capitalization = Capitalization.Title,
            replacements = mapOf(TextFormatReplacements.NEWLINE_UNICODE_BULLET),
        )

        val testCases = listOf(
            "text\n* item one" to "Text\n\u2022 Item One",
            "list\n* to-do item" to "List\n\u2022 To-do Item",
            "text\n* another item" to "Text\n\u2022 Another Item",
            "use * for wildcard" to "Use * For Wildcard", // Not converted - no newline prefix
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun alternatingCaseFormatting() {
        val format = testFormat.copy(capitalization = Capitalization.Alternating(capitalizeFirstChar = true))

        val testCases = listOf(
            "hello world" to "HeLlO wOrLd",
            "test" to "TeSt",
            "a" to "A",
            "ab" to "Ab",
            "abc" to "AbC",
            "" to "",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }

    @Test
    fun alternatingCaseFormattingLowercaseFirst() {
        val format = testFormat.copy(capitalization = Capitalization.Alternating(capitalizeFirstChar = false))

        val testCases = listOf(
            "hello world" to "hElLo WoRlD",
            "test" to "tEsT",
            "a" to "a",
            "ab" to "aB",
            "abc" to "aBc",
            "" to "",
        )

        testCases.forEach { (string, expected) ->
            assertEquals(expected, format.format(string), "Failed for string: $string")
        }
    }
}
