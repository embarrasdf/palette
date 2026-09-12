package com.embarrasdf.palette.components.demo.core

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Icon
import com.embarrasdf.palette.components.core.Sizing
import com.embarrasdf.palette.components.core.IconStyle
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.ColorSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import kotlinx.collections.immutable.persistentListOf

private val IconDemoContainerSize = 240.dp

/**
 * Which [Sizing] variant the demo is currently exercising. Selecting one exposes only the
 * controls relevant to that variant (a `Dp` for [Sizing.Fixed], a fraction for [Sizing.Scale],
 * nothing for [Sizing.Fill]).
 */
enum class SizingType {
    Fixed,
    Scale,
    Fill,
}

@Composable
fun IconDemo(
    state: IconDemoState = rememberIconDemoState(),
    control: IconDemoControl = rememberIconDemoControl(state),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        IconDemo(state = state)
    }
}

@Composable
fun DemoScope.IconDemo(
    modifier: Modifier = Modifier,
    state: IconDemoState = rememberIconDemoState(),
) {
    // A fixed, bordered container so the Scale/Fill variants have visible bounds to size against.
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(IconDemoContainerSize)
            .align(Alignment.Center)
            .border(1.dp, PaletteTheme.semantic.color.outline)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Demo Icon",
            style = state.iconStyle,
        )
    }
}

@Composable
fun rememberIconDemoState(
    colorInitial: Color = PaletteTheme.semantic.color.primary,
): IconDemoState = rememberSaveable(saver = IconDemoStateSaver) {
    IconDemoState(colorInitial = colorInitial)
}

@Stable
class IconDemoState(
    sizeTypeInitial: SizingType = SizingType.Fixed,
    fixedSizeInitial: Dp = 48.dp,
    scaleInitial: Float = 0.75f,
    colorInitial: Color = Color.Unspecified,
) {
    var sizeType by mutableStateOf(sizeTypeInitial)
        internal set
    var fixedSize by mutableStateOf(fixedSizeInitial)
        internal set
    var scale by mutableStateOf(scaleInitial)
        internal set
    var color by mutableStateOf(colorInitial)
        internal set

    val sizing: Sizing
        get() = when (sizeType) {
            SizingType.Fixed -> Sizing.Fixed(fixedSize)
            SizingType.Scale -> Sizing.Scale(scale)
            SizingType.Fill -> Sizing.Fill
        }

    val iconStyle: IconStyle
        get() = IconStyle(size = sizing, color = color)
}

private const val sizeTypeKey = "sizeType"
private const val fixedSizeKey = "fixedSize"
private const val scaleKey = "scale"
private const val colorKey = "color"

val IconDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            sizeTypeKey to value.sizeType,
            fixedSizeKey to value.fixedSize.value,
            scaleKey to value.scale,
            colorKey to save(value.color, ColorSaver, this),
        )
    },
    restore = { map ->
        IconDemoState(
            sizeTypeInitial = map[sizeTypeKey] as SizingType,
            fixedSizeInitial = (map[fixedSizeKey] as Float).dp,
            scaleInitial = map[scaleKey] as Float,
            colorInitial = restore(map[colorKey], ColorSaver)!!,
        )
    },
)

@Composable
fun rememberIconDemoControl(
    state: IconDemoState,
): IconDemoControl = remember(state) { IconDemoControl(state) }

@Stable
class IconDemoControl(
    val state: IconDemoState,
) {
    val colorControl = Control.Color(
        name = "Color",
        color = { state.color },
        onColorChange = { state.color = it },
    )

    val sizeTypeControl = enumControl(
        name = "Size",
        values = { SizingType.entries },
        selectedValue = { state.sizeType },
        onValueChange = { state.sizeType = it },
    )

    val fixedSizeControl = Control.Slider(
        name = "Size (dp)",
        value = { state.fixedSize.value },
        onValueChange = { state.fixedSize = it.dp },
        valueRange = { 0f..IconDemoContainerSize.value },
        stepIncrement = 4f,
    )

    val scaleControl = Control.Slider(
        name = "Scale",
        value = { state.scale },
        onValueChange = { state.scale = it },
        valueRange = { 0f..1f },
    )

    val styleControls = Control.ControlColumn(
        name = "Style",
        indent = true,
        expandedInitial = true,
        controls = {
            persistentListOf(
                colorControl,
                sizeTypeControl,
                *sizeControls(state.sizeType).toTypedArray(),
            )
        },
    )

    // Only the control relevant to the selected size variant is exposed.
    private fun sizeControls(sizeType: SizingType): List<Control> = when (sizeType) {
        SizingType.Fixed -> listOf(fixedSizeControl)
        SizingType.Scale -> listOf(scaleControl)
        SizingType.Fill -> emptyList()
    }

    val controls = persistentListOf(styleControls)
}

@Preview
@Composable
private fun Preview() {
    IconDemo()
}
