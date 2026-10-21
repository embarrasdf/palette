package com.embarrasdf.palette.theme.semantic.motion.infinite

/** Continuous, looping motion presets keyed by semantic [InfiniteMotionToken]. */
data class InfiniteMotionScheme(
    val loading: InfiniteEffect = InfiniteEffect.Spin(),
)

val PaletteInfiniteMotionScheme = InfiniteMotionScheme()

fun InfiniteMotionScheme.copy(
    token: InfiniteMotionToken,
    effect: InfiniteEffect,
) = this.copy(
    loading = if (token == InfiniteMotionToken.Loading) effect else this.loading,
)
