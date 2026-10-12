package com.embarrasdf.palette.components.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

// Adapted from Material3's Slider. Does not support RTL layout.

data class SliderStyle(
    val colors: SliderColors = SliderColors(),
)

@Composable
fun Slider(
    value: Float,
    onValueChange: ((Float) -> Unit)?,
    stepIncrement: Float,
    modifier: Modifier = Modifier,
    style: SliderStyle = SliderStyle(),
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = stepsForIncrement(valueRange = valueRange, stepIncrement = stepIncrement),
        onValueChangeFinished = onValueChangeFinished,
        style = style,
        interactionSource = interactionSource,
    )
}

@Composable
fun Slider(
    value: Float,
    onValueChange: ((Float) -> Unit)?,
    modifier: Modifier = Modifier,
    style: SliderStyle = SliderStyle(),
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val onValueChangeFinishedState = rememberUpdatedState(onValueChangeFinished)
    val state = remember(valueRange, steps) {
        SliderState(
            value = value,
            onValueChangeFinished = { onValueChangeFinishedState.value?.invoke() },
            valueRange = valueRange,
            steps = steps,
        )
    }

    state.onValueChange = onValueChange
    state.value = value

    val currentOnValueChange by rememberUpdatedState(onValueChange)
    DisposableEffect(state) {
        val snapped = snapValueToTick(
            current = value,
            tickFractions = state.tickFractions,
            valueRange = valueRange,
        )
        if (snapped != value) {
            currentOnValueChange?.invoke(snapped)
        }
        onDispose {}
    }

    Slider(
        state = state,
        modifier = modifier,
        enabled = enabled,
        style = style,
        interactionSource = interactionSource,
    )
}

@Composable
fun Slider(
    value: Float,
    onValueChange: ((Float) -> Unit)?,
    snapValues: List<Float>,
    modifier: Modifier = Modifier,
    style: SliderStyle = SliderStyle(),
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val onValueChangeFinishedState = rememberUpdatedState(onValueChangeFinished)
    val state = remember(valueRange, snapValues) {
        SliderState(
            value = value,
            onValueChangeFinished = { onValueChangeFinishedState.value?.invoke() },
            valueRange = valueRange,
            snapValues = snapValues,
        )
    }

    state.onValueChange = onValueChange
    state.value = value

    val currentOnValueChange by rememberUpdatedState(onValueChange)
    DisposableEffect(state) {
        val snapped = snapValueToTick(
            current = value,
            tickFractions = state.tickFractions,
            valueRange = valueRange,
        )
        if (snapped != value) {
            currentOnValueChange?.invoke(snapped)
        }
        onDispose {}
    }

    Slider(
        state = state,
        modifier = modifier,
        enabled = enabled,
        style = style,
        interactionSource = interactionSource,
    )
}

@Composable
fun Slider(
    state: SliderState,
    modifier: Modifier = Modifier,
    style: SliderStyle = SliderStyle(),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Layout(
        content = {
            SliderDefaults.Track(tickFractions = state.tickFractions, colors = style.colors)
            SliderDefaults.Thumb(colors = style.colors)
        },
        modifier = modifier
            .requiredSizeIn(
                minWidth = SliderDefaults.ThumbSize,
                minHeight = SliderDefaults.ThumbSize,
            )
            .sliderSemantics(state, enabled)
            .focusable(enabled, interactionSource)
            .then(
                if (enabled) {
                    Modifier.pointerInput(state, interactionSource) {
                        detectTapGestures(
                            onPress = { state.onPress(it) },
                            onTap = {
                                state.dispatchRawDelta(0f)
                                state.gestureEndAction()
                            },
                        )
                    }
                } else {
                    Modifier
                },
            )
            .draggable(
                orientation = Orientation.Horizontal,
                enabled = enabled,
                interactionSource = interactionSource,
                onDragStopped = { state.gestureEndAction() },
                startDragImmediately = state.isDragging,
                state = state,
            ),
    ) { measurables, constraints ->

        val trackPlaceable = measurables[0].measure(constraints)
        val thumbPlaceable = measurables[1].measure(constraints)

        val sliderWidth = thumbPlaceable.width + trackPlaceable.width
        val sliderHeight = max(trackPlaceable.height, thumbPlaceable.height)

        state.updateDimensions(
            newThumbWidth = thumbPlaceable.width.toFloat(),
            newTotalWidth = sliderWidth,
        )

        val trackOffsetX = thumbPlaceable.width / 2
        val thumbOffsetX = ((trackPlaceable.width) * state.coercedValueAsFraction).roundToInt()
        val trackOffsetY = (sliderHeight - trackPlaceable.height) / 2
        val thumbOffsetY = (sliderHeight - thumbPlaceable.height) / 2

        layout(sliderWidth, sliderHeight) {
            trackPlaceable.placeRelative(
                trackOffsetX,
                trackOffsetY,
            )
            thumbPlaceable.placeRelative(
                thumbOffsetX,
                thumbOffsetY,
            )
        }
    }
}

