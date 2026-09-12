package com.embarrasdf.palette.components.demo.control

import androidx.compose.foundation.Indication
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.ChevronDirection
import com.embarrasdf.palette.components.core.ChevronIcon
import com.embarrasdf.palette.components.core.IconStyle
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle

data class ExpandableHeaderStyle(
    val headerStyle: TextStyle = TextStyle(),
    val borderColor: Color = Color.Unspecified,
    val spacing: Dp = 8.dp,
    val borderWidth: Dp = 1.dp,
    val labelPadding: PaddingValues = PaddingValues(4.dp),
    val chevronIconStyle: IconStyle = IconStyle(),
    val chevronPadding: PaddingValues = PaddingValues(0.dp),
    val indication: Indication? = null,
)

@Composable
fun ExpandableHeader(
    name: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    style: ExpandableHeaderStyle = ExpandableHeaderStyle(),
) {
    Surface(
        onClick = { onExpandedChange(!expanded) },
        style = SurfaceStyle(indication = style.indication),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(style.spacing),
            modifier = Modifier
                .height(IntrinsicSize.Min)
        ) {
            Text(
                text = name,
                style = style.headerStyle,
                modifier = Modifier
                    .border(style.borderWidth, style.borderColor)
                    .padding(style.labelPadding)
            )
            ChevronIcon(
                direction = if (expanded) ChevronDirection.Up else ChevronDirection.Down,
                style = style.chevronIconStyle,
                modifier = Modifier
                    .padding(style.chevronPadding)
            )
        }
    }
}
