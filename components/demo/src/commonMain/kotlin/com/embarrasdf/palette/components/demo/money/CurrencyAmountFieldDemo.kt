package com.embarrasdf.palette.components.demo.money

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.BoxWithLabel
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.paddingValuesControls
import com.embarrasdf.palette.components.money.CurrencyAmountField
import com.embarrasdf.palette.formats.money.MoneyFormat
import com.embarrasdf.palette.formats.money.format
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CurrencyAmountFieldDemo(
    moneyFormat: MoneyFormat = PaletteTheme.semantic.format.moneyFormats.default,
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = rememberTextFieldState(),
) {
    val text by snapshotFlow { textFieldState.text.toString() }
        .collectAsState(initial = textFieldState.text.toString())

    val base = PaletteTheme.component.money.currencyAmountField
    var padding by remember { mutableStateOf(base.padding) }
    var spacing by remember { mutableStateOf(base.spacing) }

    val controls = persistentListOf(
        Control.ControlColumn(
            name = "Style",
            indent = true,
            expandedInitial = true,
            controls = {
                persistentListOf(
                    paddingValuesControls(
                        name = "Padding",
                        value = { padding },
                        onValueChange = { padding = it },
                    ),
                    Control.Slider(
                        name = "Spacing",
                        value = { spacing.value },
                        onValueChange = { spacing = it.dp },
                        valueRange = { 0f..48f },
                    ),
                )
            },
        ),
    )

    Demo(
        controls = controls,
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.medium, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max)
                .align(Alignment.Center),
        ) {
            BoxWithLabel(
                label = "Raw",
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = text,
                    style = PaletteTheme.component.core.text.headline,
                )
            }
            BoxWithLabel(
                label = "Formatted",
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = moneyFormat.format(text),
                    style = PaletteTheme.component.core.text.headline,
                )
            }
            CurrencyAmountField(
                style = base.copy(
                    padding = padding,
                    spacing = spacing,
                ),
                moneyFormat = PaletteTheme.semantic.format.moneyFormats.default,
                textFieldState = textFieldState,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val textFieldState = rememberTextFieldState(initialText = "123.45")
    CurrencyAmountFieldDemo(textFieldState = textFieldState)
}
