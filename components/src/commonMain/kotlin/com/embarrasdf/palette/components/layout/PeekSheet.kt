package com.embarrasdf.palette.components.layout

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.util.Spacer
import com.alexrdclement.trace.trace
import kotlin.math.max

private const val TraceName = "PeekSheet"

enum class PeekSheetAnchor {
    Peek,
    Expanded,
}

@Stable
class PeekSheetState(
    val initialValue: PeekSheetAnchor,
) {
    internal var anchoredDraggableState = AnchoredDraggableState(
        initialValue = initialValue,
        confirmValueChange = {
            when (it) {
                PeekSheetAnchor.Peek,
                PeekSheetAnchor.Expanded -> true
            }
        },
    )

    suspend fun expand() {
        anchoredDraggableState.animateTo(PeekSheetAnchor.Expanded)
    }

    suspend fun peek() {
        anchoredDraggableState.animateTo(PeekSheetAnchor.Peek)
    }

    internal suspend fun animateTo(targetValue: PeekSheetAnchor) {
        anchoredDraggableState.animateTo(targetValue)
    }

    internal suspend fun snapTo(targetValue: PeekSheetAnchor) {
        anchoredDraggableState.snapTo(targetValue)
    }

    internal suspend fun settle(animationSpec: AnimationSpec<Float>) {
        anchoredDraggableState.settle(animationSpec)
    }

    val offset: Float get() = anchoredDraggableState.offset

    val partialToFullProgress: Float get() {
        return anchoredDraggableState.progress(
            PeekSheetAnchor.Peek,
            PeekSheetAnchor.Expanded,
        )
    }

    val currentValue: PeekSheetAnchor get() = anchoredDraggableState.currentValue
    val targetValue: PeekSheetAnchor get() = anchoredDraggableState.targetValue

    val isExpanded: Boolean get() = targetValue == PeekSheetAnchor.Expanded

    internal val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            // Lock content scroll while sheet is mid-drag; move sheet back toward expanded instead
            if (available.y < 0f && partialToFullProgress < 1f) {
                val delta = anchoredDraggableState.dispatchRawDelta(available.y)
                return Offset(0f, delta)
            }
            return Offset.Zero
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            if (available.y <= 0f) return Offset.Zero

            return if (source == NestedScrollSource.UserInput || partialToFullProgress < 1f) {
                // Allow deliberate drags and continuations of in-progress sheet drag
                val delta = anchoredDraggableState.dispatchRawDelta(available.y)
                Offset(0f, delta)
            } else {
                // Absorb fling momentum that hits the top boundary without having
                // moved the sheet yet (i.e. accidental overscroll from list scrolling)
                Offset(0f, available.y)
            }
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            // Snap to nearest anchor; absorb any remaining downward velocity
            anchoredDraggableState.settle(spring())
            return if (available.y > 0f) available else Velocity.Zero
        }
    }

    companion object {
        fun Saver(): Saver<PeekSheetState, PeekSheetAnchor> = Saver(
            save = { it.currentValue },
            restore = { PeekSheetState(initialValue = it) }
        )
    }
}

@Composable
fun rememberPeekSheetState(
    initialValue: PeekSheetAnchor = PeekSheetAnchor.Peek,
): PeekSheetState {
    return rememberSaveable(saver = PeekSheetState.Saver()) {
        PeekSheetState(initialValue = initialValue)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PeekSheet(
    peekHeight: Dp,
    modifier: Modifier = Modifier,
    state: PeekSheetState = rememberPeekSheetState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    above: @Composable () -> Unit = {},
    bar: @Composable (progress: () -> Float) -> Unit,
    content: @Composable () -> Unit = {},
) {
    trace(TraceName) {
        BoxWithConstraints(modifier = modifier) {
            val peekHeightPx = with(LocalDensity.current) { peekHeight.toPx() }
            val expandedHeightPx = constraints.maxHeight

            Column(
                modifier = Modifier
                    .nestedScroll(state.nestedScrollConnection)
                    .offset { IntOffset(0, state.offset.toInt()) }
                    .anchoredDraggable(
                        state = state.anchoredDraggableState,
                        orientation = Orientation.Vertical,
                    )
                    .modalBottomSheetAnchors(
                        state = state,
                        minHeight = peekHeightPx,
                        fullHeight = expandedHeightPx.toFloat(),
                    )
            ) {
                Spacer(height = contentPadding.calculateTopPadding())
                above()
                bar { state.partialToFullProgress }
                content()
                Spacer(height = contentPadding.calculateBottomPadding())
            }
        }
    }
}

@ExperimentalFoundationApi
private fun Modifier.modalBottomSheetAnchors(
    state: PeekSheetState,
    minHeight: Float,
    fullHeight: Float,
) = onSizeChanged { size ->
    val newAnchors = DraggableAnchors {
        if (size.height > minHeight) {
            PeekSheetAnchor.Peek at fullHeight - minHeight
        }
        if (size.height != 0) {
            PeekSheetAnchor.Expanded at max(0f, fullHeight - size.height)
        }
    }
    state.anchoredDraggableState.updateAnchors(newAnchors, state.targetValue)
}
