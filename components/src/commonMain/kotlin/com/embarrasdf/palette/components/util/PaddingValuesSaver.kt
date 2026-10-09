package com.embarrasdf.palette.components.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private const val StartKey = "start"
private const val TopKey = "top"
private const val EndKey = "end"
private const val BottomKey = "bottom"

val PaddingValuesSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            StartKey to value.calculateStartPadding(LayoutDirection.Ltr).value,
            TopKey to value.calculateTopPadding().value,
            EndKey to value.calculateEndPadding(LayoutDirection.Ltr).value,
            BottomKey to value.calculateBottomPadding().value,
        )
    },
    restore = { map ->
        PaddingValues(
            start = (map[StartKey] as Float).dp,
            top = (map[TopKey] as Float).dp,
            end = (map[EndKey] as Float).dp,
            bottom = (map[BottomKey] as Float).dp,
        )
    },
)
