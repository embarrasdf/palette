package com.embarrasdf.palette.components.demo.control

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.components.core.Checkbox
import com.embarrasdf.palette.components.core.CheckboxStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle

data class ToggleControlStyle(
    val labelStyle: TextStyle = TextStyle(),
    val checkboxStyle: CheckboxStyle = CheckboxStyle(),
    val spacing: Dp = 8.dp,
)

@Composable
fun ToggleControl(
    control: Control.Toggle,
    modifier: Modifier = Modifier,
    style: ToggleControlStyle = ToggleControlStyle(),
    includeTitle: Boolean = true,
) {
    val checked by rememberUpdatedState(control.value())

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (includeTitle) {
            Text(control.name, style = style.labelStyle)
            Spacer(modifier = Modifier.height(style.spacing))
        }

        Checkbox(checked, onCheckedChange = control.onValueChange, style = style.checkboxStyle)
    }
}

@Composable
fun ToggleControlRow(
    control: Control.Toggle,
    modifier: Modifier = Modifier,
    style: ToggleControlStyle = ToggleControlStyle(),
    includeTitle: Boolean = true,
) {
    val isChecked by rememberUpdatedState(control.value())

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (includeTitle) {
            Text(control.name, style = style.labelStyle)
            Spacer(modifier = Modifier.height(style.spacing))
        }

        Checkbox(isChecked, onCheckedChange = control.onValueChange, style = style.checkboxStyle)
    }
}

@Preview
@Composable
private fun ToggleControlPreview() {
    var on by remember { mutableStateOf(false) }
    val control = Control.Toggle(
        name = "Color",
        value = { on },
        onValueChange = { on = it }
    )
    ToggleControl(control = control)
}

@Preview
@Composable
private fun ToggleControlRowPreview() {
    var on by remember { mutableStateOf(false) }
    val control = Control.Toggle(
        name = "Color",
        value = { on },
        onValueChange = { on = it }
    )
    ToggleControlRow(control = control)
}
