package com.embarrasdf.palette.app.theme.primitive.animation

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
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.primitive.EasingCurve
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.primitive.EasingValue
import com.embarrasdf.palette.theme.primitive.toCubic
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun EasingScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = remember(themeController) { EasingScreenState(themeController) }
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

/** The editable control points per token, seeded from the current theme easing curves. */
@Stable
class EasingScreenState(themeState: ThemeState) {
    var token by mutableStateOf(EasingPrimitiveToken.Standard)
        internal set

    val curves = mutableStateMapOf<EasingPrimitiveToken, EasingCurve>().apply {
        putAll(themeState.primitive.easing)
    }

    val active: EasingCurve
        get() = curves.getValue(token)
}

@Stable
class EasingScreenControl(
    val state: EasingScreenState,
    val themeController: ThemeController,
) {
    private fun set(curve: EasingCurve) {
        state.curves[state.token] = curve
        themeController.updatePrimitive {
            it.copy(easing = it.easing + (state.token to curve))
        }
    }

    private val tokenControl = enumControl(
        name = "Token",
        values = { EasingPrimitiveToken.entries },
        selectedValue = { state.token },
        onValueChange = { state.token = it },
    )

    private val curveControl = enumControl(
        name = "Curve",
        values = { EasingCurveType.entries },
        selectedValue = { state.active.type },
        onValueChange = { set(state.active.asType(it)) },
    )

    private val point1XControl = Control.Slider(
        name = "P1 X",
        value = { state.active.point1X },
        onValueChange = { set(state.active.withPoint1X(it)) },
        valueRange = { 0f..1f },
    )

    private val point1YControl = Control.Slider(
        name = "P1 Y",
        value = { state.active.point1Y },
        onValueChange = { set(state.active.withPoint1Y(it)) },
        valueRange = { 0f..1f },
    )

    private val point2XControl = Control.Slider(
        name = "P2 X",
        value = { state.active.point2X },
        onValueChange = { set(state.active.withPoint2X(it)) },
        valueRange = { 0f..1f },
    )

    private val point2YControl = Control.Slider(
        name = "P2 Y",
        value = { state.active.point2Y },
        onValueChange = { set(state.active.withPoint2Y(it)) },
        valueRange = { 0f..1f },
    )

    val controls: PersistentList<Control>
        get() = buildList {
            add(tokenControl)
            add(curveControl)
            add(point1XControl)
            add(point1YControl)
            if (state.active is EasingCurve.Cubic) {
                add(point2XControl)
                add(point2YControl)
            }
        }.toPersistentList()
}

/** The curve shapes an [EasingCurve] can take, for the editor's type selector. */
enum class EasingCurveType {
    Cubic,
    Quadratic,
}

private val EasingCurve.type: EasingCurveType
    get() = when (this) {
        is EasingCurve.Cubic -> EasingCurveType.Cubic
        is EasingCurve.Quadratic -> EasingCurveType.Quadratic
    }

private val EasingCurve.point1X: Float
    get() = when (this) {
        is EasingCurve.Cubic -> x1
        is EasingCurve.Quadratic -> x1
    }

private val EasingCurve.point1Y: Float
    get() = when (this) {
        is EasingCurve.Cubic -> y1
        is EasingCurve.Quadratic -> y1
    }

private val EasingCurve.point2X: Float
    get() = (this as? EasingCurve.Cubic)?.x2 ?: 0f

private val EasingCurve.point2Y: Float
    get() = (this as? EasingCurve.Cubic)?.y2 ?: 0f

private fun EasingCurve.withPoint1X(value: Float): EasingCurve = when (this) {
    is EasingCurve.Cubic -> copy(x1 = value)
    is EasingCurve.Quadratic -> copy(x1 = value)
}

private fun EasingCurve.withPoint1Y(value: Float): EasingCurve = when (this) {
    is EasingCurve.Cubic -> copy(y1 = value)
    is EasingCurve.Quadratic -> copy(y1 = value)
}

private fun EasingCurve.withPoint2X(value: Float): EasingCurve = when (this) {
    is EasingCurve.Cubic -> copy(x2 = value)
    is EasingCurve.Quadratic -> this
}

private fun EasingCurve.withPoint2Y(value: Float): EasingCurve = when (this) {
    is EasingCurve.Cubic -> copy(y2 = value)
    is EasingCurve.Quadratic -> this
}

/** Converts a curve to [type], keeping the first control point and expanding a quadratic to its
 *  exact cubic equivalent. */
private fun EasingCurve.asType(type: EasingCurveType): EasingCurve = when (type) {
    EasingCurveType.Cubic -> when (this) {
        is EasingCurve.Cubic -> this
        is EasingCurve.Quadratic -> toCubic()
    }

    EasingCurveType.Quadratic -> when (this) {
        is EasingCurve.Cubic -> EasingCurve.Quadratic(x1 = x1, y1 = y1)
        is EasingCurve.Quadratic -> this
    }
}
