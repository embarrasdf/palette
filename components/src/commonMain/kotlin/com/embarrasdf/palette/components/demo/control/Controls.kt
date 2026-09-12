package com.embarrasdf.palette.components.demo.control

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ControlsStyle(
    val spacing: Dp = 16.dp,
    val verticalContentPadding: Dp = 8.dp,
    val horizontalContentPadding: Dp = 8.dp,
    val rowSpacing: Dp = 8.dp,
    val indent: Dp = 16.dp,
    val button: ButtonControlStyle = ButtonControlStyle(),
    val slider: SliderControlStyle = SliderControlStyle(),
    val color: ColorControlStyle = ColorControlStyle(),
    val toggle: ToggleControlStyle = ToggleControlStyle(),
    val char: CharControlStyle = CharControlStyle(),
    val textField: TextFieldControlStyle = TextFieldControlStyle(),
    val dropdown: DropdownControlStyle = DropdownControlStyle(),
    val expandableHeader: ExpandableHeaderStyle = ExpandableHeaderStyle(),
    val dynamicList: DynamicListControlStyle = DynamicListControlStyle(),
)

@Composable
fun Controls(
    controls: ImmutableList<Control>,
    modifier: Modifier = Modifier,
    controlsStyle: ControlsStyle = ControlsStyle(),
    name: String? = null,
    indent: Boolean = false,
    expandedInitial: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(vertical = controlsStyle.verticalContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(controlsStyle.spacing)
) {
    Column(
        verticalArrangement = verticalArrangement,
        modifier = modifier
            .padding(contentPadding)
    ) {
        var expanded by remember { mutableStateOf(expandedInitial) }
        name?.let {
            ExpandableHeader(
                name = it,
                expanded = expanded,
                onExpandedChange = { expanded = it },
                style = controlsStyle.expandableHeader,
            )
        }

        if (!expanded && name != null) return@Column

        for (control in controls) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (indent)
                            Modifier.padding(start = controlsStyle.indent)
                        else Modifier
                    )
            ) {
                when (control) {
                    is Control.Button -> ButtonControl(control = control, style = controlsStyle.button)
                    is Control.Color -> ColorControl(control = control, style = controlsStyle.color)
                    is Control.Slider -> SliderControl(control = control, style = controlsStyle.slider)
                    is Control.Dropdown<*> -> DropdownControlRow(control = control, style = controlsStyle.dropdown)
                    is Control.Toggle -> ToggleControlRow(control = control, style = controlsStyle.toggle)
                    is Control.CharField -> CharControl(control = control, style = controlsStyle.char)
                    is Control.TextField -> TextFieldControl(control = control, style = controlsStyle.textField)
                    is Control.DynamicList<*> -> DynamicListControl(control = control, controlsStyle = controlsStyle)
                    is Control.ControlColumn -> {
                        val controls by rememberUpdatedState(control.controls())
                        Controls(
                            controls = controls,
                            controlsStyle = controlsStyle,
                            name = control.name,
                            indent = control.indent,
                            expandedInitial = control.expandedInitial,
                            contentPadding = PaddingValues(vertical = controlsStyle.verticalContentPadding),
                        )
                    }
                    is Control.ControlRow -> {
                        val controls by rememberUpdatedState(control.controls())
                        ControlsRow(controls = controls, controlsStyle = controlsStyle)
                    }
                }
            }
        }
    }
}

@Composable
fun ControlsRow(
    controls: ImmutableList<Control>,
    modifier: Modifier = Modifier,
    controlsStyle: ControlsStyle = ControlsStyle(),
    equalWeight: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(controlsStyle.rowSpacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        for (control in controls) {
            Box(
                modifier = Modifier
                    .then(if (equalWeight) Modifier.weight(1f, fill = false) else Modifier)
            ) {
                when (control) {
                    is Control.Button -> ButtonControl(control = control, style = controlsStyle.button)
                    is Control.Color -> ColorControl(control = control, style = controlsStyle.color)
                    is Control.Slider -> SliderControl(control = control, style = controlsStyle.slider)
                    is Control.Dropdown<*> -> DropdownControl(control = control, style = controlsStyle.dropdown)
                    is Control.Toggle -> ToggleControl(control = control, style = controlsStyle.toggle)
                    is Control.CharField -> CharControl(control = control, style = controlsStyle.char)
                    is Control.TextField -> TextFieldControl(control = control, style = controlsStyle.textField)
                    is Control.DynamicList<*> -> DynamicListControl(control = control, controlsStyle = controlsStyle)
                    is Control.ControlColumn -> {
                        val controls by rememberUpdatedState(control.controls())
                        Controls(
                            controls = controls,
                            controlsStyle = controlsStyle,
                            name = control.name,
                            indent = control.indent,
                            expandedInitial = control.expandedInitial,
                            contentPadding = PaddingValues(horizontal = controlsStyle.horizontalContentPadding),
                        )
                    }
                    is Control.ControlRow -> {
                        val controls by rememberUpdatedState(control.controls())
                        Controls(controls = controls, controlsStyle = controlsStyle)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var expanded by remember { mutableStateOf(true) }
    Controls(
        controls = persistentListOf(
            Control.Slider(
                name = "Amount",
                value = { 0.5f },
                onValueChange = {},
            ),
            Control.Slider(
                name = "Amount 2",
                value = { 0.5f },
                onValueChange = {},
            )
        ),
        expandedInitial = expanded,
        name = "Sliders",
    )
}
