package com.embarrasdf.palette.components.demo.control

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.util.copy
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.round

/**
 * A collapsed column of continuous dp sliders for each edge of a [PaddingValues]. The sliders are
 * continuous (no tick marks), but each edge value is snapped to [snapIncrement] dp. Edges are read
 * and written in [LayoutDirection.Ltr] so start/end map to left/right consistently.
 */
fun paddingValuesControls(
    value: () -> PaddingValues,
    onValueChange: (PaddingValues) -> Unit,
    name: String = "Padding",
    valueRange: () -> ClosedFloatingPointRange<Float> = { 0f..80f },
    snapIncrement: Float = 1f,
    expandedInitial: Boolean = false,
): Control {
    fun snap(value: Float): Float =
        if (snapIncrement > 0f) round(value / snapIncrement) * snapIncrement else value

    fun edgeControl(
        edgeName: String,
        edgeValue: (PaddingValues) -> Float,
        withEdge: (PaddingValues, Float) -> PaddingValues,
    ) = Control.Slider(
        name = edgeName,
        value = { edgeValue(value()) },
        onValueChange = { onValueChange(withEdge(value(), snap(it))) },
        valueRange = valueRange,
    )

    return Control.ControlColumn(
        name = name,
        indent = true,
        expandedInitial = expandedInitial,
        controls = {
            persistentListOf(
                edgeControl(
                    edgeName = "Start",
                    edgeValue = { it.calculateStartPadding(LayoutDirection.Ltr).value },
                    withEdge = { pv, v -> pv.copy(start = v.dp, layoutDirection = LayoutDirection.Ltr) },
                ),
                edgeControl(
                    edgeName = "Top",
                    edgeValue = { it.calculateTopPadding().value },
                    withEdge = { pv, v -> pv.copy(top = v.dp, layoutDirection = LayoutDirection.Ltr) },
                ),
                edgeControl(
                    edgeName = "End",
                    edgeValue = { it.calculateEndPadding(LayoutDirection.Ltr).value },
                    withEdge = { pv, v -> pv.copy(end = v.dp, layoutDirection = LayoutDirection.Ltr) },
                ),
                edgeControl(
                    edgeName = "Bottom",
                    edgeValue = { it.calculateBottomPadding().value },
                    withEdge = { pv, v -> pv.copy(bottom = v.dp, layoutDirection = LayoutDirection.Ltr) },
                ),
            )
        },
    )
}
