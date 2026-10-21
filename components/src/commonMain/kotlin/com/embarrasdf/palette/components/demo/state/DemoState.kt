package com.embarrasdf.palette.components.demo.state

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Base class for demo state that saves itself.
 *
 * Declare each property once with [saveable], [textField] or [child]; it's saved under its
 * property name. Restoring builds a fresh instance with the factory passed to [demoStateSaver]
 * and applies the saved values on top, so a missing or unreadable value keeps its default
 * instead of discarding the whole state. Values that shouldn't be saved are plain
 * `mutableStateOf` properties.
 */
@Stable
open class DemoState protected constructor() {
    internal val entries = LinkedHashMap<String, DemoStateEntry>()

    protected fun saveable(initial: Float): SaveableProperty<Float> =
        SaveableProperty(initial = initial, saver = DemoSavers.Float)

    protected fun saveable(initial: Int): SaveableProperty<Int> =
        SaveableProperty(initial = initial, saver = DemoSavers.Int)

    protected fun saveable(initial: Boolean): SaveableProperty<Boolean> =
        SaveableProperty(initial = initial, saver = DemoSavers.Boolean)

    protected fun saveable(initial: String): SaveableProperty<String> =
        SaveableProperty(initial = initial, saver = DemoSavers.String)

    protected fun saveable(initial: Dp): SaveableProperty<Dp> =
        SaveableProperty(initial = initial, saver = DemoSavers.Dp)

    protected fun saveable(initial: Color): SaveableProperty<Color> =
        SaveableProperty(initial = initial, saver = DemoSavers.Color)

    /** Saved by name; a name that no longer exists restores the default. */
    protected inline fun <reified E : Enum<E>> saveable(initial: E): SaveableProperty<E> =
        SaveableProperty(initial = initial, saver = DemoSavers.enum<E>())

    /** Saves the value with [saver]. A value that saves as `null` restores the default. */
    protected fun <T> saveable(
        initial: T,
        saver: Saver<T, out Any>,
    ): SaveableProperty<T> = SaveableProperty(initial = initial, saver = saver)

    /** A [TextFieldState] restored in place, so anything sharing it keeps the same instance. */
    protected fun textField(initial: String = ""): TextFieldProperty = TextFieldProperty(TextFieldState(initial))

    /** Nested demo state, saved with its parent and restored in place. */
    protected fun <S : DemoState> child(state: S): ChildProperty<S> = ChildProperty(state)
}

internal fun DemoState.register(
    name: String,
    entry: DemoStateEntry,
) {
    require(name !in entries) { "Duplicate demo state property \"$name\"" }
    entries[name] = entry
}

internal fun DemoState.saveEntries(scope: SaverScope): Map<String, Any?> = entries.mapValues { (_, entry) ->
    entry.save(scope)
}

internal fun DemoState.restoreEntries(saved: Map<String, Any?>) {
    for ((name, entry) in entries) {
        val value = saved[name] ?: continue
        // One unreadable value (e.g. after a type change) shouldn't reset the others.
        runCatching { entry.restore(value) }
    }
}

/** Saves a [DemoState] subclass. [factory] builds the instance that saved values are restored onto. */
fun <S : DemoState> demoStateSaver(factory: () -> S): Saver<S, Any> = mapSaver(
    save = { state -> state.saveEntries(scope = this) },
    restore = { saved -> factory().apply { restoreEntries(saved) } },
)

/** Remembers a [DemoState] across recomposition, configuration changes and process death. */
@Composable
fun <S : DemoState> rememberDemoState(
    vararg inputs: Any?,
    factory: () -> S,
): S = rememberSaveable(
    inputs = inputs,
    saver = demoStateSaver(factory),
    init = factory,
)

internal interface DemoStateEntry {
    fun save(scope: SaverScope): Any?

    fun restore(value: Any)
}

class SaveableProperty<T>(
    private val initial: T,
    private val saver: Saver<T, out Any>,
) {
    operator fun provideDelegate(
        thisRef: DemoState,
        property: KProperty<*>,
    ): ReadWriteProperty<DemoState, T> {
        @Suppress("UNCHECKED_CAST")
        val entry = SaveableEntry(initial = initial, saver = saver as Saver<T, Any>)
        thisRef.register(name = property.name, entry = entry)
        return entry
    }
}

private class SaveableEntry<T>(
    initial: T,
    private val saver: Saver<T, Any>,
) : DemoStateEntry, ReadWriteProperty<DemoState, T> {
    private val state = mutableStateOf(initial)

    override fun getValue(
        thisRef: DemoState,
        property: KProperty<*>,
    ): T = state.value

    override fun setValue(
        thisRef: DemoState,
        property: KProperty<*>,
        value: T,
    ) {
        state.value = value
    }

    override fun save(scope: SaverScope): Any? = with(saver) { scope.save(state.value) }

    override fun restore(value: Any) {
        saver.restore(value)?.let { state.value = it }
    }
}

class TextFieldProperty internal constructor(
    private val textFieldState: TextFieldState,
) {
    operator fun provideDelegate(
        thisRef: DemoState,
        property: KProperty<*>,
    ): ReadOnlyProperty<DemoState, TextFieldState> {
        thisRef.register(name = property.name, entry = TextFieldEntry(textFieldState))
        return ReadOnlyProperty { _, _ -> textFieldState }
    }
}

private class TextFieldEntry(
    private val textFieldState: TextFieldState,
) : DemoStateEntry {
    override fun save(scope: SaverScope): Any? = with(TextFieldState.Saver) { scope.save(textFieldState) }

    override fun restore(value: Any) {
        val restored = TextFieldState.Saver.restore(value) ?: return
        textFieldState.edit {
            replace(start = 0, end = length, text = restored.text)
            selection = restored.selection
        }
    }
}

class ChildProperty<S : DemoState> internal constructor(
    private val state: S,
) {
    operator fun provideDelegate(
        thisRef: DemoState,
        property: KProperty<*>,
    ): ReadOnlyProperty<DemoState, S> {
        thisRef.register(name = property.name, entry = ChildEntry(state))
        return ReadOnlyProperty { _, _ -> state }
    }
}

private class ChildEntry(
    private val state: DemoState,
) : DemoStateEntry {
    override fun save(scope: SaverScope): Any? = with(EntriesSaver) { scope.save(state.saveEntries(scope)) }

    override fun restore(value: Any) {
        EntriesSaver.restore(value)?.let(state::restoreEntries)
    }
}

// Flattens a child's entries into a list, which the platform can save.
private val EntriesSaver = mapSaver<Map<String, Any?>>(
    save = { it },
    restore = { it },
)
