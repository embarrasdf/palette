package com.embarrasdf.palette.components.demo.control

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.components.core.Slider
import com.embarrasdf.palette.components.core.SliderStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.core.stepsForIncrement

data class SliderControlStyle(
    val labelStyle: TextStyle = TextStyle(),
    val sliderStyle: SliderStyle = SliderStyle(),
    val spacing: Dp = 8.dp,
)

@Composable
fun SliderControl(
    control: Control.Slider,
    modifier: Modifier = Modifier,
    style: SliderControlStyle = SliderControlStyle(),
) {
    val name by rememberUpdatedState(control.name())
    val value by rememberUpdatedState(control.value())
    val valueRange by rememberUpdatedState(control.valueRange())

    Column(
        verticalArrangement = Arrangement.spacedBy(style.spacing),
        modifier = modifier,
    ) {
        val label by derivedStateOf {
            val value = if (value % 1f == 0f) value.toInt() else value.toString()
            "$name: $value"
        }
        Text(
            text = label,
            style = style.labelStyle,
        )
        val steps = control.stepIncrement?.let { stepsForIncrement(valueRange, it) } ?: 0
        Slider(
            value = value,
            onValueChange = control.onValueChange,
            valueRange = valueRange,
            steps = steps,
            style = style.sliderStyle,
            modifier = Modifier.semantics {
                contentDescription = name
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    var value by remember { mutableStateOf(0.5f) }
    val control = remember {
        Control.Slider(
            name = "Amount",
            value = { value },
            onValueChange = { value = it },
        )
    }
    SliderControl(control = control)
}
