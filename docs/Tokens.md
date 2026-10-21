# Design tokens

Requirements for adding and organizing theme tokens, plus a reference for the token model that
`PaletteTheme` exposes. The `MUST`/`SHOULD`/`MAY` items are rules a new token is expected to follow;
the surrounding prose explains the model those rules build on. Component-tier styling has its own
requirements in **[Components.md](Components.md)** — this document covers the primitive/semantic model
those build on and cross-links rather than repeats the component rules.

## Tiers

Tokens are organized into three tiers, each building on the one before:

- **Primitive** — unopinionated, context-free raw values (a font family, a shape, an indication
  effect). Primitives carry no design intent; they are the catalog a theme chooses from.
- **Semantic** — design-intent tokens that give primitives meaning in context (`ColorToken.Primary`,
  `TypographyToken.Display`, `ShapeToken.Surface`). A semantic token selects a primitive (or, for
  color, holds the values directly) and is what app code and component styles reference.
- **Component** — per-component styling assembled from semantic tokens (`ButtonStyleTokenSet`,
  `TextStyleTokenSet`), resolving to the headless `*Style` a component consumes.

- A new token MUST be placed in the tier that matches its role, by this test: a **capability** is
  primitive, a **choice** is semantic, a **component's assembled styling** is component-tier.
  "ColorSplit is an available effect" is primitive; "interactions use ColorSplit" is semantic; "the
  primary button uses these tokens" is component-tier.
- Tokens MUST depend only downward — component references semantic, semantic references primitive. A
  primitive MUST NOT reference a semantic or component token.

## Accessors

Each tier is reachable from `PaletteTheme`, and each has one editable holder type provided through one
composition local:

| Tier | Accessor | Holder | Local |
| --- | --- | --- | --- |
| Primitive | `PaletteTheme.primitive` | `PrimitiveTokens` | `LocalPrimitiveTokens` |
| Semantic | `PaletteTheme.semantic` | `SemanticTokens` | `LocalSemanticTokens` (+ `LocalIsDarkMode`) |
| Component | `PaletteTheme.component` | `ComponentTokens` | `LocalComponentTokens` |

- Each tier MUST expose exactly one holder type, provided through exactly one composition local, and be
  reachable via `PaletteTheme.<tier>`. A new token belongs on its tier's existing holder — it MUST NOT
  introduce a second holder or a per-value composition local.

`PaletteTheme` provides these locals from the tokens passed to it:

```kotlin
PaletteTheme(
    primitive: PrimitiveTokens = PrimitiveTokens(),
    semantic: SemanticTokens = SemanticTokens(),
    component: ComponentTokens = ComponentTokens(),
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
)
```

## Primitive tier

Primitives are stored on `PrimitiveTokens` as maps from a primitive token enum to its resolved value,
exposed through `PaletteTheme.primitive`:

- `PaletteTheme.primitive.fontFamily` — `Map<FontFamily, ComposeFontFamily>`
- `PaletteTheme.primitive.fontWeight` — `Map<FontWeight, ComposeFontWeight>`
- `PaletteTheme.primitive.fontStyle` — `Map<FontStyle, ComposeFontStyle>`
- `PaletteTheme.primitive.shape` — `Map<ShapePrimitiveToken, Shape>`
- `PaletteTheme.primitive.indication` — `Map<IndicationPrimitiveToken, IndicationTokenSet>`
- `PaletteTheme.primitive.easing` — `Map<EasingPrimitiveToken, EasingCurve>`

The map form keeps primitives editable (e.g. the `RoundRect` shape's corner radius, or an indication's
amount and colour mode) and in one place. Requirements for a new primitive family:

- A primitive value MUST be unopinionated — a raw capability with no design intent. Anything that
  encodes a *choice* belongs in the semantic tier.
- A primitive family MUST be exposed as a `Map<*PrimitiveToken, Value>` on `PrimitiveTokens`,
  populated from the token enum (`entries.associateWith { … }`).
- A primitive token enum MUST either convert itself to its value (`FontFamily.Monospace
  .toComposeFontFamily()`) or carry a `default` (`ShapePrimitiveToken.RoundRect.default`,
  `IndicationPrimitiveToken.ColorSplit.default`).
