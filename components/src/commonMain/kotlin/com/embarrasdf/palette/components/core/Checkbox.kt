package com.embarrasdf.palette.components.core

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.embarrasdf.palette.components.preview.BoolPreviewParameterProvider

data class CheckboxStyle(
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val textStyle: TextStyle = TextStyle(),
)

@Composable
fun Checkbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    style: CheckboxStyle = CheckboxStyle(),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Button(
        style = style.buttonStyle,
        onClick = { onCheckedChange(!isChecked) },
        modifier = modifier.semantics(mergeDescendants = true) {
            role = Role.Checkbox
        },
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Text(
            text = if (isChecked) "☑︎" else "☐",
            style = style.textStyle,
        )
    }
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(BoolPreviewParameterProvider::class) isChecked: Boolean,
) {
    Surface {
        var isChecked by remember { mutableStateOf(isChecked) }

        Checkbox(
            isChecked = isChecked,
            onCheckedChange = { isChecked = !it },
        )
    }
}
