package com.embarrasdf.palette.theme.components.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.motion.toContentTransform
import androidx.navigation3.ui.NavDisplay as Navigation3NavDisplay

/**
 * A [Navigation3NavDisplay] whose transitions default to the theme's [MotionScheme][com.embarrasdf.palette.theme.semantic.motion.MotionScheme]:
 * the `enter` token drives forward navigation, `exit` drives pops, and `predictiveExit` drives
 * predictive back. Any spec can still be overridden per call site.
 */
@Composable
fun <T : Any> NavDisplay(
    backStack: List<T>,
    entryProvider: (T) -> NavEntry<T>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    onBack: () -> Unit = {},
    sizeTransform: SizeTransform? = null,
    transitionSpec: AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform =
        rememberEnterTransitionSpec(),
    popTransitionSpec: AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform =
        rememberExitTransitionSpec(),
    predictivePopTransitionSpec: AnimatedContentTransitionScope<Scene<T>>.(Int) -> ContentTransform =
        rememberPredictiveExitTransitionSpec(),
) {
    Navigation3NavDisplay(
        backStack = backStack,
        modifier = modifier,
        contentAlignment = contentAlignment,
        onBack = onBack,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryProvider = entryProvider,
    )
}

@Composable
private fun <T : Any> rememberEnterTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform {
    val motion = PaletteTheme.semantic.motion
    val animation = PaletteTheme.semantic.animation
    val easings = PaletteTheme.primitive.easing
    return { motion.enter.toContentTransform(animation, easings, forward = true) }
}

@Composable
private fun <T : Any> rememberExitTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform {
    val motion = PaletteTheme.semantic.motion
    val animation = PaletteTheme.semantic.animation
    val easings = PaletteTheme.primitive.easing
    return { motion.exit.toContentTransform(animation, easings, forward = false) }
}

@Composable
private fun <T : Any> rememberPredictiveExitTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(Int) -> ContentTransform {
    val motion = PaletteTheme.semantic.motion
    val animation = PaletteTheme.semantic.animation
    val easings = PaletteTheme.primitive.easing
    return { motion.predictiveExit.toContentTransform(animation, easings, forward = false) }
}
