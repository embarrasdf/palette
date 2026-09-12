package com.embarrasdf.palette.app.theme.primitive.shape

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.core.Shape
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.core.ShapeDemo
import com.embarrasdf.palette.components.demo.core.ShapeDemoControl
import com.embarrasdf.palette.components.demo.core.rememberShapeDemoControl
import com.embarrasdf.palette.components.demo.core.rememberShapeDemoState
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.control.rememberThemeController
import com.embarrasdf.palette.theme.primitive.ShapePrimitiveToken
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ShapeScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberPrimitiveShapeScreenState(themeState = themeController)
    val shapeDemoState = rememberShapeDemoState(state.subjectShape)
    val shapeDemoControl = rememberShapeDemoControl(shapeDemoState)
    val control = rememberPrimitiveShapeScreenControl(
        state = state,
        themeController = themeController,
        shapeDemoControl = shapeDemoControl,
    )

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Shape",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        Demo(
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ShapeDemo(
                shape = state.subjectShape,
                state = shapeDemoState,
            )
        }
    }
}

@Composable
fun rememberPrimitiveShapeScreenState(
    themeState: ThemeState,
): PrimitiveShapeScreenState {
    return rememberSaveable(
        themeState,
        saver = PrimitiveShapeScreenStateSaver(themeState),
    ) {
        PrimitiveShapeScreenState(themeState = themeState)
    }
}

@Stable
class PrimitiveShapeScreenState(
    val themeState: ThemeState,
    subjectInitial: ShapePrimitiveToken = ShapePrimitiveToken.Rectangle,
) {
    var subject by mutableStateOf(subjectInitial)

    val shapePrimitives: Map<ShapePrimitiveToken, Shape>
        get() = themeState.primitive.shape

    val subjectShape: Shape
        get() = shapePrimitives.getValue(subject)
}

private const val subjectKey = "subject"

fun PrimitiveShapeScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state -> mapOf(subjectKey to state.subject.name) },
    restore = { map ->
        PrimitiveShapeScreenState(
            themeState = themeState,
            subjectInitial = (map[subjectKey] as? String)
                ?.let { ShapePrimitiveToken.valueOf(it) }
                ?: ShapePrimitiveToken.Rectangle,
        )
    }
)

@Composable
fun rememberPrimitiveShapeScreenControl(
    state: PrimitiveShapeScreenState,
    themeController: ThemeController,
    shapeDemoControl: ShapeDemoControl,
): PrimitiveShapeScreenControl {
    return remember(state, themeController, shapeDemoControl) {
        PrimitiveShapeScreenControl(
            state = state,
            themeController = themeController,
            shapeDemoControl = shapeDemoControl,
        )
    }
}

@Stable
class PrimitiveShapeScreenControl(
    val state: PrimitiveShapeScreenState,
    val themeController: ThemeController,
    val shapeDemoControl: ShapeDemoControl,
) {
    private val subjectControl = enumControl(
        name = "Shape",
        values = { ShapePrimitiveToken.entries },
        selectedValue = { state.subject },
        onValueChange = { state.subject = it },
    )

    private val cornerRadiusControl = Control.Slider(
        name = "Corner radius",
        value = { state.shapePrimitives.getValue(ShapePrimitiveToken.RoundRect).cornerRadius.value },
        onValueChange = { radius ->
            themeController.updatePrimitive {
                it.copy(
                    shape = it.shape + (ShapePrimitiveToken.RoundRect to Shape.Rectangle(cornerRadius = radius.dp))
                )
            }
        },
        valueRange = { 0f..64f },
        stepIncrement = 4f,
    )

    val controls: PersistentList<Control>
        get() = buildList {
            add(subjectControl)
            when (state.subject) {
                ShapePrimitiveToken.RoundRect -> add(cornerRadiusControl)
                else -> Unit
            }
            addAll(shapeDemoControl.controls)
        }.toPersistentList()
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        ShapeScreen(
            themeController = rememberThemeController(),
            onNavigateUp = {},
        )
    }
}