@Composable
fun RangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: ((ClosedFloatingPointRange<Float>) -> Unit)?,
    stepIncrement: Float,
    modifier: Modifier = Modifier,
    style: SliderStyle = SliderStyle(),
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    RangeSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = stepsForIncrement(valueRange = valueRange, stepIncrement = stepIncrement),
        onValueChangeFinished = onValueChangeFinished,
        style = style,
    )
}

@Composable
fun RangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: ((ClosedFloatingPointRange<Float>) -> Unit)?,
    modifier: Modifier = Modifier,
    style: SliderStyle = SliderStyle(),
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val onValueChangeFinishedState = rememberUpdatedState(onValueChangeFinished)
    val state = remember(valueRange, steps) {
        RangeSliderState(
            activeRange = value,
            onValueChangeFinished = { onValueChangeFinishedState.value?.invoke() },
            valueRange = valueRange,
            steps = steps,
        )
    }
    state.onValueChange = onValueChange
    state.activeRangeStart = value.start
    state.activeRangeEnd = value.endInclusive

    val currentOnValueChange by rememberUpdatedState(onValueChange)
    DisposableEffect(state) {
        val snappedStart = snapValueToTick(
            current = value.start,
            tickFractions = state.tickFractions,
            valueRange = valueRange,
        )
        val snappedEnd = snapValueToTick(
            current = value.endInclusive,
            tickFractions = state.tickFractions,
            valueRange = valueRange,
        )
        if (snappedStart != value.start || snappedEnd != value.endInclusive) {
            currentOnValueChange?.invoke(snappedStart..snappedEnd)
        }
        onDispose {}
    }

    RangeSlider(
        state = state,
        modifier = modifier,
        enabled = enabled,
        style = style,
    )
}

@Composable
fun RangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: ((ClosedFloatingPointRange<Float>) -> Unit)?,
    snapValues: List<Float>,
    modifier: Modifier = Modifier,
    style: SliderStyle = SliderStyle(),
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val onValueChangeFinishedState = rememberUpdatedState(onValueChangeFinished)
    val state = remember(valueRange, snapValues) {
        RangeSliderState(
            activeRange = value,
            onValueChangeFinished = { onValueChangeFinishedState.value?.invoke() },
            valueRange = valueRange,
            snapValues = snapValues,
        )
    }
    state.onValueChange = onValueChange
    state.activeRangeStart = value.start
    state.activeRangeEnd = value.endInclusive

    val currentOnValueChange by rememberUpdatedState(onValueChange)
    DisposableEffect(state) {
        val snappedStart = snapValueToTick(
            current = value.start,
            tickFractions = state.tickFractions,
            valueRange = valueRange,
        )
        val snappedEnd = snapValueToTick(
            current = value.endInclusive,
            tickFractions = state.tickFractions,
            valueRange = valueRange,
        )
        if (snappedStart != value.start || snappedEnd != value.endInclusive) {
            currentOnValueChange?.invoke(snappedStart..snappedEnd)
        }
        onDispose {}
    }

    RangeSlider(
        state = state,
        modifier = modifier,
        enabled = enabled,
        style = style,
    )
}

