package com.embarrasdf.palette.modifiers

private const val UniformShaderName = "composable"
private const val UniformSizeName = "size"
private const val UniformSubdivisionsName = "subdivisions"

// SKSL
private var ShaderSource = """
uniform shader $UniformShaderName;
uniform float2 $UniformSizeName;
uniform float $UniformSubdivisionsName;

half4 main(float2 fragCoord) {
    float2 newCoord = fragCoord;
    newCoord.x -= mod(fragCoord.x, subdivisions + 1);
    newCoord.y -= mod(fragCoord.y, subdivisions + 1);
    return composable.eval(newCoord);
}
"""

actual fun createPixelateShader(): PixelateShader {
    return PixelateShaderImpl()
}

class PixelateShaderImpl : PixelateShader {
    private val control = createShaderControl(ShaderSource, UniformShaderName)

    private var subdivisions: Int = 0

    override fun isActive(): Boolean = subdivisions != 0

    override fun createRenderEffect() = control.createRenderEffect()

    override fun setSubdivisions(subdivisions: Int) {
        this.subdivisions = subdivisions
        control.setFloatUniform(UniformSubdivisionsName, subdivisions.toFloat())
    }

    override fun onRemeasured(width: Int, height: Int) {
        control.setFloatUniform(UniformSizeName, width.toFloat(), height.toFloat())
    }
}
