package com.embarrasdf.palette.components.core

import androidx.compose.foundation.Indication
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.sqrt

fun Modifier.shapeClickable(
    shape: Shape,
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    hapticFeedbackEnabled: Boolean = true,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
): Modifier = this
    .then(
        if (onLongClick != null && onLongClickLabel != null) {
            Modifier.semantics {
                onLongClick(label = onLongClickLabel, action = null)
                role = Role.Button
            }
        } else if (onLongClick != null || enabled) {
            Modifier.semantics {
                role = Role.Button
            }
        } else {
            Modifier
        },
    )
    .indication(interactionSource, indication)
    .clippedPointerEvents(
        shape = shape,
        interactionSource = interactionSource,
        enabled = enabled,
    )
    .clippedTapGestures(
        shape = shape,
        interactionSource = interactionSource,
        enabled = enabled,
        hapticFeedbackEnabled = hapticFeedbackEnabled,
        onClick = onClick,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick,
    )

private fun Modifier.clippedPointerEvents(
    shape: Shape,
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
): Modifier = this then ClippedPointerEventsElement(
    shape = shape,
    interactionSource = interactionSource,
    enabled = enabled,
)

private data class ClippedPointerEventsElement(
    val shape: Shape,
    val interactionSource: MutableInteractionSource,
    val enabled: Boolean,
) : ModifierNodeElement<ClippedPointerEventsNode>() {
    override fun create() = ClippedPointerEventsNode(
        shape = shape,
        interactionSource = interactionSource,
        enabled = enabled,
    )

    override fun update(node: ClippedPointerEventsNode) {
        node.update(shape = shape, interactionSource = interactionSource, enabled = enabled)
    }
}

private class ClippedPointerEventsNode(
    private var shape: Shape,
    private var interactionSource: MutableInteractionSource,
    private var enabled: Boolean,
) : DelegatingNode() {
    private var hoverInteraction: HoverInteraction.Enter? = null

    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            if (!enabled) return@SuspendingPointerInputModifierNode

            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    val change = event.changes.firstOrNull() ?: continue
                    val inShape = isInShape(size = size, offset = change.position, shape = shape)

                    hoverInteraction = coroutineScope.updateHoverInteraction(
                        eventType = event.type,
                        inShape = inShape,
                        current = hoverInteraction,
                        interactionSource = interactionSource,
                    )

                    if (!inShape && event.type != PointerEventType.Exit) {
                        change.consume()
                    }
                }
            }
        },
    )

    fun update(
        shape: Shape,
        interactionSource: MutableInteractionSource,
        enabled: Boolean,
    ) {
        val changed = shape != this.shape || interactionSource != this.interactionSource || enabled != this.enabled
        this.shape = shape
        this.interactionSource = interactionSource
        this.enabled = enabled
        if (changed) pointerInputNode.resetPointerInputHandler()
    }
}

fun Modifier.clippedTapGestures(
    shape: Shape,
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
    hapticFeedbackEnabled: Boolean,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
): Modifier = this then ClippedTapGesturesElement(
    shape = shape,
    interactionSource = interactionSource,
    enabled = enabled,
    hapticFeedbackEnabled = hapticFeedbackEnabled,
    onClick = onClick,
    onLongClick = onLongClick,
    onDoubleClick = onDoubleClick,
)

private data class ClippedTapGesturesElement(
    val shape: Shape,
    val interactionSource: MutableInteractionSource,
    val enabled: Boolean,
    val hapticFeedbackEnabled: Boolean,
    val onClick: () -> Unit,
    val onLongClick: (() -> Unit)?,
    val onDoubleClick: (() -> Unit)?,
) : ModifierNodeElement<ClippedTapGesturesNode>() {
    override fun create() = ClippedTapGesturesNode(element = this)

    override fun update(node: ClippedTapGesturesNode) {
        node.update(element = this)
    }
}

private class ClippedTapGesturesNode(
    private var element: ClippedTapGesturesElement,
) : DelegatingNode(),
    CompositionLocalConsumerModifierNode {
    private var pressInteraction: PressInteraction.Press? = null

    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            if (!element.enabled) return@SuspendingPointerInputModifierNode
            detectClippedTapGestures()
        },
    )

    fun update(element: ClippedTapGesturesElement) {
        // The tap detector reads callbacks from the current element, so only changes to what it
        // was started with need a restart: the enabled flag, the shape and which gestures exist.
        val restart = element.enabled != this.element.enabled ||
            element.shape != this.element.shape ||
            element.interactionSource != this.element.interactionSource ||
            (element.onLongClick == null) != (this.element.onLongClick == null) ||
            (element.onDoubleClick == null) != (this.element.onDoubleClick == null)
        this.element = element
        if (restart) pointerInputNode.resetPointerInputHandler()
    }

    private suspend fun PointerInputScope.detectClippedTapGestures() {
        val shape = element.shape
        detectTapGestures(
            onPress = { offset ->
                if (isInShape(size = size, offset = offset, shape = shape)) {
                    trackPress(offset = offset)
                }
            },
            onTap = { offset ->
                if (isInShape(size = size, offset = offset, shape = shape)) {
                    element.onClick()
                }
            },
            onLongPress = if (element.onLongClick != null) {
                { offset ->
                    if (isInShape(size = size, offset = offset, shape = shape)) {
                        if (element.hapticFeedbackEnabled) {
                            currentValueOf(LocalHapticFeedback).performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        element.onLongClick?.invoke()
                    }
                }
            } else {
                null
            },
            onDoubleTap = if (element.onDoubleClick != null) {
                { offset ->
                    if (isInShape(size = size, offset = offset, shape = shape)) {
                        element.onDoubleClick?.invoke()
                    }
                }
            } else {
                null
            },
        )
    }

    private suspend fun PressGestureScope.trackPress(offset: Offset) {
        val interactionSource = element.interactionSource
        val interaction = PressInteraction.Press(offset)
        pressInteraction = interaction
        coroutineScope.launch {
            interactionSource.emit(interaction)
        }

        val released = tryAwaitRelease()

        val currentInteraction = pressInteraction
        pressInteraction = null

        if (currentInteraction != null) {
            coroutineScope.launch {
                if (released) {
                    interactionSource.emit(PressInteraction.Release(currentInteraction))
                } else {
                    interactionSource.emit(PressInteraction.Cancel(currentInteraction))
                }
            }
        }
    }
}

private fun CoroutineScope.updateHoverInteraction(
    eventType: PointerEventType,
    inShape: Boolean,
    current: HoverInteraction.Enter?,
    interactionSource: MutableInteractionSource,
): HoverInteraction.Enter? = when (eventType) {
    PointerEventType.Enter, PointerEventType.Move -> {
        if (inShape && current == null) {
            val interaction = HoverInteraction.Enter()
            launch {
                interactionSource.emit(interaction)
            }
            interaction
        } else if (!inShape && current != null) {
            launch {
                interactionSource.emit(HoverInteraction.Exit(current))
            }
            null
        } else {
            current
        }
    }
    PointerEventType.Exit -> {
        launch {
            if (current != null) {
                interactionSource.emit(HoverInteraction.Exit(current))
            }
        }
        null
    }
    else -> current
}

private fun isInShape(
    size: IntSize,
    offset: Offset,
    shape: Shape,
): Boolean {
    return when (shape) {
        Shape.Circle -> {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val radius = size.width / 2f

            val dx = offset.x - centerX
            val dy = offset.y - centerY
            val distance = sqrt(dx * dx + dy * dy)

            distance <= radius
        }
        else -> true
    }
}
