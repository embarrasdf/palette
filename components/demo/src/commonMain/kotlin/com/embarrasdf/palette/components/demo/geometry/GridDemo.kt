package com.embarrasdf.palette.components.demo.geometry

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.theme.components.core.Surface
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.util.DensitySaver
import com.embarrasdf.palette.components.geometry.Grid
import com.embarrasdf.palette.components.geometry.GridCoordinateSystem
import com.embarrasdf.palette.components.geometry.GridCoordinateSystemType
import com.embarrasdf.palette.components.geometry.GridLineStyle
import com.embarrasdf.palette.components.geometry.GridLineStyleSaver
import com.embarrasdf.palette.components.geometry.GridScale
import com.embarrasdf.palette.components.geometry.GridScaleType
import com.embarrasdf.palette.components.geometry.GridVertex
import com.embarrasdf.palette.components.geometry.GridVertexType
import com.embarrasdf.palette.components.util.ColorSaver
import com.embarrasdf.palette.components.util.DrawStyleSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.PI

@Composable
fun GridDemo(
    modifier: Modifier = Modifier,
    state: GridDemoState = rememberGridDemoState(),
    control: GridDemoControl = rememberGridDemoControl(state),
) {
    Demo(
        controls = control.controls,
        modifier = modifier.fillMaxSize(),
    ) {
        GridDemo(
            state = state,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun DemoScope.GridDemo(
    modifier: Modifier = Modifier,
    state: GridDemoState = rememberGridDemoState(),
) {
    Grid(
        lineStyle = state.lineStyle.takeIf { state.showLines },
        vertex = state.vertexState.vertex,
        coordinateSystem = state.coordinateSystem,
        offset = with(LocalDensity.current) {
            Offset(state.offsetX.toPx(), state.offsetY.toPx())
        },
        clipToBounds = state.clipToBounds,
        modifier = modifier
    )
}

@Composable
fun rememberGridDemoState(
    color: Color = PaletteTheme.semantic.color.primary,
    density: Density = LocalDensity.current,
): GridDemoState = rememberSaveable(
    saver = GridDemoStateSaver,
) {
    GridDemoState(
        color = color,
        density = density,
    )
}

@Stable
class GridDemoState(
    val density: Density,
    val color: Color,
    xGridScaleStateInitial: CartesianGridScaleState = CartesianGridScaleState(),
    yGridScaleStateInitial: CartesianGridScaleState = CartesianGridScaleState(),
    polarGridScaleStateInitial: PolarGridScaleState = PolarGridScaleState(),
    coordinateSystemTypeInitial: GridCoordinateSystemType = GridCoordinateSystemType.Cartesian,
    vertexStateInitial: GridVertexState = GridVertexState(
        density = density,
        colorInitial = color,
    ),
    showLinesInitial: Boolean = true,
    lineStyleInitial: GridLineStyle = GridLineStyle(
        color = color,
        stroke = Stroke(width = 1f),
    ),
    strokeWidthPxInitial: Float = 1f,
    offsetXInitial: Dp = 0.dp,
    offsetYInitial: Dp = 0.dp,
    rotationDegreesInitial: Float = 0f,
    clipToBoundsInitial: Boolean = true,
) {
    var coordinateSystemType by mutableStateOf(coordinateSystemTypeInitial)
        internal set
    val coordinateSystem: GridCoordinateSystem
        get() = when (coordinateSystemType) {
            GridCoordinateSystemType.Cartesian -> GridCoordinateSystem.Cartesian(
                scaleX = xGridScaleState.gridScale,
                scaleY = yGridScaleState.gridScale,
                rotationDegrees = rotationDegrees,
            )

            GridCoordinateSystemType.Polar -> GridCoordinateSystem.Polar(
                radiusScale = polarGridScaleState.gridScale,
                thetaRadians = polarGridScaleState.thetaRadians,
                rotationDegrees = rotationDegrees,
            )
        }

    var xGridScaleState by mutableStateOf(xGridScaleStateInitial)
        internal set
    var yGridScaleState by mutableStateOf(yGridScaleStateInitial)
        internal set
    var polarGridScaleState by mutableStateOf(polarGridScaleStateInitial)
        internal set

    var vertexState by mutableStateOf(vertexStateInitial)
        internal set

    var showLines by mutableStateOf(showLinesInitial)
        internal set
    var lineStyle: GridLineStyle by mutableStateOf(lineStyleInitial)
        internal set
    var strokeWidthPx by mutableStateOf(strokeWidthPxInitial)
        internal set

    var offsetX by mutableStateOf(offsetXInitial)
        internal set
    var offsetY by mutableStateOf(offsetYInitial)
        internal set
    var rotationDegrees by mutableStateOf(rotationDegreesInitial)
        internal set
    var clipToBounds by mutableStateOf(clipToBoundsInitial)
        internal set
}

private const val ColorKey = "color"
private const val DensityKey = "density"
private const val CoordinateSystemTypeKey = "coordinateSystem"
private const val XGridScaleStateKey = "xGridScaleState"
private const val YGridScaleStateKey = "yGridScaleState"
private const val PolarGridScaleStateKey = "radiusGridScaleState"
private const val VertexStateKey = "vertexState"
private const val ShowLinesKey = "showLines"
private const val LineStyleKey = "lineStyle"
private const val StrokeWidthPxKey = "strokeWidthPx"
private const val OffsetXKey = "offsetX"
private const val OffsetYKey = "offsetY"
private const val RotationDegreesKey = "rotationDegrees"
private const val ClipToBoundsKey = "clipToBounds"

val GridDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            ColorKey to save(value.color, ColorSaver, this),
            DensityKey to save(value.density, DensitySaver, this),
            CoordinateSystemTypeKey to value.coordinateSystemType,
            XGridScaleStateKey to save(
                value = value.xGridScaleState,
                saver = CartesianGridScaleStateSaver,
                scope = this,
            ),
            YGridScaleStateKey to save(
                value = value.yGridScaleState,
                saver = CartesianGridScaleStateSaver,
                scope = this,
            ),
            PolarGridScaleStateKey to save(
                value = value.polarGridScaleState,
                saver = PolarGridScaleStateSaver,
                scope = this,
            ),
            VertexStateKey to save(
                value = value.vertexState,
                saver = GridVertexStateSaver,
                scope = this,
            ),
            ShowLinesKey to value.showLines,
            LineStyleKey to save(value.lineStyle, GridLineStyleSaver, this),
            StrokeWidthPxKey to value.strokeWidthPx,
            OffsetXKey to value.offsetX.value,
            OffsetYKey to value.offsetY.value,
            RotationDegreesKey to value.rotationDegrees,
            ClipToBoundsKey to value.clipToBounds,
        )
    },
    restore = { value ->
        GridDemoState(
            color = restore(value[ColorKey], ColorSaver)!!,
            density = restore(value[DensityKey], DensitySaver)!!,
            coordinateSystemTypeInitial = value[CoordinateSystemTypeKey] as GridCoordinateSystemType,
            xGridScaleStateInitial = restore(
                value = value[XGridScaleStateKey],
                saver = CartesianGridScaleStateSaver,
            )!!,
            yGridScaleStateInitial = restore(
                value = value[YGridScaleStateKey],
                saver = CartesianGridScaleStateSaver,
            )!!,
            polarGridScaleStateInitial = restore(
                value = value[PolarGridScaleStateKey],
                saver = PolarGridScaleStateSaver,
            )!!,
            vertexStateInitial = restore(value[VertexStateKey], GridVertexStateSaver)!!,
            showLinesInitial = value[ShowLinesKey] as Boolean,
            strokeWidthPxInitial = value[StrokeWidthPxKey] as Float,
            lineStyleInitial = restore(value[LineStyleKey], GridLineStyleSaver)!!,
            offsetXInitial = (value[OffsetXKey] as Float).dp,
            offsetYInitial = (value[OffsetYKey] as Float).dp,
            rotationDegreesInitial = value[RotationDegreesKey] as Float,
            clipToBoundsInitial = value[ClipToBoundsKey] as Boolean,
        )
    },
)

