package com.embarrasdf.palette.components.layout.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle

data class CatalogStyle(
    val itemSpacing: Dp = 16.dp,
    val itemStyle: ButtonStyle = ButtonStyle(),
    val itemTextStyle: TextStyle = TextStyle(),
)

@Composable
fun <T : CatalogItem> Catalog(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: CatalogStyle = CatalogStyle(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(
            space = style.itemSpacing,
            alignment = Alignment.CenterVertically,
        ),
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.title }
        ) { item ->
            Button(
                style = style.itemStyle,
                onClick = { onItemClick(item) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(item.title, style = style.itemTextStyle)
            }
        }
        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}


private enum class MainCatalogItem : CatalogItem {
    Components,
    Modifiers,
    ;

    override val title = this.name
}

@Preview
@Composable
private fun Preview() {
    Catalog(
        items = MainCatalogItem.entries.toList(),
        onItemClick = {}
    )
}
