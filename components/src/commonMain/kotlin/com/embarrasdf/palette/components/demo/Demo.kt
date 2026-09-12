package com.embarrasdf.palette.components.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.DividerStyle
import com.embarrasdf.palette.components.core.HorizontalDivider
import com.embarrasdf.palette.components.core.VerticalDivider
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.Controls
import com.embarrasdf.palette.components.demo.control.ControlsStyle
import com.embarrasdf.palette.components.util.horizontalPaddingValues
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class DemoStyle(
    val controlsStyle: ControlsStyle = ControlsStyle(),
    val dividerStyle: DividerStyle = DividerStyle(),
    val contentPadding: PaddingValues = PaddingValues(16.dp),
    val controlsMaxSize: Dp = 300.dp,
)

@Stable
interface DemoScope : BoxWithConstraintsScope

private class DemoScopeImpl(
    val boxWithConstraintsScope: BoxWithConstraintsScope,
) : DemoScope, BoxWithConstraintsScope by boxWithConstraintsScope

@Composable
fun Demo(
    modifier: Modifier = Modifier,
    style: DemoStyle = DemoStyle(),
    controls: PersistentList<Control> = persistentListOf(),
    content: @Composable DemoScope.() -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier
            .padding(WindowInsets.safeDrawing.horizontalPaddingValues()),
    ) {
        if (minWidth < minHeight) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val scope = DemoScopeImpl(this)
                    scope.content()
                }
                if (controls.isNotEmpty()) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), style = style.dividerStyle)
                    Controls(
                        controls = controls,
                        controlsStyle = style.controlsStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = style.controlsMaxSize)
                            .verticalScroll(rememberScrollState())
                            .padding(style.contentPadding)
                            .navigationBarsPadding(),
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    val scope = DemoScopeImpl(this@BoxWithConstraints)
                    scope.content()
                }
                if (controls.isNotEmpty()) {
                    VerticalDivider(modifier = Modifier.fillMaxHeight(), style = style.dividerStyle)
                    Controls(
                        controls = controls,
                        controlsStyle = style.controlsStyle,
                        modifier = Modifier
                            .fillMaxHeight()
                            .widthIn(max = style.controlsMaxSize)
                            .verticalScroll(rememberScrollState())
                            .padding(style.contentPadding)
                            .navigationBarsPadding(),
                    )
                }
            }
        }
    }
}