@Composable
fun rememberGridDemoControl(
    state: GridDemoState,
): GridDemoControl = remember(state) { GridDemoControl(state) }

@Stable
class GridDemoControl(
    val state: GridDemoState,
) {
    val gridScaleXControls = CartesianGridScaleControl(
        name = "x-axis",
        state = state.xGridScaleState,
        onStateChanged = { state.xGridScaleState = it },
    ).controls

    val gridScaleYControls = CartesianGridScaleControl(
        name = "y-axis",
        state = state.yGridScaleState,
        onStateChanged = { state.yGridScaleState = it },
    ).controls

    val polarControls = PolarGridScaleControl(
        state = state.polarGridScaleState,
        onStateChanged = { state.polarGridScaleState = it },
    ).controls

    val coordinateSystemControl = enumControl(
        name = "Coordinate System",
        values = { GridCoordinateSystemType.entries },
        selectedValue = { state.coordinateSystemType },
        onValueChange = { state.coordinateSystemType = it },
    )

    val vertexControls = GridVertexControl(
        state = state.vertexState,
        onStateChanged = { vertexState -> state.vertexState = vertexState },
    ).controls

    val showLinesControl = Control.Toggle(
        name = "Show Grid Lines",
        value = { state.showLines },
        onValueChange = { state.showLines = it },
    )

    val strokeWidthControl = Control.Slider(
        name = "Line Stroke Width",
        value = { state.strokeWidthPx },
        onValueChange = {
            state.strokeWidthPx = it
            state.lineStyle = state.lineStyle.copy(
                stroke = Stroke(width = state.strokeWidthPx),
            )
        },
        valueRange = { 1f..100f },
    )

    val offsetXControl = Control.Slider(
        name = "Offset X",
        value = { state.offsetX.value },
        onValueChange = { state.offsetX = it.dp },
        valueRange = { -2000f..2000f },
    )

    val offsetYControl = Control.Slider(
        name = "Offset Y",
        value = { state.offsetY.value },
        onValueChange = { state.offsetY = it.dp },
        valueRange = { -2000f..2000f },
    )

    val rotationDegreesControl = Control.Slider(
        name = "Rotation",
        value = { state.rotationDegrees },
        onValueChange = { state.rotationDegrees = it },
        valueRange = { -180f..180f },
    )

    val clipControl = Control.Toggle(
        name = "Clip to bounds",
        value = { state.clipToBounds },
        onValueChange = { state.clipToBounds = it },
    )

    val cartesianControls = persistentListOf(
        *gridScaleXControls.toTypedArray(),
        *gridScaleYControls.toTypedArray(),
    )

    val coordinateSystemControls: PersistentList<Control>
        get() {
            val scaleControls = when (state.coordinateSystem) {
                is GridCoordinateSystem.Cartesian -> cartesianControls
                is GridCoordinateSystem.Polar -> polarControls
            }
            return persistentListOf(
                coordinateSystemControl,
                *scaleControls.toTypedArray()
            )
        }

    val lineControls
        get() = if (state.showLines) {
            persistentListOf(
                showLinesControl,
                strokeWidthControl,
            )
        } else {
            persistentListOf(
                showLinesControl,
            )
        }

    val controls
        get() = persistentListOf(
            *coordinateSystemControls.toTypedArray(),
            *vertexControls.toTypedArray(),
            *lineControls.toTypedArray(),
            offsetXControl,
            offsetYControl,
            rotationDegreesControl,
            clipControl,
        )
}

