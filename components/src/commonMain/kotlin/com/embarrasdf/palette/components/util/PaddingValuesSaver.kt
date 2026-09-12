package com.embarrasdf.palette.components.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private const val startKey = "start"
private const val topKey = "top"
private const val endKey = "end"
private const val bottomKey = "bottom"

val PaddingValuesSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            startKey to value.calculateStartPadding(LayoutDirection.Ltr).value,
            topKey to value.calculateTopPadding().value,
            endKey to value.calculateEndPadding(LayoutDirection.Ltr).value,
            bottomKey to value.calculateBottomPadding().value,
        )
    },
    restore = { map ->
        PaddingValues(
            start = (map[startKey] as Float).dp,
            top = (map[topKey] as Float).dp,
            end = (map[endKey] as Float).dp,
            bottom = (map[bottomKey] as Float).dp,
        )
    },
)
