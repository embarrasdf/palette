package com.embarrasdf.palette.components.demo.control

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.menu.DropdownMenu
import com.embarrasdf.palette.components.menu.DropdownMenuItem
import com.embarrasdf.palette.components.menu.DropdownMenuStyle
import kotlinx.collections.immutable.toPersistentList

data class DropdownControlStyle(
    val labelStyle: TextStyle = TextStyle(),
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val menuStyle: DropdownMenuStyle = DropdownMenuStyle(),
    val labelSpacing: Dp = 8.dp,
    val rowSpacing: Dp = 16.dp,
)

@Composable
fun <T> DropdownControl(
    control: Control.Dropdown<T>,
    modifier: Modifier = Modifier,
    style: DropdownControlStyle = DropdownControlStyle(),
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val values by rememberUpdatedState(control.values())
    val selectedIndex by rememberUpdatedState(control.selectedIndex())
    val selectedValue by remember(values, selectedIndex) {
        derivedStateOf { values[selectedIndex] }
    }

    Column(modifier = modifier) {
        if (control.includeLabel) {
            Text(control.name, style = style.labelStyle)
            Spacer(modifier = Modifier.height(style.labelSpacing))
        }

        DropdownMenuControlButton(
            selectedValue = selectedValue,
            onClick = { isMenuExpanded = true },
            style = style,
        )

        DropdownControlMenu(
            control = control,
            isMenuExpanded = isMenuExpanded,
            onMenuDismissRequest = { isMenuExpanded = false },
            style = style,
        )
    }
}

@Composable
fun <T> DropdownControlRow(
    control: Control.Dropdown<T>,
    modifier: Modifier = Modifier,
    style: DropdownControlStyle = DropdownControlStyle(),
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val values by rememberUpdatedState(control.values())
    val selectedIndex by rememberUpdatedState(control.selectedIndex())
    val selectedValue by remember(values, selectedIndex) {
        derivedStateOf { values[selectedIndex] }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (control.includeLabel) {
            Text(
                text = control.name,
                style = style.labelStyle,
                softWrap = true,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(modifier = Modifier.width(style.rowSpacing))
        }

        DropdownMenuControlButton(
            selectedValue = selectedValue,
            onClick = { isMenuExpanded = true },
            style = style,
            modifier = Modifier.weight(1f, fill = false)
        )

        DropdownControlMenu(
            control = control,
            isMenuExpanded = isMenuExpanded,
            onMenuDismissRequest = { isMenuExpanded = false },
            style = style,
        )
    }
}

@Composable
private fun <T> DropdownMenuControlButton(
    selectedValue: Control.Dropdown.DropdownItem<T>,
    onClick: () -> Unit,
    style: DropdownControlStyle,
    modifier: Modifier = Modifier,
) {
    Button(
        style = style.buttonStyle,
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(text = selectedValue.name, style = style.labelStyle)
    }
}

@Composable
private fun <T> DropdownControlMenu(
    control: Control.Dropdown<T>,
    isMenuExpanded: Boolean,
    onMenuDismissRequest: () -> Unit,
    style: DropdownControlStyle,
) {
    val values by rememberUpdatedState(control.values())
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = onMenuDismissRequest,
        style = style.menuStyle,
    ) {
        values.forEachIndexed { index, value ->
            DropdownMenuItem(
                text = { Text(text = value.name, style = style.labelStyle) },
                onClick = {
                    onMenuDismissRequest()
                    control.onValueChange(index)
                },
                style = style.menuStyle.itemStyle,
            )
        }
    }
}

@Preview
@Composable
private fun DropdownControlPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    val control by remember {
        mutableStateOf(
            Control.Dropdown(
                name = "Options",
                values = {
                    listOf("A", "B", "C").map {
                        Control.Dropdown.DropdownItem(
                            name = it,
                            value = it
                        )
                    }.toPersistentList()
                },
                selectedIndex = { selectedIndex },
                onValueChange = { selectedIndex = it }
            )
        )
    }
    DropdownControl(control = control)
}

@Preview
@Composable
private fun DropdownControlRowPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    val control by remember {
        mutableStateOf(
            Control.Dropdown(
                name = "Options",
                values = {
                    listOf("A", "B", "C").map {
                        Control.Dropdown.DropdownItem(
                            name = it,
                            value = it
                        )
                    }.toPersistentList()
                },
                selectedIndex = { selectedIndex },
                onValueChange = { selectedIndex = it }
            )
        )
    }
    DropdownControlRow(control = control)
}
