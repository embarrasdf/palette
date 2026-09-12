package com.embarrasdf.palette.modifiers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.modifiers.preview.DemoCircle
import com.embarrasdf.palette.modifiers.preview.ModifierPreview
import org.intellij.lang.annotations.Language

private const val UniformShaderName = "composable"
private const val UniformAmount = "amount"

@Language("AGSL")
private var ShaderSource = """
uniform shader $UniformShaderName;
uniform float $UniformAmount;

half4 main(float2 fragCoord) {
    half4 color = composable.eval(fragCoord);
    return half4(abs(amount - color.r), abs(amount - color.g), abs(amount - color.b), color.a);
}
"""

actual fun createColorInvertShader(): ColorInvertShader {
    return ColorInvertShaderImpl()
}

class ColorInvertShaderImpl : ColorInvertShader {

    private val control = createShaderControl(ShaderSource, UniformShaderName)

    private var amount: Float = 0f

    override fun isActive(): Boolean = amount != 0f

    override fun createRenderEffect(): RenderEffect? = control.createRenderEffect()

    override fun setAmount(amount: Float) {
        this.amount = amount
        control.setFloatUniform(UniformAmount, amount)
    }
}

@ModifierPreview
@Composable
private fun Preview() {
    val range = 0f..1f
    var amount by remember { mutableStateOf(range.endInclusive / 2f) }
    Column {
        DemoCircle(
            modifier = Modifier
                .weight(1f)
                .colorInvert(
                    amount = { amount }
                )
        )
        Slider(
            valueRange = range,
            value = amount,
            onValueChange = { amount = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}
