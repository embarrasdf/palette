package com.embarrasdf.palette.app.theme.semantic.motion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.shape.toComposeShape
import com.embarrasdf.palette.theme.semantic.animation.infinite.InfiniteAnimationToken
import com.embarrasdf.palette.theme.semantic.motion.infinite.InfiniteEffect
import com.embarrasdf.palette.theme.semantic.motion.infinite.InfiniteEffectType
import com.embarrasdf.palette.theme.semantic.motion.infinite.InfiniteMotionToken
import com.embarrasdf.palette.theme.semantic.motion.infinite.copy
import com.embarrasdf.palette.theme.semantic.motion.infinite.toEffect
import com.embarrasdf.palette.theme.semantic.motion.infinite.toModifier
import com.embarrasdf.palette.theme.semantic.motion.infinite.type
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun InfiniteScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberInfiniteScreenState()
    val control = rememberInfiniteScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Infinite",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        Demo(
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val effectModifier = state.token.toModifier()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(PaletteTheme.semantic.color.surface),
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .then(effectModifier)
                        .clip(ShapeToken.Secondary.toComposeShape())
                        .background(PaletteTheme.semantic.color.primary),
                )
            }
        }
    }
}

@Composable
fun rememberInfiniteScreenState(): InfiniteScreenState {
    return rememberSaveable(saver = InfiniteScreenStateSaver) {
        InfiniteScreenState(tokenInitial = InfiniteMotionToken.Loading)
    }
}

@Stable
class InfiniteScreenState(
    tokenInitial: InfiniteMotionToken,
) {
    var token by mutableStateOf(tokenInitial)
        internal set
}

private const val tokenKey = "token"

val InfiniteScreenStateSaver = mapSaverSafe(
    save = { state -> mapOf(tokenKey to state.token) },
    restore = { map -> InfiniteScreenState(tokenInitial = map[tokenKey] as InfiniteMotionToken) },
)

@Composable
fun rememberInfiniteScreenControl(
    state: InfiniteScreenState,
    themeController: ThemeController,
): InfiniteScreenControl {
    return remember(state, themeController) {
        InfiniteScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class InfiniteScreenControl(
    val state: InfiniteScreenState,
    val themeController: ThemeController,
) {
    private fun effect(): InfiniteEffect =
        state.token.toEffect(themeController.semantic.motion.infinite)

    private fun push(effect: InfiniteEffect) {
        themeController.updateSemantic {
            it.copy(
                motion = it.motion.copy(
                    infinite = it.motion.infinite.copy(token = state.token, effect = effect),
                ),
            )
        }
    }

    val tokenControl = enumControl(
        name = "Token",
        values = { InfiniteMotionToken.entries },
        selectedValue = { state.token },
        onValueChange = { state.token = it },
    )

    private val effectTypeControl = enumControl(
        name = "Effect",
        values = { InfiniteEffectType.entries },
        selectedValue = { effect().type() },
        onValueChange = { type -> push(effect().withType(type)) },
    )

    private val animationControl = enumControl(
        name = "Animation",
        values = { InfiniteAnimationToken.entries },
        selectedValue = { effect().animation },
        onValueChange = { token -> push(effect().withAnimation(token)) },
    )

    val controls: PersistentList<Control>
        get() = persistentListOf(
            tokenControl,
            effectTypeControl,
            animationControl,
        )
}

private fun InfiniteEffect.withType(type: InfiniteEffectType): InfiniteEffect = when (type) {
    InfiniteEffectType.Spin -> InfiniteEffect.Spin(animation = animation)
}

private fun InfiniteEffect.withAnimation(token: InfiniteAnimationToken): InfiniteEffect = when (this) {
    is InfiniteEffect.Spin -> copy(animation = token)
}
