package com.embarrasdf.palette.modifiers

private const val UniformShaderName = "composable"
private const val UniformAmount = "amount"

// SKSL
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

    override fun createRenderEffect() = control.createRenderEffect()

    override fun setAmount(amount: Float) {
        this.amount = amount
        control.setFloatUniform(UniformAmount, amount)
    }
}
