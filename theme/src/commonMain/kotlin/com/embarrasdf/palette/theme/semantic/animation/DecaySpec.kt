package com.embarrasdf.palette.theme.semantic.animation

/**
 * A velocity-based, *target-less* animation: a value coasts from an initial velocity to rest,
 * wherever momentum carries it (Compose's `DecayAnimationSpec`). This is where friction lives, and
 * it is used for flings and gesture releases.
 *
 * It is a sibling to [AnimationSpec], not a member of it: with no target it cannot animate *to* a
 * known end state, so it can never drive a transition. Kept as vocabulary for future fling/gesture
 * work; there are no consumers yet.
 */
sealed interface DecaySpec {
    data class Exponential(val friction: Float = 1f) : DecaySpec
    data object Spline : DecaySpec
}