@Composable
fun RangeSlider(
    state: RangeSliderState,
    modifier: Modifier = Modifier,
    style: SliderStyle = SliderStyle(),
    enabled: Boolean = true,
) {
    Layout(
        content = {
            SliderDefaults.Track(tickFractions = state.tickFractions, colors = style.colors)
            Box(
                modifier = Modifier.then(
                    if (enabled) {
                        Modifier.draggable(
                            orientation = Orientation.Horizontal,
                            onDragStarted = { state.initStartDrag() },
                            onDragStopped = { state.gestureEndAction() },
                            startDragImmediately = state.isDraggingStart,
                            state = state.startDraggableState,
                        )
                    } else {
                        Modifier
                    },
                ),
            ) {
                SliderDefaults.Thumb(colors = style.colors)
            }
            Box(
                modifier = Modifier.then(
                    if (enabled) {
                        Modifier.draggable(
                            orientation = Orientation.Horizontal,
                            onDragStarted = { state.initEndDrag() },
                            onDragStopped = { state.gestureEndAction() },
                            startDragImmediately = state.isDraggingEnd,
                            state = state.endDraggableState,
                        )
                    } else {
                        Modifier
                    },
                ),
            ) {
                SliderDefaults.Thumb(colors = style.colors)
            }
        },
        modifier = modifier
            .requiredSizeIn(
                minWidth = SliderDefaults.ThumbSize * 2,
                minHeight = SliderDefaults.ThumbSize,
            ),
    ) { measurables, constraints ->
        val thumbConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val trackPlaceable = measurables[0].measure(constraints)
        val startThumbPlaceable = measurables[1].measure(thumbConstraints)
        val endThumbPlaceable = measurables[2].measure(thumbConstraints)

        val sliderWidth = startThumbPlaceable.width + trackPlaceable.width
        val sliderHeight = maxOf(
            trackPlaceable.height,
            startThumbPlaceable.height,
            endThumbPlaceable.height,
        )

        state.updateDimensions(
            newThumbWidth = startThumbPlaceable.width.toFloat(),
            newTotalWidth = sliderWidth,
        )

        val trackOffsetX = startThumbPlaceable.width / 2
        val trackOffsetY = (sliderHeight - trackPlaceable.height) / 2
        val startThumbOffsetX = (trackPlaceable.width * state.coercedActiveRangeStartAsFraction).roundToInt()
        val endThumbOffsetX = (trackPlaceable.width * state.coercedActiveRangeEndAsFraction).roundToInt()
        val thumbOffsetY = (sliderHeight - startThumbPlaceable.height) / 2

        layout(sliderWidth, sliderHeight) {
            trackPlaceable.placeRelative(trackOffsetX, trackOffsetY)
            startThumbPlaceable.placeRelative(startThumbOffsetX, thumbOffsetY)
            endThumbPlaceable.placeRelative(endThumbOffsetX, thumbOffsetY)
        }
    }
}

private fun Modifier.sliderSemantics(
    state: SliderState,
    enabled: Boolean,
) = semantics {
    if (!enabled) disabled()
    setProgress { targetValue ->
        val resolvedValue = snapValueToTick(
            current = targetValue.coerceIn(state.valueRange.start, state.valueRange.endInclusive),
            tickFractions = state.tickFractions,
            valueRange = state.valueRange,
        )

        if (resolvedValue == state.value) {
            return@setProgress false
        }

        if (state.onValueChange != null) {
            state.onValueChange?.let {
                it(resolvedValue)
            }
        } else {
            state.value = resolvedValue
        }
        state.onValueChangeFinished?.invoke()

        true
    }
}.progressSemantics(
    value = state.value,
    valueRange = state.valueRange.start..state.valueRange.endInclusive,
    steps = state.steps,
)

