package com.embarrasdf.palette.components.demo.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.core.RangeSlider
import com.embarrasdf.palette.components.core.Slider
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
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
    state: SliderDemoState = rememberSliderDemoState(),
    control: SliderDemoControl = rememberSliderDemoControl(state),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier.fillMaxSize(),
    ) {
        SliderDemo(
            state = state,
            control = control,
        )
    }
}

@Composable
fun DemoScope.SliderDemo(
    modifier: Modifier = Modifier,
    state: SliderDemoState = rememberSliderDemoState(),
    control: SliderDemoControl = rememberSliderDemoControl(state),
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
fun rememberSliderDemoState(): SliderDemoState = rememberSaveable(
    saver = SliderDemoStateSaver,
) { SliderDemoState() }

@Stable
class SliderDemoState(
    sliderTypeInitial: SliderType = SliderType.Continuous,
    valueInitial: Float = 0.5f,
    rangeStartInitial: Float = 0.25f,
    rangeEndInitial: Float = 0.75f,
    stepsInitial: Int = 0,
    enabledInitial: Boolean = true,
    stepModeInitial: StepMode = StepMode.Uniform,
    snapValuesInitial: List<Float> = listOf(0f, 0.25f, 0.5f, 0.75f, 1f),
) {
    var sliderType by mutableStateOf(sliderTypeInitial)
        internal set
    var value by mutableFloatStateOf(valueInitial)
        internal set
    var rangeStart by mutableFloatStateOf(rangeStartInitial)
        internal set
    var rangeEnd by mutableFloatStateOf(rangeEndInitial)
        internal set
    var steps by mutableIntStateOf(stepsInitial)
        internal set
    var enabled by mutableStateOf(enabledInitial)
        internal set
    var stepMode by mutableStateOf(stepModeInitial)
        internal set
    var snapValues by mutableStateOf(snapValuesInitial)
        internal set
}

private const val sliderTypeKey = "sliderType"
private const val valueKey = "value"
private const val rangeStartKey = "rangeStart"
private const val rangeEndKey = "rangeEnd"
private const val stepsKey = "steps"
private const val enabledKey = "enabled"
private const val stepModeKey = "stepMode"
private const val snapValuesKey = "snapValues"

val SliderDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            sliderTypeKey to value.sliderType.name,
            valueKey to value.value,
            rangeStartKey to value.rangeStart,
            rangeEndKey to value.rangeEnd,
            stepsKey to value.steps,
            enabledKey to value.enabled,
            stepModeKey to value.stepMode.name,
            snapValuesKey to value.snapValues.joinToString(","),
        )
    },
    restore = { map ->
        SliderDemoState(
            sliderTypeInitial = runCatching {
                SliderType.valueOf(map[sliderTypeKey] as String)
            }.getOrDefault(SliderType.Continuous),
            valueInitial = map[valueKey] as Float,
            rangeStartInitial = map[rangeStartKey] as Float,
            rangeEndInitial = map[rangeEndKey] as Float,
            stepsInitial = map[stepsKey] as Int,
            enabledInitial = map[enabledKey] as Boolean,
            stepModeInitial = runCatching {
                StepMode.valueOf(map[stepModeKey] as String)
            }.getOrDefault(StepMode.Uniform),
            snapValuesInitial = runCatching {
                (map[snapValuesKey] as String)
                    .split(",")
                    .map { it.toFloat() }
            }.getOrDefault(listOf(0f, 0.25f, 0.5f, 0.75f, 1f)),
        )
    },
)

@Composable
fun rememberSliderDemoControl(
    state: SliderDemoState,
): SliderDemoControl = remember(state) { SliderDemoControl(state) }

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