- A primitive whose value bundles parameters (like an indication's amount + colour mode) SHOULD use a
  `*TokenSet` that builds the value via a resolver (see [Token sets](#token-sets)); simple primitives
  (shape, font) resolve directly.

## Semantic tier

Semantic tokens are the editable inputs on `SemanticTokens`; `PaletteTheme.semantic.<family>` (color,
typography, shape, dimension, interaction, format, …) exposes the value components consume, resolving
where needed. Most families follow one shape: a `*Token` enum selects a primitive, and the accessor
resolves that selection through the tier below. A few families carry extra behavior worth calling out:

- **Color** holds its `light`/`dark` `ColorScheme`s directly (there is no color primitive tier), and
  `PaletteTheme.semantic.color` resolves the active one on read from `LocalIsDarkMode` — dark mode is
  runtime state, not a token.
- **Typography** resolves a whole ramp: each `TypographyToken` selects primitive
  `FontFamily`/`FontWeight`/`FontStyle` tokens plus its own size/line-height/letter-spacing, so its
  accessor memoizes the resolved `Typography`.
- **Interaction** additionally exposes `PaletteTheme.semantic.indication`, a convenience that resolves
  the default interaction token to its `Indication` for the many component styles that just want "the"
  indication.
- **Dimension** groups the size families under `PaletteTheme.semantic.dimension`, split by the *role*
  a value plays:
  - `.spacing` — space **between** items (gaps in a `Row`/`Column`, item/row/content spacing). The
    `Spacing` scale is a set of `Dp` steps (`none`/`xs`/`small`/`medium`/`large`) selected by
    `SpacingToken`; `none` (= `0.dp`) is a real step so a zero gap or a zeroed padding edge is a token,
    not a literal.
  - `.padding` — space **inside** a component (internal insets). A `PaddingScheme` of named
    `PaddingValuesToken`s that resolve to a `PaddingValuesTokenSet` (a `SpacingToken` per edge) and then
    to `PaddingValues` through the spacing scale — padding never embeds raw `Dp`s, it selects spacing
    tokens per edge.
  - A `PaddingValuesToken` exists **only for an inset whose edges differ** — asymmetric (`Wide` =
    `Large`/`Small`/`Large`/`Small`, wider horizontally than vertically) or directional (some edges
    `None`). A **uniform** inset carries no per-edge information, so it is expressed straight from a
    single spacing step (`PaddingValues(dimension.spacing.medium)`), not a token.
  - `.size` — how big an element **is** (icon/glyph sizes, min touch target). Distinct from spacing
    (*between*) and padding (*inside*). Unlike spacing, `SizeToken` is named by **role** rather than
    magnitude (like `ColorToken`, not `SpacingToken`) — `TouchTargetMin`, `IconSmall`, … — because
    sizes serve distinct purposes at very different scales, and a single small/medium/large ramp can
    neither convey purpose nor stretch (a large component isn't "xLarge"). Only tokens with a real
    consumer live here; add more as components need them. Components stay headless, so a `SizeToken`
    resolves to a `Dp` in the theme (`SizeToken.IconSmall.toSize()`) and is fed into the component's
    style, exactly like `ColorToken.toColor()`.

Requirements for a new semantic token:

- A semantic token MUST express design intent by **selecting** a primitive (`ShapeToken` → a
  `ShapePrimitiveToken`, `TypographyToken` → primitive font tokens) rather than embedding a raw value —
  except color, which holds its `light`/`dark` `ColorScheme`s directly because there is no color
  primitive tier.
- A semantic accessor MUST resolve on read from its tier local and MUST NOT cache a resolved value on
  the holder. Resolution that is more than a cheap branch SHOULD be memoized in the accessor (as
  typography's ramp is).
- Runtime state that isn't a token (dark mode) MUST NOT live on `SemanticTokens`; it is provided
  separately (`LocalIsDarkMode`) and folded in during resolution.

## Component tier

Component tokens assemble a component's style out of semantic tokens. `ComponentTokens` holds one
`Map<*Token, *TokenSet>` per token family (`text`, `button`, `surface`, `border`, …), and
`PaletteTheme.component` exposes the resolved styles through the `*Styles` facades that mirror the
component packages:

- `PaletteTheme.component.core.button.primary`, `…core.surface.default`, `…core.text.titleMedium`
- `PaletteTheme.component.media.playPauseButton`, `…color.colorDisplay`, …

The requirements for a component `*Style` getter and its `*TokenSet` (reuse existing theme values,
expose a resolver, `.copy()` only in the theme layer, …) live in
**[Components.md → Theme](Components.md#theme)** and
**[Components.md → Token sets](Components.md#token-sets)** — they are not repeated here.

## Token sets

All three tiers share one editing pattern: a token whose value is itself a bundle of other tokens or
parameters is backed by a `*TokenSet`. A `*TokenSet` holds the token selections and literals for one
token (e.g. `TypographyTokenSet` holds primitive `FontFamily`/`FontWeight`/`FontStyle` plus
size/line-height/letter-spacing; `IndicationTokenSet` holds an effect's parameters such as amount and
colour mode). Most are data classes; `IndicationTokenSet` is a sealed type with one variant per effect.

Requirements for a new `*TokenSet`:

- Each `*Token` enum entry MUST carry a `default: *TokenSet`.
- The current set for every token MUST be stored in the tier holder as a `Map<*Token, *TokenSet>` (one
  map per family), populated by `*Token.entries.associateWith { it.default }` — e.g.
  `SemanticTypography.tokens`, `ComponentTokens.button`.
- A `*TokenSet` MUST expose a resolver (`toComposeTextStyle`, `toComponentStyle`, `toIndication`, …)
  that turns it into the final value, looking any token selections up through the tier below.

This is why `PrimitiveTokens.indication`, `SemanticTypography.tokens`, `ShapeScheme`,
`InteractionScheme`, and `ComponentTokens` all read the same way: a map of tokens to selections or
parameters that resolves on read.

## Value hierarchies

Most token values are a flat choice — an enum selecting a primitive, or a `*TokenSet` bundle
([Token sets](#token-sets)). A few are richer: the value is itself a **sealed type hierarchy** whose
variants carry different capabilities. The animation specs are the worked example throughout
(`AnimationSpec`: `Tween`/`Snap`/`Spring` mechanisms, a `Repeat` combinator, and a `Finite` "settles"
marker), but the principles apply to any token whose value is a hierarchy rather than a flat
selection.

- **Model the concept; mirror roles, not names.** When the value maps onto a framework type, mirror
  the framework's *roles and relationships* but name them for Palette's model, collapsing or splitting
  where that reads more clearly. *Animation:* `DurationBased` mirrors Compose's
  `DurationBasedAnimationSpec`, but `Repeat` is one concept where Compose has two separate specs
  (`RepeatableSpec` / `InfiniteRepeatableSpec`).

- **Make a crosscutting capability a marker interface, not a branch.** A property consumers must be
  able to *require* SHOULD be a marker interface that cuts across the taxonomy — so unrelated variants
  can share it and a consumer can demand it in a signature — rather than a node in the tree.
  *Animation:* `Finite` marks the specs that settle; both mechanism families and the finite repeat
  implement it, and a transition asks for a `Finite`.

- **Prefer a sealed hierarchy over a generic when variants differ in capability.** Kotlin has no
  conditional conformance, so a generic (`Repeat<Count>`) cannot make one instantiation carry a marker
  while another does not. A sealed type lets each variant choose its own supertypes, so SHOULD be
  preferred when that distinction matters. *Animation:* `Repeat.Finite` is `Finite` (it settles after
  N iterations) while `Repeat.Infinite` is not — a generic could not express that split.

- **Group a combinator; don't flatten it among the leaves.** A variant that *wraps* another SHOULD be
  its own (sealed) type under its own name, not listed as a peer of the leaf variants it composes — the
  type should show the composition, and an editor SHOULD present it as an orthogonal axis (a toggle),
  not as a peer in a flat type picker. *Animation:* `Repeat` is distinct from `Tween`/`Snap`/`Spring`;
  its editor is a mechanism selector plus a "Repeat" toggle.

- **Keep markers honest; move an unexpressible guarantee to the boundary.** A marker MUST be applied
  only to variants that genuinely have the capability — never one that would lie. Where the value type
  cannot state a guarantee statically, enforce it at the consumption boundary instead, often the
  *token* that selects the value. *Animation:* a finite repeat is honestly `Finite`, and transitions
  reference `FiniteAnimationToken`, so a non-settling spec cannot be wired into a transition at all.

- **Keep a misfit out of the hierarchy.** A value that breaks the family's root contract belongs in a
  parallel type, not forced under a marker it does not satisfy. *Animation:* a velocity-driven,
  target-less decay would be a separate spec rather than an `AnimationSpec`.

## Editing tokens

The demo app's theme editor drives tokens through `ThemeController`, which wraps a `ThemeState`
(`primitive`, `semantic`, `component`, `isDarkMode`, and the resolved `colorScheme`).

- Edits MUST go through the tier's `ThemeController.update*` (or `setIsDarkMode`) and replace tokens by
  value; an editor MUST NOT mutate a resolved value:
  - `themeController.updatePrimitive { it.copy(shape = it.shape + (ShapePrimitiveToken.RoundRect to …)) }`
  - `themeController.updateSemantic { it.copy(interaction = it.interaction.copy(IndicationToken.Default, …)) }`
  - `themeController.updateComponent { it.copy(button = it.button + (token to …)) }`
  - `themeController.setIsDarkMode(true)`

`PaletteTheme` re-provides the tier locals from the updated `ThemeState`, and the `@Composable`
accessors re-resolve. The editor-screen requirements are in
**[Components.md → Demo app: theme editor](Components.md#demo-app-theme-editor)**.