@Stable
class CartesianGridScaleState(
    gridScaleTypeInitial: GridScaleType = GridScaleType.Linear,
    gridSpacingInitial: Dp = 100.dp,
    gridScaleBaseInitial: Float = 2f,
    gridScaleExponentInitial: Float = 2f,
) : GridScaleState(
    gridScaleTypeInitial = gridScaleTypeInitial,
    gridSpacingInitial = gridSpacingInitial,
    gridScaleBaseInitial = gridScaleBaseInitial,
    gridScaleExponentInitial = gridScaleExponentInitial,
)

private const val CartesianGridSpacingKey = "cartesianGridSpacing"
private const val CartesianGridScaleBaseKey = "cartesianGridScaleBase"
private const val CartesianGridScaleExponentKey = "cartesianGridScaleExponent"

val CartesianGridScaleStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            CartesianGridSpacingKey to value.gridSpacing.value,
            CartesianGridScaleBaseKey to value.gridScaleBase,
            CartesianGridScaleExponentKey to value.gridScaleExponent,
        )
    },
    restore = { value ->
        CartesianGridScaleState(
            gridSpacingInitial = (value[CartesianGridSpacingKey] as Float).dp,
            gridScaleBaseInitial = value[CartesianGridScaleBaseKey] as Float,
            gridScaleExponentInitial = value[CartesianGridScaleExponentKey] as Float,
        )
    },
)

