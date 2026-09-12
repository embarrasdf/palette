package com.embarrasdf.palette.modifiers

import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.unit.Density

interface Shader {
    fun isActive(): Boolean = true
    fun createRenderEffect(): RenderEffect?
    fun onRemeasured(width: Int, height: Int) {}
    fun onDensityChanged(density: Density) {}
}