data class SliderColors(
    val trackColor: Color = Color.Unspecified,
    val thumbColor: Color = Color.Unspecified,
    val thumbPointColor: Color = Color.Unspecified,
    val thumbBackgroundColor: Color = Color.Unspecified,
)

object SliderDefaults {
    val TrackHeight = 1.dp
    val TickSize = 2.dp

    val ThumbSize = 20.dp
    val ThumbBorderWidth = 1.dp
    val ThumbPointSizeDp = 4.dp
    val ThumbPointSize: Size
        @Composable
        @ReadOnlyComposable
        get() = with(LocalDensity.current) {
            Size(ThumbPointSizeDp.toPx(), ThumbPointSizeDp.toPx())
        }

    @Composable
    fun Track(
        modifier: Modifier = Modifier,
        tickFractions: FloatArray = floatArrayOf(),
        colors: SliderColors = SliderColors(),
    ) {
        val brush = remember(colors.trackColor) { SolidColor(colors.trackColor) }
        val tickRadius = with(LocalDensity.current) { TickSize.toPx() }
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(TrackHeight),
        ) {
            drawLine(
                brush = brush,
                start = Offset(0f, center.y),
                end = Offset(this.size.width, center.y),
                strokeWidth = TrackHeight.toPx(),
            )
            for (fraction in tickFractions) {
                drawCircle(
                    brush = brush,
                    radius = tickRadius,
                    center = Offset(this.size.width * fraction, center.y),
                )
            }
        }
    }

    @Composable
    fun Thumb(
        modifier: Modifier = Modifier,
        colors: SliderColors = SliderColors(),
    ) {
        val borderStroke = remember(colors.thumbColor) {
            BorderStroke(width = ThumbBorderWidth, color = colors.thumbColor)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(ThumbSize)
                .border(borderStroke),
        ) {
            val pointBrush = remember(colors.thumbPointColor) { SolidColor(colors.thumbPointColor) }
            val pointSize = ThumbPointSize
            val backgroundBrush = remember(colors.thumbBackgroundColor) { SolidColor(colors.thumbBackgroundColor) }
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    brush = backgroundBrush,
                    topLeft = Offset(center.x, 0f),
                    size = Size(this.size.width / 2, this.size.height),
                )
                drawRect(
                    brush = pointBrush,
                    topLeft = Offset(center.x - pointSize.width / 2, center.y - pointSize.height / 2),
                    size = pointSize,
                )
            }
        }
    }
}