@Stable
class CartesianGridScaleControl(
    state: CartesianGridScaleState,
    onStateChanged: ((CartesianGridScaleState) -> Unit)? = null,
    private val name: String? = null,
) {
    val gridScaleControl = GridScaleControl(
        state = state,
        onStateChanged = { onStateChanged?.invoke(it as CartesianGridScaleState) },
    )

    val controls = persistentListOf(
        Control.ControlColumn(
            name = name ?: "Scale",
            indent = true,
            controls = {
                persistentListOf(
                    *gridScaleControl.controls.toTypedArray(),
                )
            },
        )
    )
}

@Stable
class PolarGridScaleState(
    gridScaleInitial: GridScaleType = GridScaleType.Linear,
    gridSpacingInitial: Dp = 50.dp,
    gridScaleBaseInitial: Float = 2f,
    gridScaleExponentInitial: Float = 2f,
    thetaRadiansInitial: Float = (PI / 3f).toFloat(),
) : GridScaleState(
    gridScaleTypeInitial = gridScaleInitial,
    gridSpacingInitial = gridSpacingInitial,
    gridScaleBaseInitial = gridScaleBaseInitial,
    gridScaleExponentInitial = gridScaleExponentInitial,
) {
    var thetaRadians by mutableStateOf(thetaRadiansInitial)
        internal set
}

private const val PolarGridSpacingKey = "polarGridSpacing"
private const val PolarGridScaleBaseKey = "polarGridScaleBase"
private const val PolarGridScaleExponentKey = "polarGridScaleExponent"
private const val PolarGridThetaRadiansKey = "polarGridThetaRadians"

val PolarGridScaleStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            PolarGridSpacingKey to value.gridSpacing.value,
            PolarGridScaleBaseKey to value.gridScaleBase,
            PolarGridScaleExponentKey to value.gridScaleExponent,
            PolarGridThetaRadiansKey to value.thetaRadians,
        )
    },
    restore = { value ->
        PolarGridScaleState(
            gridSpacingInitial = (value[PolarGridSpacingKey] as Float).dp,
            gridScaleBaseInitial = value[PolarGridScaleBaseKey] as Float,
            gridScaleExponentInitial = value[PolarGridScaleExponentKey] as Float,
            thetaRadiansInitial = value[PolarGridThetaRadiansKey] as Float,
        )
    },
)

