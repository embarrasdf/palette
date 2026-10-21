package com.embarrasdf.palette.components.demo.state

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DemoStateTest {

    private enum class Mode { First, Second }

    private class ChildState : DemoState() {
        var amount by saveable(1f)
    }

    private class TestState : DemoState() {
        var float by saveable(0.5f)
        var int by saveable(1)
        var boolean by saveable(false)
        var string by saveable("a")
        var dp: Dp by saveable(4.dp)
        var color by saveable(Color.Red)
        var mode by saveable(Mode.First)
        var floats by saveable(listOf(0f, 1f), DemoSavers.FloatList)
        var drawStyle: DrawStyle by saveable(Fill, DemoSavers.DrawStyle)
        val text by textField("hello")
        val linkedText: TextFieldState = text
        val child by child(ChildState())
        var notSaved = 0
    }

    // Accepts what an Android Bundle can hold, so a value the platform can't save fails here too.
    private val scope = SaverScope { it.isPlatformSaveable() }

    private fun Any?.isPlatformSaveable(): Boolean = when (this) {
        null, is Boolean, is Number, is String, is Char, is Enum<*> -> true
        is List<*> -> all { it.isPlatformSaveable() }
        else -> false
    }

    private val saver = demoStateSaver { TestState() }

    private fun TestState.saveAndRestore(): TestState = saver.restore(
        with(saver) {
            scope.save(this@saveAndRestore)
        }!!,
    )!!

    @Test
    fun restoresEveryDeclaredProperty() {
        val state = TestState().apply {
            float = 0.25f
            int = 7
            boolean = true
            string = "b"
            dp = 12.dp
            color = Color.Blue
            mode = Mode.Second
            floats = listOf(0.1f, 0.2f, 0.3f)
            drawStyle = Stroke(width = 3f)
            text.edit { replace(0, length, "world") }
            child.amount = 0.75f
            notSaved = 9
        }

        val restored = state.saveAndRestore()

        assertEquals(0.25f, restored.float)
        assertEquals(7, restored.int)
        assertEquals(true, restored.boolean)
        assertEquals("b", restored.string)
        assertEquals(12.dp, restored.dp)
        assertEquals(Color.Blue, restored.color)
        assertEquals(Mode.Second, restored.mode)
        assertEquals(listOf(0.1f, 0.2f, 0.3f), restored.floats)
        assertEquals(Stroke(width = 3f), restored.drawStyle)
        assertEquals("world", restored.text.text.toString())
        assertEquals(0.75f, restored.child.amount)
        assertEquals(0, restored.notSaved)
    }

    @Test
    fun savedValueIsPlatformSaveable() {
        val saved = with(saver) { scope.save(TestState()) }

        assertTrue(saved.isPlatformSaveable())
    }

    @Test
    fun textFieldIsRestoredInPlace() {
        val state = TestState().apply {
            text.edit {
                replace(0, length, "world")
                selection = TextRange(2)
            }
        }

        val restored = state.saveAndRestore()

        assertSame(restored.linkedText, restored.text)
        assertEquals("world", restored.linkedText.text.toString())
        assertEquals(TextRange(2), restored.text.selection)
    }

    @Test
    fun missingOrUnreadableValuesKeepTheirDefaults() {
        val restored = saver.restore(
            listOf(
                "float",
                0.25f,
                "int",
                "not an int",
                "mode",
                "RemovedMode",
            ),
        )!!

        assertEquals(0.25f, restored.float)
        assertEquals(1, restored.int)
        assertEquals(Mode.First, restored.mode)
        assertEquals("a", restored.string)
    }

    @Test
    fun customSaverReturningNullKeepsTheDefault() {
        class NullableState : DemoState() {
            var value by saveable(3, Saver(save = { it }, restore = { null }))
        }
        val nullableSaver = demoStateSaver { NullableState() }
        val state = NullableState().apply { value = 5 }

        val restored = nullableSaver.restore(with(nullableSaver) { scope.save(state) }!!)!!

        assertEquals(3, restored.value)
    }
}
