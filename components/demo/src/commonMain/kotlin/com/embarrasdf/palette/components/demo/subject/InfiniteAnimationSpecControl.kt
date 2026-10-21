package com.embarrasdf.palette.components.demo.subject

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.RepeatMode
import kotlinx.collections.immutable.PersistentList

/**
 * Editable state for an [AnimationSpec.Repeat.Infinite]: a [driverState] duration-based spec that is
 * looped, plus the [repeatMode] applied at the end of each iteration. The driver is shared with the
 * finite [AnimationSpec.Repeat.Finite] editor — the two differ only in that the finite one also
 * carries an iteration count.
 */
@Stable
class InfiniteAnimationSpecState(
    repeatModeInitial: RepeatMode = RepeatMode.Restart,
    val driverState: DurationBasedSpecState = DurationBasedSpecState(),
) {
    var repeatMode by mutableStateOf(repeatModeInitial)
        internal set

    val spec: AnimationSpec.Repeat.Infinite
        get() = AnimationSpec.Repeat.Infinite(
            animation = driverState.spec,
            repeatMode = repeatMode,
        )

    companion object {
        /** Builds a state seeded from an existing [AnimationSpec.Repeat.Infinite]. */
        fun from(spec: AnimationSpec.Repeat.Infinite): InfiniteAnimationSpecState {
            return InfiniteAnimationSpecState(
                repeatModeInitial = spec.repeatMode,
                driverState = DurationBasedSpecState.from(spec.animation),
            )
        }
    }
}

private const val repeatModeKey = "repeatMode"
private const val driverKey = "driver"

val InfiniteAnimationSpecStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            repeatModeKey to value.repeatMode,
            driverKey to save(value.driverState, DurationBasedSpecStateSaver, this),
        )
    },
    restore = { value ->
        InfiniteAnimationSpecState(
            repeatModeInitial = value[repeatModeKey] as RepeatMode,
            driverState = restore(value[driverKey], DurationBasedSpecStateSaver)!!,
        )
    },
)

/**
 * Exposes controls for an [InfiniteAnimationSpecState]: the shared duration-based driver editor plus
 * a repeat-mode control.
 */
@Stable
class InfiniteAnimationSpecControl(
    val state: InfiniteAnimationSpecState,
    val onChanged: (() -> Unit)? = null,
) {
    private val driverControl = DurationBasedSpecControl(state.driverState, onChanged)

    val repeatModeControl = enumControl(
        name = "Repeat mode",
        values = { RepeatMode.entries },
        selectedValue = { state.repeatMode },
        onValueChange = {
            state.repeatMode = it
            onChanged?.invoke()
        },
    )

    val controls: PersistentList<Control>
        get() = driverControl.controls.add(repeatModeControl)
}