@Stable
class PolarGridScaleControl(
    val state: PolarGridScaleState,
    val onStateChanged: ((PolarGridScaleState) -> Unit)? = null,
) {
    val gridScaleControl = GridScaleControl(
        state = state,
        onStateChanged = { onStateChanged?.invoke(it as PolarGridScaleState) },
    )

    val thetaRadiansRange = 0.01f..(PI * 2f).toFloat()
    val thetaControl = Control.Slider(
        name = "Angle",
        value = { logToLinearScale(state.thetaRadians, thetaRadiansRange) },
        onValueChange = {
            state.thetaRadians = linearToLogScale(it, thetaRadiansRange)
            onStateChanged?.invoke(state)
        },
        valueRange = { thetaRadiansRange },
    )

    val controls = persistentListOf(
        Control.ControlColumn(
            name = "Radius",
            indent = true,
            controls = { gridScaleControl.controls },
        ),
        thetaControl,
    )
}

open class GridScaleState(
    gridScaleTypeInitial: GridScaleType,
    gridSpacingInitial: Dp,
    gridScaleBaseInitial: Float,
    gridScaleExponentInitial: Float,
) {
    var gridScaleType: GridScaleType by mutableStateOf(gridScaleTypeInitial)
        internal set
    var gridSpacing by mutableStateOf(gridSpacingInitial)
        internal set
    var gridScaleBase: Float by mutableStateOf(gridScaleBaseInitial)
        internal set
    var gridScaleExponent: Float by mutableStateOf(gridScaleExponentInitial)
        internal set

    val gridScale: GridScale
        get() = when (gridScaleType) {
            GridScaleType.Linear -> GridScale.Linear(gridSpacing)
            GridScaleType.Logarithmic -> GridScale.Logarithmic(
                spacing = gridSpacing,
                base = gridScaleBase,
            )

            GridScaleType.LogarithmicDecay -> GridScale.LogarithmicDecay(
                spacing = gridSpacing,
                base = gridScaleBase,
            )

            GridScaleType.Exponential -> GridScale.Exponential(
                spacing = gridSpacing,
                exponent = gridScaleExponent,
            )

            GridScaleType.ExponentialDecay -> GridScale.ExponentialDecay(
                spacing = gridSpacing,
                exponent = gridScaleExponent,
            )
        }
}

@Stable
open class GridScaleControl(
    val state: GridScaleState,
    val onStateChanged: ((GridScaleState) -> Unit)? = null,
) {
    val gridScaleTypeControl = enumControl(
        name = "Scale",
        values = { GridScaleType.entries },
        selectedValue = { state.gridScaleType },
        onValueChange = { state.gridScaleType = it }
    )

    val gridSpacingControl = Control.Slider(
        name = { if (state.gridScale is GridScale.Linear) "Spacing" else "Initial spacing" },
        value = { state.gridSpacing.value },
        onValueChange = {
            state.gridSpacing = it.dp
            onStateChanged?.invoke(state)
        },
        valueRange = { 0f..200f },
    )

    val scaleBaseRange = 1f..10f
    val gridScaleBaseControl = Control.Slider(
        name = "Log Base",
        value = { logToLinearScale(state.gridScaleBase, scaleBaseRange) },
        onValueChange = {
            state.gridScaleBase = linearToLogScale(it, scaleBaseRange)
            onStateChanged?.invoke(state)
        },
        valueRange = { scaleBaseRange },
    )

    val scaleExponentRange = 1.001f..10f
    val gridScaleExponentControl = Control.Slider(
        name = "Exponent Factor",
        value = { logToLinearScale(state.gridScaleExponent, scaleExponentRange) },
        onValueChange = {
            state.gridScaleExponent = linearToLogScale(it, scaleExponentRange)
            onStateChanged?.invoke(state)
        },
        valueRange = { scaleExponentRange },
    )

    val controls
        get() = when (state.gridScale) {
            is GridScale.Linear -> persistentListOf(
                gridScaleTypeControl,
                gridSpacingControl,
            )

            is GridScale.Logarithmic,
            is GridScale.LogarithmicDecay,
                -> persistentListOf(
                gridScaleTypeControl,
                gridSpacingControl,
                gridScaleBaseControl,
            )

            is GridScale.Exponential,
            is GridScale.ExponentialDecay,
                -> persistentListOf(
                gridScaleTypeControl,
                gridSpacingControl,
                gridScaleExponentControl,
            )
        }
}

