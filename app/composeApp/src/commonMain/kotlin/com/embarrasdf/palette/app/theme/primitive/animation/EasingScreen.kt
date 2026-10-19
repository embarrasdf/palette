package com.embarrasdf.palette.app.theme.primitive.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.subject.AnimationDemoSubject
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.primitive.EasingValue
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun EasingScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = remember { EasingScreenState() }
    val control = remember(state, themeController) { EasingScreenControl(state, themeController) }

    Scaffold(
        modifier = modifier,
        topBar = {
            DemoTopBar(
                title = "Easing",
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
                .padding(paddingValues),
        ) {
            AnimationDemoSubject(
                subject = AnimationDemoSubject.Ball,
                spec = AnimationSpec.Tween(
                    durationMillis = 800,
                    easing = EasingValue.Preset(state.token),
                ),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

/**
 * Editable control points for each [EasingPrimitiveToken], seeded from its hard-coded defaults. The
 * curves are held per token so switching the selected token keeps each one's edits.
 */
@Stable
class EasingScreenState {
    var token by mutableStateOf(EasingPrimitiveToken.Standard)
        internal set

    val curves = mutableStateMapOf<EasingPrimitiveToken, EasingValue.CubicBezier>().apply {
        EasingPrimitiveToken.entries.forEach { token ->
            put(token, EasingValue.CubicBezier(token.x1, token.y1, token.x2, token.y2))
        }
    }

    val active: EasingValue.CubicBezier
        get() = curves.getValue(token)
}

@Stable
class EasingScreenControl(
    val state: EasingScreenState,
    val themeController: ThemeController,
) {
    private fun update(curve: EasingValue.CubicBezier) {
        state.curves[state.token] = curve
        themeController.updatePrimitive {
            it.copy(
                easing = it.easing + (state.token to CubicBezierEasing(curve.x1, curve.y1, curve.x2, curve.y2)),
            )
        }
    }

    private val tokenControl = enumControl(
        name = "Token",
        values = { EasingPrimitiveToken.entries },
        selectedValue = { state.token },
        onValueChange = { state.token = it },
    )

    private val point1XControl = Control.Slider(
        name = "P1 X",
        value = { state.active.x1 },
        onValueChange = { update(state.active.copy(x1 = it)) },
        valueRange = { 0f..1f },
    )

    private val point1YControl = Control.Slider(
        name = "P1 Y",
        value = { state.active.y1 },
        onValueChange = { update(state.active.copy(y1 = it)) },
        valueRange = { 0f..1f },
    )

    private val point2XControl = Control.Slider(
        name = "P2 X",
        value = { state.active.x2 },
        onValueChange = { update(state.active.copy(x2 = it)) },
        valueRange = { 0f..1f },
    )

    private val point2YControl = Control.Slider(
        name = "P2 Y",
        value = { state.active.y2 },
        onValueChange = { update(state.active.copy(y2 = it)) },
        valueRange = { 0f..1f },
    )

    val controls: PersistentList<Control> = persistentListOf(
        tokenControl,
        point1XControl,
        point1YControl,
        point2XControl,
        point2YControl,
    )
}
