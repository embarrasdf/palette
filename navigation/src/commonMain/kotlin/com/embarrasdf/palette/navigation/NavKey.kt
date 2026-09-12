package com.embarrasdf.palette.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey as Navigation3NavKey

@Stable
interface NavKey : Navigation3NavKey {
    companion object
    val pathSegment: PathSegment
}
