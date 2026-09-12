package com.embarrasdf.palette.components.demo.control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle

data class ButtonControlStyle(
    val labelStyle: TextStyle = TextStyle(),
    val buttonStyle: ButtonStyle = ButtonStyle(),
)

@Composable
fun ButtonControl(
    control: Control.Button,
    modifier: Modifier = Modifier,
    style: ButtonControlStyle = ButtonControlStyle(),
) {
    val enabled by rememberUpdatedState(control.enabled())
    Button(
        style = style.buttonStyle,
        onClick = control.onClick,
        enabled = enabled,
        modifier = modifier
            .then(control.modifier)
    ) {
        Text(control.name, style = style.labelStyle)
    }
}
