package com.embarrasdf.palette.components.demo.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.primitive.FontFamily
import com.embarrasdf.palette.theme.primitive.FontStyle
import com.embarrasdf.palette.theme.primitive.FontWeight
import com.embarrasdf.palette.theme.primitive.toComposeFontFamily
import com.embarrasdf.palette.theme.primitive.toComposeFontStyle
import com.embarrasdf.palette.theme.primitive.toComposeFontWeight
import com.embarrasdf.palette.theme.primitive.toFontFamily
import com.embarrasdf.palette.theme.primitive.toFontStyle
import com.embarrasdf.palette.theme.primitive.toFontWeight
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.ui.text.TextStyle as ComposeTextStyle

val ComposeTextStyleDemoDefault = ComposeTextStyle(
    fontSize = 52.sp,
)

@Composable
fun rememberComposeTextStyleDemoState(
    composeTextStyleInitial: ComposeTextStyle = ComposeTextStyleDemoDefault,
) = rememberSaveable(saver = ComposeTextStyleDemoStateSaver) {
    ComposeTextStyleDemoState(
        composeTextStyleInitial = composeTextStyleInitial,
    )
}

@Stable
class ComposeTextStyleDemoState(
    composeTextStyleInitial: ComposeTextStyle = ComposeTextStyleDemoDefault,
) {
    var fontFamily by mutableStateOf(composeTextStyleInitial.fontFamily?.toFontFamily())
        internal set

    var fontWeight by mutableStateOf(composeTextStyleInitial.fontWeight?.toFontWeight())
        internal set

    var fontStyle by mutableStateOf(composeTextStyleInitial.fontStyle?.toFontStyle())
        internal set

    var fontSize by mutableStateOf(composeTextStyleInitial.fontSize.value.takeIf { it.isFinite() })
        internal set

    var lineHeight by mutableStateOf(composeTextStyleInitial.lineHeight.value.takeIf { it.isFinite() })
        internal set

    var letterSpacing by mutableStateOf(composeTextStyleInitial.letterSpacing.value.takeIf { it.isFinite() })
        internal set

    val composeTextStyle by derivedStateOf {
        ComposeTextStyle(
            fontFamily = fontFamily?.toComposeFontFamily(),
            fontWeight = fontWeight?.toComposeFontWeight(),
            fontStyle = fontStyle?.toComposeFontStyle(),
            fontSize = fontSize?.let { TextUnit(it, TextUnitType.Sp) } ?: TextUnit.Unspecified,
            lineHeight = lineHeight?.let { TextUnit(it, TextUnitType.Sp) } ?: TextUnit.Unspecified,
            letterSpacing = letterSpacing?.let { TextUnit(it, TextUnitType.Sp) } ?: TextUnit.Unspecified,
        )
    }
}

private const val fontFamilyKey = "fontFamily"
private const val fontWeightKey = "fontWeight"
private const val fontStyleKey = "fontStyle"
private const val fontSizeKey = "fontSize"
private const val lineHeightKey = "lineHeight"
private const val letterSpacingKey = "letterSpacing"

val ComposeTextStyleDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            fontFamilyKey to value.fontFamily?.name,
            fontWeightKey to value.fontWeight?.name,
            fontStyleKey to value.fontStyle?.name,
            fontSizeKey to value.fontSize,
            lineHeightKey to value.lineHeight,
            letterSpacingKey to value.letterSpacing,
        )
    },
    restore = { map ->
        ComposeTextStyleDemoState().apply {
            fontFamily = (map[fontFamilyKey] as? String)?.let { FontFamily.valueOf(it) }
            fontWeight = (map[fontWeightKey] as? String)?.let { FontWeight.valueOf(it) }
            fontStyle = (map[fontStyleKey] as? String)?.let { FontStyle.valueOf(it) }
            fontSize = map[fontSizeKey] as? Float
            lineHeight = map[lineHeightKey] as? Float
            letterSpacing = map[letterSpacingKey] as? Float
        }
    }
)

@Composable
fun rememberComposeTextStyleDemoControl(
    state: ComposeTextStyleDemoState,
) = remember(state) {
    ComposeTextStyleDemoControl(state = state)
}

@Stable
class ComposeTextStyleDemoControl(
    private val state: ComposeTextStyleDemoState,
) {
    val fontFamilyControl = enumControl(
        name = "Font family",
        values = { FontFamily.entries },
        selectedValue = { state.fontFamily ?: FontFamily.Default },
        onValueChange = { state.fontFamily = it }
    )

    val fontWeightControl = enumControl(
        name = "Font weight",
        values = { FontWeight.entries },
        selectedValue = { state.fontWeight ?: FontWeight.Normal },
        onValueChange = { state.fontWeight = it }
    )

    val fontStyleControl = enumControl(
        name = "Font style",
        values = { FontStyle.entries },
        selectedValue = { state.fontStyle ?: FontStyle.Normal },
        onValueChange = { state.fontStyle = it },
    )

    val fontSizeControl = Control.Slider(
        name = "Font size",
        value = { state.fontSize ?: 16f },
        onValueChange = { state.fontSize = it },
        valueRange = { 8f..200f },
    )

    val lineHeightControl = Control.Slider(
        name = "Line height",
        value = { state.lineHeight ?: 24f },
        onValueChange = { state.lineHeight = it },
        valueRange = { 8f..200f },
    )

    val letterSpacingControl = Control.Slider(
        name = "Letter spacing",
        value = { state.letterSpacing ?: 0f },
        onValueChange = { state.letterSpacing = it },
        valueRange = { -10f..10f },
    )

    val controls = persistentListOf(
        fontFamilyControl,
        fontWeightControl,
        fontStyleControl,
        fontSizeControl,
        lineHeightControl,
        letterSpacingControl,
    )

    fun updateTextStyle(style: ComposeTextStyle) {
        with(style) {
            state.fontFamily = fontFamily?.toFontFamily()
            state.fontWeight = fontWeight?.toFontWeight()
            state.fontStyle = fontStyle?.toFontStyle()
            state.fontSize = fontSize.value.takeIf { it.isFinite() }
            state.lineHeight = lineHeight.value.takeIf { it.isFinite() }
            state.letterSpacing = letterSpacing.value.takeIf { it.isFinite() }
        }
    }
}