class SliderState(
    value: Float = 0f,
    var onValueChange: ((Float) -> Unit)? = null,
    val onValueChangeFinished: (() -> Unit)? = null,
    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    val steps: Int = 0,
    snapValues: List<Float>? = null,
) : DraggableState {

    internal val tickFractions: FloatArray = snapValues
        ?.let { snapValuesToTickFractions(snapValues = it, valueRange = valueRange) }
        ?: stepsToTickFractions(steps)

    private var valueState by mutableFloatStateOf(value)

    var value: Float
        set(newVal) {
            valueState = newVal.coerceIn(valueRange.start, valueRange.endInclusive)
        }
        get() = valueState

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit,
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(receiver = dragScope, priority = dragPriority, block = block)
        isDragging = false
    }

    override fun dispatchRawDelta(delta: Float) {
        val maxPx = max(totalWidth - thumbWidth / 2, 0f)
        val minPx = min(thumbWidth / 2, maxPx)
        rawOffset = (rawOffset + delta + pressOffset)
        pressOffset = 0f
        val offsetInTrack = rawOffset
        val rawValue = scaleToUserValue(minPx = minPx, maxPx = maxPx, offset = offsetInTrack)
        val scaledUserValue = snapValueToTick(
            current = rawValue,
            tickFractions = tickFractions,
            valueRange = valueRange,
        )
        if (scaledUserValue != this.value) {
            if (onValueChange != null) {
                onValueChange?.let { it(scaledUserValue) }
            } else {
                this.value = scaledUserValue
            }
        }
    }

    private var totalWidth by mutableIntStateOf(0)
    private var thumbWidth by mutableFloatStateOf(0f)

    internal val coercedValueAsFraction
        get() = calcFraction(
            a = valueRange.start,
            b = valueRange.endInclusive,
            pos = value.coerceIn(valueRange.start, valueRange.endInclusive),
        )

    internal var isDragging by mutableStateOf(false)
        private set

    internal fun updateDimensions(
        newThumbWidth: Float,
        newTotalWidth: Int,
    ) {
        thumbWidth = newThumbWidth
        totalWidth = newTotalWidth
    }

    internal val gestureEndAction = {
        if (!isDragging) {
            // check isDragging in case the change is still in progress (touch -> drag case)
            onValueChangeFinished?.invoke()
        }
    }

    internal fun onPress(pos: Offset) {
        pressOffset = pos.x - rawOffset
    }

    private var rawOffset by mutableFloatStateOf(scaleToOffset(minPx = 0f, maxPx = 0f, userValue = value))
    private var pressOffset by mutableFloatStateOf(0f)
    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = dispatchRawDelta(pixels)
    }

    private val scrollMutex = MutatorMutex()

    private fun scaleToUserValue(
        minPx: Float,
        maxPx: Float,
        offset: Float,
    ) = scale(
        a1 = minPx,
        b1 = maxPx,
        x1 = offset,
        a2 = valueRange.start,
        b2 = valueRange.endInclusive,
    )

    private fun scaleToOffset(
        minPx: Float,
        maxPx: Float,
        userValue: Float,
    ) = scale(
        a1 = valueRange.start,
        b1 = valueRange.endInclusive,
        x1 = userValue,
        a2 = minPx,
        b2 = maxPx,
    )
}

