package com.embarrasdf.palette.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.demo.control.Control
import kotlinx.collections.immutable.PersistentList

data class DemoListStyle(
    val demoStyle: DemoStyle = DemoStyle(),
    val itemSpacing: Dp = 24.dp,
    val contentPadding: PaddingValues = PaddingValues(16.dp),
)

@Composable
fun <T> DemoList(
    items: List<T>,
    controls: PersistentList<Control>,
    modifier: Modifier = Modifier,
    style: DemoListStyle = DemoListStyle(),
    key: ((item: T) -> Any)? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(
        space = style.itemSpacing,
        alignment = Alignment.CenterVertically,
    ),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable DemoScope.(T) -> Unit,
) {
    Demo(
        controls = controls,
        style = style.demoStyle,
        modifier = modifier,
    ) {
        LazyColumn(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            contentPadding = style.contentPadding,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {
            items(
                items = items,
                key = key,
            ) { item ->
                content(item)
            }
        }
    }
}
