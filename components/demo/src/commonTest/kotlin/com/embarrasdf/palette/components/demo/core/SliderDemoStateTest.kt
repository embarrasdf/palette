package com.embarrasdf.palette.components.demo.core

import com.embarrasdf.palette.components.demo.state.demoStateSaver
import com.embarrasdf.palette.components.demo.util.saveAndRestore
import kotlin.test.Test
import kotlin.test.assertEquals

class SliderDemoStateTest {

    @Test
    fun restoresEveryProperty() {
        val state = SliderDemoState().apply {
            sliderType = SliderType.Range
            value = 0.1f
            rangeStart = 0.2f
            rangeEnd = 0.9f
            steps = 4
            enabled = false
            stepMode = StepMode.Custom
            snapValues = listOf(0f, 0.5f)
        }

        val restored = demoStateSaver { SliderDemoState() }.saveAndRestore(state)

        assertEquals(SliderType.Range, restored.sliderType)
        assertEquals(0.1f, restored.value)
        assertEquals(0.2f, restored.rangeStart)
        assertEquals(0.9f, restored.rangeEnd)
        assertEquals(4, restored.steps)
        assertEquals(false, restored.enabled)
        assertEquals(StepMode.Custom, restored.stepMode)
        assertEquals(listOf(0f, 0.5f), restored.snapValues)
    }
}