class GridVertexState(
    val density: Density,
    colorInitial: Color,
    vertexTypeInitial: GridVertexType? = null,
    strokeWidthInitial: Dp = Dp.Hairline,
    drawStyleInitial: DrawStyle = Stroke(width = with(density) { strokeWidthInitial.toPx() }),
    widthInitial: Dp = 10.dp,
    heightInitial: Dp = 10.dp,
    rotationDegreesInitial: Float = 0f,
) {
    var vertexType by mutableStateOf(vertexTypeInitial)
        internal set
    var color by mutableStateOf(colorInitial)
        internal set
    var drawStyle by mutableStateOf(drawStyleInitial)
        internal set
    var strokeWidth by mutableStateOf(strokeWidthInitial)
        internal set
    var width by mutableStateOf(widthInitial)
        internal set
    var height by mutableStateOf(heightInitial)
        internal set
    var rotationDegrees by mutableStateOf(rotationDegreesInitial)
        internal set

    val strokeWidthPx
        get() = with(density) { strokeWidth.toPx() }
    val size
        get() = DpSize(width, height)

    val vertex: GridVertex?
        get() = when (vertexType) {
            GridVertexType.Oval -> GridVertex.Oval(
                color = color,
                size = DpSize(width, height),
                drawStyle = drawStyle,
                rotationDegrees = rotationDegrees,
            )

            GridVertexType.Rect -> GridVertex.Rect(
                color = color,
                size = DpSize(width, height),
                drawStyle = drawStyle,
                rotationDegrees = rotationDegrees,
            )

            GridVertexType.Plus -> GridVertex.Plus(
                color = color,
                size = DpSize(width, height),
                strokeWidth = strokeWidth,
                rotationDegrees = rotationDegrees,
            )

            GridVertexType.X -> GridVertex.X(
                color = color,
                size = DpSize(width, height),
                strokeWidth = strokeWidth,
                rotationDegrees = rotationDegrees,
            )

            null -> null
        }
}

private const val VertexDensityKey = "density"
private const val VertexTypeKey = "vertexType"
private const val VertexColorKey = "color"
private const val VertexStrokeWidthKey = "vertexStrokeWidth"
private const val VertexDrawStyleKey = "vertexDrawStyle"
private const val VertexWidthKey = "vertexWidth"
private const val VertexHeightKey = "vertexHeight"
private const val VertexRotationDegreesKey = "vertexRotationDegrees"

val GridVertexStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            VertexDensityKey to save(value.density, DensitySaver, this),
            VertexTypeKey to value.vertexType,
            VertexColorKey to save(value.color, ColorSaver, this),
            VertexStrokeWidthKey to value.strokeWidth.value,
            VertexDrawStyleKey to save(value.drawStyle, DrawStyleSaver, this),
            VertexWidthKey to value.width.value,
            VertexHeightKey to value.height.value,
            VertexRotationDegreesKey to value.rotationDegrees,
        )
    },
    restore = { value ->
        GridVertexState(
            density = restore(value[VertexDensityKey], DensitySaver)!!,
            vertexTypeInitial = value[VertexTypeKey] as GridVertexType,
            colorInitial = restore(value[VertexColorKey], ColorSaver)!!,
            strokeWidthInitial = (value[VertexStrokeWidthKey] as Float).dp,
            drawStyleInitial = restore(value[VertexDrawStyleKey], DrawStyleSaver)!!,
            widthInitial = (value[VertexWidthKey] as Float).dp,
            heightInitial = (value[VertexHeightKey] as Float).dp,
            rotationDegreesInitial = value[VertexRotationDegreesKey] as Float,
        )
    },
)

