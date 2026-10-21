package com.embarrasdf.palette.components.demo.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.core.RangeSlider
import com.embarrasdf.palette.components.core.Slider
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.state.DemoSavers
import com.embarrasdf.palette.components.demo.state.DemoState
import com.embarrasdf.palette.components.demo.state.rememberDemoState
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

enum class SliderType {
    Continuous,
    Range,
}

enum class StepMode {
    Uniform,
    Custom,
}

@Composable
fun SliderDemo(
    modifier: Modifier = Modifier,
    state: SliderDemoState = rememberSliderDemoState(),
    control: SliderDemoControl = rememberSliderDemoControl(state),
) {
    Demo(
        controls = control.controls,
        modifier = modifier.fillMaxSize(),
    ) {
        SliderDemo(
            state = state,
        )
    }
}

@Composable
fun DemoScope.SliderDemo(
    modifier: Modifier = Modifier,
    state: SliderDemoState = rememberSliderDemoState(),
) {
    val sliderModifier = modifier
        .fillMaxWidth()
        .align(Alignment.Center)
        .padding(PaletteTheme.semantic.dimension.spacing.medium)

    when (state.sliderType) {
        SliderType.Continuous -> when (state.stepMode) {
            StepMode.Uniform -> Slider(
                style = PaletteTheme.component.core.slider,
                value = state.value,
                onValueChange = { state.value = it },
                enabled = state.enabled,
                steps = state.steps,
                modifier = sliderModifier,
            )
            StepMode.Custom -> Slider(
                style = PaletteTheme.component.core.slider,
                value = state.value,
                onValueChange = { state.value = it },
                enabled = state.enabled,
                snapValues = state.snapValues,
                modifier = sliderModifier,
            )
        }
        SliderType.Range -> when (state.stepMode) {
            StepMode.Uniform -> RangeSlider(
                style = PaletteTheme.component.core.slider,
                value = state.rangeStart..state.rangeEnd,
                onValueChange = {
                    state.rangeStart = it.start
                    state.rangeEnd = it.endInclusive
                },
                enabled = state.enabled,
                steps = state.steps,
                modifier = sliderModifier,
            )
            StepMode.Custom -> RangeSlider(
                style = PaletteTheme.component.core.slider,
                value = state.rangeStart..state.rangeEnd,
                onValueChange = {
                    state.rangeStart = it.start
                    state.rangeEnd = it.endInclusive
                },
                enabled = state.enabled,
                snapValues = state.snapValues,
                modifier = sliderModifier,
            )
        }
    }
}

@Composable
fun rememberSliderDemoState(): SliderDemoState = rememberDemoState { SliderDemoState() }

private val DefaultSnapValues = listOf(0f, 0.25f, 0.5f, 0.75f, 1f)

@Stable
class SliderDemoState : DemoState() {
    var sliderType by saveable(SliderType.Continuous)
        internal set
    var value by saveable(0.5f)
        internal set
    var rangeStart by saveable(0.25f)
        internal set
    var rangeEnd by saveable(0.75f)
        internal set
    var steps by saveable(0)
        internal set
    var enabled by saveable(true)
        internal set
    var stepMode by saveable(StepMode.Uniform)
        internal set
    var snapValues by saveable(initial = DefaultSnapValues, saver = DemoSavers.FloatList)
        internal set
}

@Composable
fun rememberSliderDemoControl(state: SliderDemoState): SliderDemoControl = remember(state) { SliderDemoControl(state) }

@Stable
class SliderDemoControl(
    val state: SliderDemoState,
) {
    val typeControl = enumControl(
        name = "Type",
        values = { SliderType.entries },
        selectedValue = { state.sliderType },
        onValueChange = { state.sliderType = it },
    )

    val enabledControl = Control.Toggle(
        name = "Enabled",
        value = { state.enabled },
        onValueChange = { state.enabled = it },
    )

    val stepModeControl = enumControl(
        name = "Step Mode",
        values = { StepMode.entries },
        selectedValue = { state.stepMode },
        onValueChange = { state.stepMode = it },
    )

    val uniformStepsControl = Control.Slider(
        name = "Steps",
        value = { state.steps.toFloat() },
        onValueChange = { state.steps = it.roundToInt() },
        valueRange = { 0f..9f },
        stepIncrement = 1f,
    )

    val customStepsControl = Control.DynamicList(
        name = "Steps",
        items = { state.snapValues },
        onItemsChange = { state.snapValues = it },
        newItemDefault = { 0.5f },
        createControl = { item, onChange ->
            Control.Slider(
                name = "Value",
                value = { item },
                onValueChange = onChange,
            )
        },
        addButtonText = "Add Point",
    )

    val stepControl = Control.ControlColumn(
        name = "Steps",
        controls = {
            buildList {
                add(stepModeControl)
                when (state.stepMode) {
                    StepMode.Uniform -> add(uniformStepsControl)
                    StepMode.Custom -> add(customStepsControl)
                }
            }.toPersistentList()
        },
        indent = true,
    )

    val controls
        get() = buildList {
            add(typeControl)
            add(enabledControl)
            add(stepControl)
        }.toPersistentList()
}
