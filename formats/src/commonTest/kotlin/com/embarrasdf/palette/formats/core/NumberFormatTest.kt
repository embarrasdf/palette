package com.embarrasdf.palette.formats.core

import kotlin.test.Test
import kotlin.test.assertEquals

class NumberFormatTest {

    private val testFormat = NumberFormat(
        numDecimalValuesRange = 0..2,
        positiveSign = null,
        negativeSign = "-",
        decimalSeparator = '.',
        intGrouping = IntGrouping.Uniform(numDigits = 3, separator = ','),
    )

    @Test
    fun basicFormatting() {
        val format = testFormat

        val testCases = listOf(
            0.0 to "0",
            1.0 to "1",
            10.0 to "10",
            100.0 to "100",
            1000.0 to "1,000",
            10000.0 to "10,000",
            100000.0 to "100,000",
            1000000.0 to "1,000,000",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun decimalFormatting() {
        val format = testFormat

        val testCases = listOf(
            0.5 to "0.5",
            1.5 to "1.5",
            10.25 to "10.25",
            100.99 to "100.99",
            1000.01 to "1,000.01",
            1234.56 to "1,234.56",
            0.1 to "0.1",
            0.12 to "0.12",
            0.001 to "0",
            0.01 to "0.01",
            0.99 to "0.99",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun positiveSign() {
        val format = testFormat.copy(
            positiveSign = "+",
        )

        val testCases = listOf(
            1.0 to "+1",
            100.0 to "+100",
            1000.0 to "+1,000",
            1234.56 to "+1,234.56",
            0.0 to "0",
            -1.0 to "-1",
            -100.0 to "-100",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun negativeSign() {
        val format = testFormat.copy(
            negativeSign = "!",
        )

        val testCases = listOf(
            -1.0 to "!1",
            -10.0 to "!10",
            -100.0 to "!100",
            -1000.0 to "!1,000",
            -1234.56 to "!1,234.56",
            -0.5 to "!0.5",
            -0.99 to "!0.99",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun customDecimalRange() {
        val format = testFormat.copy(numDecimalValuesRange = 2..4)

        val testCases = listOf(
            0.0 to "0.00",
            1.0 to "1.00",
            1.5 to "1.50",
            1.234 to "1.234",
            1.2345 to "1.2345",
            1.23456 to "1.2345",
            100.99 to "100.99",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun customSeparators() {
        val format = testFormat.copy(
            decimalSeparator = ',',
            intGrouping = IntGrouping.Uniform(numDigits = 3, separator = ' '),
        )

        val testCases = listOf(
            1000.0 to "1 000",
            1234.56 to "1 234,56",
            10000.99 to "10 000,99",
            0.5 to "0,5",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun customGroupingChunk() {
        val format = testFormat.copy(
            intGrouping = IntGrouping.Uniform(numDigits = 2, separator = ','),
        )

        val testCases = listOf(
            100.0 to "1,00",
            1000.0 to "10,00",
            10000.0 to "1,00,00",
            123456.78 to "12,34,56.78",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun noGroupingChunk() {
        val format = testFormat.copy(
            intGrouping = IntGrouping.None,
        )

        val testCases = listOf(
            1000.0 to "1000",
            10000.0 to "10000",
            1000000.0 to "1000000",
            1234.56 to "1234.56",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun stringBasicFormatting() {
        val format = testFormat

        val testCases = listOf(
            "0" to "0",
            "1" to "1",
            "10" to "10",
            "100" to "100",
            "1000" to "1,000",
            "10000" to "10,000",
            "100000" to "100,000",
            "1000000" to "1,000,000",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun stringDecimalFormatting() {
        val format = testFormat

        val testCases = listOf(
            "0.5" to "0.5",
            "1.5" to "1.5",
            "10.25" to "10.25",
            "100.99" to "100.99",
            "1000.01" to "1,000.01",
            "1234.56" to "1,234.56",
            "0.1" to "0.1",
            "0.12" to "0.12",
            "0.001" to "0",
            "0.01" to "0.01",
            "0.99" to "0.99",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun stringWithSigns() {
        val format = testFormat.copy(
            positiveSign = "+",
        )

        val testCases = listOf(
            "-1" to "-1",
            "-100" to "-100",
            "-1000" to "-1,000",
            "-1234.56" to "-1,234.56",
            "+1" to "+1",
            "+100" to "+100",
            "+1000" to "+1,000",
            "1" to "+1",
            "100" to "+100",
            // Unicode minus
            "−1" to "-1",
            "−100" to "-100",
            "−1234.56" to "-1,234.56",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun stringHighPrecision() {
        val format = testFormat.copy(numDecimalValuesRange = 0..6)

        val testCases = listOf(
            "12345678.90" to "12,345,678.9",
            "0.123456" to "0.123456",
            "0.1234567890" to "0.123456",
            "999.999999" to "999.999999",
            "0.000001" to "0.000001",
            "0.0000001" to "0",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun stringEdgeCases() {
        val format = testFormat

        val testCases = listOf(
            "" to "",
            "0" to "0",
            "0.0" to "0",
            "0.00" to "0",
            "00123" to "123",
            "00123.45" to "123.45",
            ".5" to "0.5",
            "1." to "1",
        )

        testCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }
    }

    @Test
    fun partsBasicFormatting() {
        val format = testFormat

        assertEquals("0", format.format(intPart = "0"))
        assertEquals("1", format.format(intPart = "1"))
        assertEquals("100", format.format(intPart = "100"))
        assertEquals("1,000", format.format(intPart = "1000"))
        assertEquals("1,234,567", format.format(intPart = "1234567"))
    }

    @Test
    fun partsWithFractional() {
        val format = testFormat

        assertEquals("0.5", format.format(intPart = "0", fracPart = "5"))
        assertEquals("1.5", format.format(intPart = "1", fracPart = "5"))
        assertEquals("1,234.56", format.format(intPart = "1234", fracPart = "56"))
        assertEquals("1,234.56", format.format(intPart = "1234", fracPart = "567"))
        assertEquals("0.12", format.format(intPart = "0", fracPart = "123"))
        assertEquals("100", format.format(intPart = "100", fracPart = "00"))
    }

    @Test
    fun partsWithSign() {
        val format = testFormat

        assertEquals("-1,234.56", format.format(sign = "-", intPart = "1234", fracPart = "56"))
        assertEquals("-100", format.format(sign = "-", intPart = "100"))

        val formatWithPositiveSign = testFormat.copy(positiveSign = "+")
        assertEquals(
            "+1,234.56",
            formatWithPositiveSign.format(sign = "+", intPart = "1234", fracPart = "56")
        )
        assertEquals("+100", formatWithPositiveSign.format(sign = "+", intPart = "100"))
    }

    @Test
    fun partsHighPrecision() {
        val format = testFormat.copy(numDecimalValuesRange = 0..10)

        assertEquals("12,345,678.9", format.format(intPart = "12345678", fracPart = "90"))
        assertEquals("0.123456789", format.format(intPart = "0", fracPart = "1234567890"))
        assertEquals("999.123456789", format.format(intPart = "999", fracPart = "12345678901"))
    }

    @Test
    fun specialValuesAndScientificNotation() {
        val format = testFormat

        // Scientific notation - preserved as-is
        val scientificTestCases = listOf(
            "1.23E10" to "1.23E10",
            "1.23e10" to "1.23e10",
            "-1.23E-5" to "-1.23E-5",
            "5.0E8" to "5.0E8",
        )

        scientificTestCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }

        // Special values - preserved as-is
        val specialTestCases = listOf(
            "Infinity" to "Infinity",
            "-Infinity" to "-Infinity",
            "NaN" to "NaN",
        )

        specialTestCases.forEach { (amount, expected) ->
            assertEquals(expected, format.format(amount), "Failed for amount: $amount")
        }

        // Double.toString() produces these
        assertEquals("Infinity", format.format(Double.POSITIVE_INFINITY))
        assertEquals("-Infinity", format.format(Double.NEGATIVE_INFINITY))
        assertEquals("NaN", format.format(Double.NaN))
    }
}
