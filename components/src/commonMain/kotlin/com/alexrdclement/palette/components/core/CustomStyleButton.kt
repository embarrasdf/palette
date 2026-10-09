package com.alexrdclement.palette.components.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.CommonStyle
import androidx.compose.foundation.style.CommonStyleScope
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.getOrElse
import androidx.compose.foundation.style.StyleResolver
import androidx.compose.foundation.style.pressed
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.stylePropertyOf
import androidx.compose.foundation.style.styleResolver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * PROTOTYPE / SPIKE — a fully custom styling solution on the Compose 1.13 API,
 * deliberately NOT using `styleable`.
 *
 * This defines its OWN animatable style properties ([stylePropertyOf]), expresses
 * a component style as a [CommonStyle] that `provide`s those properties (with a
 * state-driven `pressed` override), resolves them with a [StyleResolver] + state,
 * and applies the resolved values with plain modifiers it controls
 * (clip + drawBehind). Nothing here depends on what `styleable` does or doesn't
 * support — palette owns the property set and the application.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
object PaletteButtonProperties {
    val ContainerColor = stylePropertyOf(
        name = "palette.button.containerColor",
        interpolate = { from, to, fraction -> lerp(from, to, fraction) },
    ) { Color.Unspecified }

    val CornerRadius = stylePropertyOf(
        name = "palette.button.cornerRadius",
        interpolate = { from, to, fraction -> androidx.compose.ui.unit.lerp(from, to, fraction) },
    ) { 0.dp }
}

/** Build a custom button style from palette tokens — passed as an argument, as today. */
@OptIn(ExperimentalFoundationStyleApi::class)
fun paletteButtonStyle(
    containerColor: Color,
    cornerRadius: Dp,
    pressedContainerColor: Color = containerColor,
): CommonStyle = object : CommonStyle {
    override fun CommonStyleScope.applyStyle() {
        PaletteButtonProperties.ContainerColor.provide(containerColor)
        PaletteButtonProperties.CornerRadius.provide(cornerRadius)
        pressed {
            PaletteButtonProperties.ContainerColor.provide(pressedContainerColor)
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun CustomStyleButton(
    onClick: () -> Unit,
    style: CommonStyle,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val styleState = rememberUpdatedStyleState(interactionSource) { it.isEnabled = enabled }
    val resolver = remember(style, styleState) { StyleResolver(style, styleState) }

    Row(
        modifier = modifier
            .semantics { role = Role.Button }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .styleResolver(resolver)
            .clip(RoundedCornerShape(resolver.resolve { getOrElse(PaletteButtonProperties.CornerRadius) { 0.dp } }))
            .drawBehind {
                val color = resolver.resolve { getOrElse(PaletteButtonProperties.ContainerColor) { Color.Unspecified } }
                val radius = resolver.resolve { getOrElse(PaletteButtonProperties.CornerRadius) { 0.dp } }
                drawRoundRect(
                    color = color,
                    cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
                )
            }
            .defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = ButtonDefaults.MinHeight,
            )
            .padding(ButtonDefaults.ContentPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}