@Stable
class GridVertexControl(
    val state: GridVertexState,
    val onStateChanged: ((GridVertexState) -> Unit)? = null,
    private val name: String? = null,
) {
    enum class DemoVertexType {
        None,
        Oval,
        Rect,
        Plus,
        X,
    }
    val typeControl = enumControl(
        name = "Type",
        values = { DemoVertexType.entries },
        selectedValue = {
            when (state.vertexType) {
                GridVertexType.Oval -> DemoVertexType.Oval
                GridVertexType.Rect -> DemoVertexType.Rect
                GridVertexType.Plus -> DemoVertexType.Plus
                GridVertexType.X -> DemoVertexType.X
                null -> DemoVertexType.None
            }
        },
        onValueChange = {
            state.vertexType = when (it) {
                DemoVertexType.Oval -> GridVertexType.Oval
                DemoVertexType.Rect -> GridVertexType.Rect
                DemoVertexType.Plus -> GridVertexType.Plus
                DemoVertexType.X -> GridVertexType.X
                DemoVertexType.None -> null
            }
            onStateChanged?.invoke(state)
        }
    )

    val strokeWidthControl = Control.Slider(
        name = "Stroke Width",
        value = { state.strokeWidth.value },
        onValueChange = {
            state.strokeWidth = it.dp
            state.drawStyle = when (state.drawStyle) {
                is Stroke -> Stroke(width = state.strokeWidthPx)
                is Fill -> Fill
            }
            onStateChanged?.invoke(state)
        },
        valueRange = { 1f..100f },
    )

    enum class VertexDrawStyle {
        Stroke,
        Fill,
    }
    val drawStyleControl = enumControl(
        name = "Draw Style",
        values = { VertexDrawStyle.entries },
        selectedValue = {
            when (state.drawStyle) {
                is Fill -> VertexDrawStyle.Fill
                is Stroke -> VertexDrawStyle.Stroke
            }
        },
        onValueChange = {
            state.drawStyle = when (it) {
                VertexDrawStyle.Stroke -> Stroke(width = state.strokeWidthPx)
                VertexDrawStyle.Fill -> Fill
            }
            onStateChanged?.invoke(state)
        }
    )

    val rotationDegreesControl = Control.Slider(
        name = "Rotation",
        value = { state.rotationDegrees },
        onValueChange = {
            state.rotationDegrees = it
            onStateChanged?.invoke(state)
        },
        valueRange = { -180f..180f },
    )

    val sizeControl = Control.Slider(
        name = "Size",
        value = { (state.width.value + state.height.value) / 2f },
        onValueChange = {
            state.width = it.dp
            state.height = it.dp
            onStateChanged?.invoke(state)
        },
        valueRange = { 1f..100f },
    )

    val widthControl = Control.Slider(
        name = "Width",
        value = { state.width.value },
        onValueChange = {
            state.width = it.dp
            onStateChanged?.invoke(state)
        },
        valueRange = { 1f..100f },
    )

    val heightControl = Control.Slider(
        name = "Height",
        value = { state.height.value },
        onValueChange = {
            state.height = it.dp
            onStateChanged?.invoke(state)
        },
        valueRange = { 1f..100f },
    )

    val drawStyleControls
        get() = when (state.vertex) {
            is GridVertex.Oval,
            is GridVertex.Rect,
            -> persistentListOf(
                drawStyleControl,
                if (state.drawStyle is Stroke) strokeWidthControl else null,
            ).filterNotNull().toPersistentList()

            is GridVertex.Plus,
            is GridVertex.X,
            null,
            -> persistentListOf(strokeWidthControl)
        }

    val controls = persistentListOf(
        Control.ControlColumn(
            name = name ?: "Vertex",
            indent = true,
            controls = {
                if (state.vertex == null) {
                    persistentListOf(typeControl)
                } else {
                    persistentListOf(
                        typeControl,
                        *drawStyleControls.toTypedArray(),
                        sizeControl,
                        widthControl,
                        heightControl,
                        rotationDegreesControl,
                    )
                }
            },
        )
    )
}

@Preview
@Composable
fun GridDemoPreview() {
    PaletteTheme {
        Surface {
            GridDemo()
        }
    }
}