class RangeSliderState(
    activeRange: ClosedFloatingPointRange<Float> = 0f..1f,
    var onValueChange: ((ClosedFloatingPointRange<Float>) -> Unit)? = null,
    val onValueChangeFinished: (() -> Unit)? = null,
    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    val steps: Int = 0,
    snapValues: List<Float>? = null,
) {
    internal val tickFractions: FloatArray = snapValues
        ?.let { snapValuesToTickFractions(snapValues = it, valueRange = valueRange) }
        ?: stepsToTickFractions(steps)

    private var activeRangeStartState by mutableFloatStateOf(
        activeRange.start.coerceIn(valueRange.start, valueRange.endInclusive),
    )
    private var activeRangeEndState by mutableFloatStateOf(
        activeRange.endInclusive.coerceIn(valueRange.start, valueRange.endInclusive),
    )

    var activeRangeStart: Float
        get() = activeRangeStartState
        set(newVal) {
            activeRangeStartState = newVal.coerceIn(valueRange.start, activeRangeEnd)
        }

    var activeRangeEnd: Float
        get() = activeRangeEndState
        set(newVal) {
            activeRangeEndState = newVal.coerceIn(activeRangeStart, valueRange.endInclusive)
        }

    internal val coercedActiveRangeStartAsFraction
        get() = calcFraction(a = valueRange.start, b = valueRange.endInclusive, pos = activeRangeStart)

    internal val coercedActiveRangeEndAsFraction
        get() = calcFraction(a = valueRange.start, b = valueRange.endInclusive, pos = activeRangeEnd)

    internal val gestureEndAction = {
        onValueChangeFinished?.invoke()
    }

    private var totalWidth by mutableIntStateOf(0)
    private var thumbWidth by mutableFloatStateOf(0f)

    internal fun updateDimensions(
        newThumbWidth: Float,
        newTotalWidth: Int,
    ) {
        thumbWidth = newThumbWidth
        totalWidth = newTotalWidth
    }

    internal var isDraggingStart by mutableStateOf(false)
        private set
    internal var isDraggingEnd by mutableStateOf(false)
        private set

    private var rawOffsetStart by mutableFloatStateOf(0f)
    private var rawOffsetEnd by mutableFloatStateOf(0f)

    internal fun initStartDrag() {
        val maxPx = max(totalWidth - thumbWidth / 2, 0f)
        val minPx = min(thumbWidth / 2, maxPx)
        rawOffsetStart = scale(
            a1 = valueRange.start,
            b1 = valueRange.endInclusive,
            x1 = activeRangeStart,
            a2 = minPx,
            b2 = maxPx,
        )
    }

    internal fun initEndDrag() {
        val maxPx = max(totalWidth - thumbWidth / 2, 0f)
        val minPx = min(thumbWidth / 2, maxPx)
        rawOffsetEnd = scale(
            a1 = valueRange.start,
            b1 = valueRange.endInclusive,
            x1 = activeRangeEnd,
            a2 = minPx,
            b2 = maxPx,
        )
    }

    private val startDragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float) = dispatchStartDelta(pixels)
    }
    private val startScrollMutex = MutatorMutex()

    internal val startDraggableState: DraggableState = object : DraggableState {
        override suspend fun drag(
            dragPriority: MutatePriority,
            block: suspend DragScope.() -> Unit,
        ): Unit = coroutineScope {
            isDraggingStart = true
            startScrollMutex.mutateWith(receiver = startDragScope, priority = dragPriority, block = block)
            isDraggingStart = false
        }

        override fun dispatchRawDelta(delta: Float) = dispatchStartDelta(delta)
    }

    private val endDragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float) = dispatchEndDelta(pixels)
    }
    private val endScrollMutex = MutatorMutex()

    internal val endDraggableState: DraggableState = object : DraggableState {
        override suspend fun drag(
            dragPriority: MutatePriority,
            block: suspend DragScope.() -> Unit,
        ): Unit = coroutineScope {
            isDraggingEnd = true
            endScrollMutex.mutateWith(receiver = endDragScope, priority = dragPriority, block = block)
            isDraggingEnd = false
        }

        override fun dispatchRawDelta(delta: Float) = dispatchEndDelta(delta)
    }

    private fun dispatchStartDelta(delta: Float) {
        val maxPx = max(totalWidth - thumbWidth / 2, 0f)
        val minPx = min(thumbWidth / 2, maxPx)
        rawOffsetStart = (rawOffsetStart + delta).coerceIn(minPx, rawOffsetEnd)
        val scaledUserValue = snapValueToTick(
            current = scale(
                a1 = minPx,
                b1 = maxPx,
                x1 = rawOffsetStart,
                a2 = valueRange.start,
                b2 = valueRange.endInclusive,
            ),
            tickFractions = tickFractions,
            valueRange = valueRange,
        ).coerceIn(valueRange.start, activeRangeEnd)
        if (scaledUserValue != activeRangeStart) {
            if (onValueChange != null) {
                onValueChange?.invoke(scaledUserValue..activeRangeEnd)
            } else {
                activeRangeStart = scaledUserValue
            }
        }
    }

    private fun dispatchEndDelta(delta: Float) {
        val maxPx = max(totalWidth - thumbWidth / 2, 0f)
        val minPx = min(thumbWidth / 2, maxPx)
        rawOffsetEnd = (rawOffsetEnd + delta).coerceIn(rawOffsetStart, maxPx)
        val scaledUserValue = snapValueToTick(
            current = scale(
                a1 = minPx,
                b1 = maxPx,
                x1 = rawOffsetEnd,
                a2 = valueRange.start,
                b2 = valueRange.endInclusive,
            ),
            tickFractions = tickFractions,
            valueRange = valueRange,
        ).coerceIn(activeRangeStart, valueRange.endInclusive)
        if (scaledUserValue != activeRangeEnd) {
            if (onValueChange != null) {
                onValueChange?.invoke(activeRangeStart..scaledUserValue)
            } else {
                activeRangeEnd = scaledUserValue
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Surface {
        val state = remember { SliderState(value = 0.5f) }
        Slider(state = state)
    }
}
