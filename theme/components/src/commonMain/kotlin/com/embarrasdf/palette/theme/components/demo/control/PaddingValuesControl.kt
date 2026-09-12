package com.embarrasdf.palette.theme.components.demo.control

import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.theme.semantic.spacing.SpacingToken
import com.embarrasdf.palette.theme.semantic.dimension.PaddingValuesTokenSet
import kotlinx.collections.immutable.persistentListOf

/**
 * A collapsed column of [SpacingToken] pickers for each edge of a [PaddingValuesTokenSet].
 */
fun spacingTokenPaddingControls(
    value: () -> PaddingValuesTokenSet,
    onValueChange: (PaddingValuesTokenSet) -> Unit,
    name: String = "Padding",
    expandedInitial: Boolean = false,
): Control {
    return Control.ControlColumn(
        name = name,
        indent = true,
        expandedInitial = expandedInitial,
        controls = {
            persistentListOf(
                enumControl(
                    name = "Start",
                    values = { SpacingToken.entries },
                    selectedValue = { value().start },
                    onValueChange = { onValueChange(value().copy(start = it)) },
                ),
                enumControl(
                    name = "Top",
                    values = { SpacingToken.entries },
                    selectedValue = { value().top },
                    onValueChange = { onValueChange(value().copy(top = it)) },
                ),
                enumControl(
                    name = "End",
                    values = { SpacingToken.entries },
                    selectedValue = { value().end },
                    onValueChange = { onValueChange(value().copy(end = it)) },
                ),
                enumControl(
                    name = "Bottom",
                    values = { SpacingToken.entries },
                    selectedValue = { value().bottom },
                    onValueChange = { onValueChange(value().copy(bottom = it)) },
                ),
            )
        },
    )
}
