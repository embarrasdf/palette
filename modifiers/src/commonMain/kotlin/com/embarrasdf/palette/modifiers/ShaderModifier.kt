package com.embarrasdf.palette.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.IntSize
import com.embarrasdf.palette.modifiers.util.useGraphicsLayer
import com.alexrdclement.trace.trace

data class ShaderElement<T: Shader>(
    val shader: T,
    val traceLabel: String,
) : ModifierNodeElement<ShaderNode<T>>() {
    override fun create() = ShaderNode(
        shader = shader,
        traceLabel = traceLabel,
    )
    override fun update(node: ShaderNode<T>) {
        node.shader = shader
        node.traceLabel = traceLabel
        node.invalidateDraw()
    }
}

open class ShaderNode<T: Shader>(
    var shader: T,
    var traceLabel: String,
) : Modifier.Node(),
    DrawModifierNode,
    LayoutAwareModifierNode,
    CompositionLocalConsumerModifierNode {

    override fun onPlaced(coordinates: LayoutCoordinates) {
        shader.onRemeasured(coordinates.size.width, coordinates.size.height)
        if (currentValueOf(LocalInspectionMode)) invalidateDraw()
    }

    override fun onRemeasured(size: IntSize) {
        shader.onRemeasured(size.width, size.height)
        if (currentValueOf(LocalInspectionMode)) invalidateDraw()
    }

    override fun onDensityChange() {
        shader.onDensityChanged(currentValueOf(LocalDensity))
        if (currentValueOf(LocalInspectionMode)) invalidateDraw()
    }

    override fun ContentDrawScope.draw() {
        trace(traceLabel) {
            if (!shader.isActive()) {
                drawContent()
                return@trace
            }
            val renderEffect = shader.createRenderEffect() ?: run {
                drawContent()
                return@trace
            }

            val graphicsContext = currentValueOf(LocalGraphicsContext)
            graphicsContext.useGraphicsLayer {
                this.renderEffect = renderEffect

                record { this@draw.drawContent() }
                drawLayer(this)
            }

            if (currentValueOf(LocalInspectionMode)) invalidateDraw()
        }
    }
}
