package com.embarrasdf.palette.components.demo.state

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.util.ColorSaver
import com.embarrasdf.palette.components.util.DrawStyleSaver
import com.embarrasdf.palette.components.util.PaddingValuesSaver
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.unit.Dp as ComposeDp

/**
 * Savers for values commonly held in a [DemoState]. Each restores `null` for a value of the wrong
 * type, so a saved value from an older version of the demo falls back to the default.
 */
object DemoSavers {
    val Float: Saver<Float, Any> = Saver(save = { it }, restore = { it as? Float })

    val Int: Saver<Int, Any> = Saver(save = { it }, restore = { it as? Int })

    val Boolean: Saver<Boolean, Any> = Saver(save = { it }, restore = { it as? Boolean })

    val String: Saver<String, Any> = Saver(save = { it }, restore = { it as? String })

    val Dp: Saver<ComposeDp, Any> = Saver(save = { it.value }, restore = { (it as? Float)?.dp })

    val Color: Saver<ComposeColor, Any> = ColorSaver

    val PaddingValues: Saver<PaddingValues, Any> = PaddingValuesSaver

    val DrawStyle: Saver<DrawStyle, Any> = DrawStyleSaver

    val FloatList: Saver<List<Float>, Any> = Saver(
        save = { ArrayList(it) },
        restore = { value ->
            (value as? List<*>)?.filterIsInstance<Float>()?.takeIf { it.size == value.size }
        },
    )

    inline fun <reified E : Enum<E>> enum(): Saver<E, Any> = Saver(
        save = { it.name },
        restore = { name -> enumValues<E>().firstOrNull { it.name == name } },
    )
}
