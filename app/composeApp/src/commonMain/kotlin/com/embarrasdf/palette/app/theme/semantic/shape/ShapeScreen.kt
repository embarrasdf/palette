package com.embarrasdf.palette.app.theme.semantic.shape

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
import com.embarrasdf.palette.theme.semantic.shape.ShapeScheme
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.shape.copy
import com.embarrasdf.palette.theme.semantic.shape.primitiveToken
import com.embarrasdf.palette.theme.semantic.shape.toShape
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ShapeScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberShapeScreenState(themeState = themeController)
    val shapeDemoState = rememberShapeDemoState(state.subjectShape)
    val shapeDemoControl = rememberShapeDemoControl(shapeDemoState)
    val control = rememberShapeScreenControl(
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
fun rememberShapeScreenState(
    themeState: ThemeState,
): ShapeScreenState {
    return rememberSaveable(
        themeState,
        saver = ShapeScreenStateSaver(themeState),
    ) {
        ShapeScreenState(themeState = themeState)
    }
}

@Stable
class ShapeScreenState(
    val themeState: ThemeState,
    subjectInitial: ShapeToken = ShapeToken.Primary,
) {
    var subject by mutableStateOf(subjectInitial)

    val shapeScheme: ShapeScheme
        get() = themeState.semantic.shapeScheme

    val subjectShape: Shape
        get() = subject.toShape(shapeScheme, themeState.primitive.shape)
}

private const val subjectKey = "subject"

fun ShapeScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state -> mapOf(subjectKey to state.subject.name) },
    restore = { map ->
        ShapeScreenState(
            themeState = themeState,
            subjectInitial = (map[subjectKey] as? String)
                ?.let { ShapeToken.valueOf(it) }
                ?: ShapeToken.Primary,
        )
    }
)

@Composable
fun rememberShapeScreenControl(
    state: ShapeScreenState,
    themeController: ThemeController,
    shapeDemoControl: ShapeDemoControl,
): ShapeScreenControl {
    return remember(state, themeController, shapeDemoControl) {
        ShapeScreenControl(
            state = state,
            themeController = themeController,
            shapeDemoControl = shapeDemoControl,
        )
    }
}

@Stable
class ShapeScreenControl(
    val state: ShapeScreenState,
    val themeController: ThemeController,
    val shapeDemoControl: ShapeDemoControl,
) {
    private val subjectControl = enumControl(
        name = "Token",
        values = { ShapeToken.entries },
        selectedValue = { state.subject },
        onValueChange = { state.subject = it },
    )

    private val primitiveControl = enumControl(
        name = "Primitive",
        values = { ShapePrimitiveToken.entries },
        selectedValue = { state.subject.primitiveToken(state.shapeScheme) },
        onValueChange = { primitiveToken ->
            themeController.updateSemantic {
                it.copy(
                    shapeScheme = it.shapeScheme.copy(
                        token = state.subject,
                        shapePrimitiveToken = primitiveToken,
                    ),
                )
            }
        },
    )

    val controls: PersistentList<Control> = buildList {
        add(subjectControl)
        add(primitiveControl)
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
