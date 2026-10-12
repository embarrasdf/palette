package com.embarrasdf.palette.components.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
@ReadOnlyComposable
fun WindowInsets.horizontalPaddingValues(): PaddingValues = this.asPaddingValues().calculateHorizontalPaddingValues()
