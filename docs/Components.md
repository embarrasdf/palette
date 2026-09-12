# Components

Requirements for building components in Palette. This is a requirements document, not a tutorial:
each item is a rule that new components are expected to follow. Existing components are being
migrated toward these rules and do not all conform yet; where current coverage is partial (e.g.
screenshot tests, `:theme:components` wrappers) the relevant section says so.

## Summary

- Components MUST live in the `:components` module and be grouped into packages by use case
  (`core`, `layout`, `media`, `menu`, `money`, `navigation`, `auth`, `color`, `geometry`, `demo`, …).
  The `demo` package holds the reusable `Demo`/`DemoList` scaffolding components (distinct from the
  `:components:demo` module, which holds the per-component demos).
- Each component SHOULD exist in its own file. A file with tightly-coupled helpers (e.g. an icon
  drawn only for one button) MAY keep them together, but unrelated components MUST NOT share a file.
- A component file MUST be ordered:
  1. the `*Style` data class (named `<ComponentName>Style`),
  2. the component `@Composable`,
  3. any helper functions/private composables,
  4. a `@Preview` (private).
- Components MUST be headless with respect to the theme: the `:components` module MUST NOT depend on
  `:theme`. All visual values come in through the `*Style` parameter (see below); the theme supplies
  those values from the outside.
- Component source MUST live in `commonMain`. Platform-specific `expect`/`actual` is only warranted
  when an API genuinely has no common implementation; appearance and layout stay common.

### Component signatures

- Components MUST be **stateless / state-hoisted**: mutable state is exposed as a value plus an
  `on<Value>Change` callback (`isChecked: Boolean, onCheckedChange: (Boolean) -> Unit`); a component
  MUST NOT own state a caller cannot control.
- Parameters MUST follow the house order: required (hoisted) state first, then
  `modifier: Modifier = Modifier`, then `style: <ComponentName>Style = <ComponentName>Style()`, then
  remaining optional params (`enabled`, `interactionSource`, …), with a trailing `content` lambda
  last. `modifier` MUST be applied to the component's root once.
- Interactive components MUST take `enabled: Boolean = true` and MUST visibly dim when disabled via
  the style's `disabledContentAlpha` / `disabledContainerAlpha` (the theme supplies the non-`1f`
  values). A hand-built themed `*Style` that omits these alphas is a bug — the component will not dim.
- Components SHOULD carry the appropriate semantics (e.g. `Role.Checkbox`, `mergeDescendants`) so
  they are accessible and testable.
- A value that already lives on the `*Style` MUST NOT also be exposed as a standalone component
  parameter — it belongs in the style so the theme owns it.
- The private `@Preview` SHOULD render the component through the shared preview providers in the
  `preview` package (e.g. `@PreviewParameter(BoolPreviewParameterProvider::class)`) so the preview
  shows the component's real states.

## Style data classes

- Every visually-styled component MUST expose a `<ComponentName>Style` data class, and the component
  MUST take it as a `style: <ComponentName>Style = <ComponentName>Style()` parameter.
- Style data classes contain the values for visually styling the component (colors, shapes, borders,
  text styles, indication, spacing/padding, sizes).
- Style data classes MUST provide reasonable defaults:
  - `Dp` *defaults* SHOULD be in increments of `4.dp`. (This applies to the data-class defaults only;
    the theme layer MAY resolve finer values where a design calls for it.)
  - `Color` values MUST default to `Color.Unspecified`.
  - Defaults represent the *unstyled* component (transparent/neutral); the themed look is supplied
    by the theme layer, not by the defaults.
- Styles MUST contain child component styles where applicable rather than re-declaring the child's
  fields. For example, an `AuthButtonStyle` contains a `ButtonStyle`; a `SkipButtonStyle` contains a
  `ButtonStyle` and an `IconStyle`.
- Components MUST NOT contain hardcoded visual styling values. Every color, shape, size, padding, or
  spacing that affects appearance MUST be read from the `style`. (Layout values driven by runtime
  constraints — window insets, available space — are not "visual styling" and MAY remain parameters;
  everything a theme would want to control belongs in the style.)
- A component MUST render the `style` it is given. It MUST NOT construct or mutate a child style with
  `.copy()` to inject a value it decided itself — that value belongs in the style, supplied by the
  theme. (Building a framework value such as a `PaddingValues` from `style` fields is fine; overriding
  a themed sub-style is not.)
- **Content color belongs to the caller, not the container.** A component that renders caller-supplied
  content (a `Surface`, a `Button`) MUST NOT bake a content color into its own style; the caller sets
  the content color on the content it passes (as it must already know which surface it sits on). Only
  a component that owns its content end-to-end (e.g. an icon it draws itself) carries that color in
  its style.
- **Padding is styling — expose it only through `style`.** Spacing/padding that affects appearance
  MUST live on the `*Style` (as a `PaddingValues` / `Dp` field). Components MUST NOT add a dedicated
  `contentPadding`-style parameter that lets a caller bypass the themed value. (Params that carry a
  genuine runtime layout choice — window insets, a caller-chosen container size — are not styling and
  are covered by the runtime-constraints exception above.)
- **A configurable size is a `Sizing`.** When a component's own size can be more than one fixed value
  — an icon/glyph size, or a footprint that can be fixed, scaled, or filled — its `*Style` field
  SHOULD be a `Sizing` (`Fixed`/`Scale`/`Fill`), applied with `Modifier.size`/`height`/`width`, rather
  than a bare `Dp` — the same way multi-edge padding is a `PaddingValues`. A size that is always a
  single fixed value MAY stay a `Dp`.
- **Separate a design bound from a runtime target.** When a component has both a design-time cap and a
  runtime-driven size, the cap (`minContentSize`, `maxContentSize`) belongs on the `*Style` and the
  runtime target (`expandedContentSize`) is a parameter. Do not collapse the two.

## Theme

Component styles are resolved and exposed to app code through the theme, which is organized into
primitive, semantic, and component token tiers — see **[Tokens.md](Tokens.md)** for that model. This
section covers the requirements for exposing a component's style through it.

- Resolved component styles MUST be reachable via `PaletteTheme.component`, which returns the
  `Component` facade. `Component` exposes a chain of `val` accessors that mirror the component package
  structure, e.g. `PaletteTheme.component.core.button`,
  `PaletteTheme.component.media.playPauseButton`, `PaletteTheme.component.color.colorDisplay`.
- Each package's accessors live in a `*Styles` object in `:theme` (`CoreStyles`, `LayoutStyles`,
  `MediaStyles`, …) as `@Composable get()` properties that return a fully-resolved `*Style`.
- A component that has more than one themed variant exposes them as named sub-accessors rather than a
  single value: `PaletteTheme.component.core.surface.default`, `…core.button.primary` /
  `…core.button.secondary`. A component with a single themed style resolves directly
  (`PaletteTheme.component.core.slider`). Use the named variant where one exists.
- A `*Style` getter MUST reuse existing theme values wherever one applies rather than inventing a
  literal:
  - content on a `Surface` uses `PaletteTheme.semantic.color.onSurface`;
  - `Dp` values use `PaletteTheme.semantic.dimension.spacing.*` tokens; multi-edge padding uses
    `PaletteTheme.semantic.dimension.padding.*` / `PaddingValuesToken.*`;
  - shapes use `PaletteTheme.semantic.shape.*` / shape tokens; indication uses
    `PaletteTheme.semantic.indication`.
- `*Style` getters MAY compose sub-styles from other getters (e.g. `SkipButtonStyle.buttonStyle =
  CoreStyles.button.secondary`). `.copy()` to assemble a themed style **in the theme layer** is
  allowed — that is the theme being the source of truth. `.copy()` inside a component is not.

### Token sets

Component styles are edited token-by-token (so the theme editor can tweak them) via the token-set
pattern shared across the token tiers — see **[Tokens.md → Token sets](Tokens.md#token-sets)** for the
general mechanism. The component-tier requirements:

- A `*TokenSet` data class primarily contains other token values (e.g. `ButtonStyleTokenSet` holds a
  `containerColor: ColorToken`, `shape: ShapeToken`, `borderStyle: BorderStyleToken?`,
  `contentPadding: PaddingValuesTokenSet`).
- Each `*Token` enum entry carries a `default: *TokenSet`.
- The current token set for every token is stored in `ComponentTokens` as a `Map<*Token, *TokenSet>`
  (one map per token family), provided via `LocalComponentTokens` and edited through
  `ThemeController.updateComponent`.
- A `*TokenSet` MUST expose a resolver helper (e.g. `toComponentStyle()` / `toTextStyle()`) that
  turns it into the final component `*Style`. The package `*Styles` getters call these resolvers.

## `:theme:components` wrappers

The `:theme:components` module contains theme-aware wrappers over the headless components. A wrapper
exists only to save callers from passing `style = PaletteTheme.component.…` at every call site.

- Not every component has a wrapper. A wrapper is warranted only when a component has a single
  dominant themed default that most callers want. Components whose style is a per-call choice
  (`Text`, `Button`, `AuthButton` — variant/named-style driven, where the caller selects the style)
  do not get a wrapper; callers pick the style explicitly. The current wrappers cover the high-traffic
  single-default components (`Surface`, `Scaffold`, `TopBar`, `BoxWithLabel`, `Demo`, `DemoList`,
  `BackNavigationButton`, …) and more MAY be added as call-site pressure warrants.
- When a wrapper does exist, it MUST mirror its base component's package and file structure (a base
  `components/core/Surface.kt` has a wrapper `theme/components/core/Surface.kt`), and MUST live in
  package `com.embarrasdf.palette.theme.components.*`.
- A wrapper MUST expose the **same API** as the base component (same name, same parameters), except
  that its `style` parameter defaults to the matching themed value, e.g.
  `style: SurfaceStyle = PaletteTheme.component.core.surface.default`.
- A wrapper MUST do nothing but default the style and delegate to the base component. It MUST NOT add
  behavior. Callers that want the unstyled/headless component use the base `:components` version;
  callers that want the themed default use the wrapper.

## Component demos

Every **new** component MUST have a demo in the `:components:demo` module. Demos are **required** for
new work (most existing components already have one) and have their own structural requirements — see
**[Demos.md](Demos.md)**.

## Testing

Components SHOULD have a Paparazzi screenshot test, and new components with non-trivial appearance
SHOULD add one. Coverage today is partial, so this is a target to grow toward, not a universally-met
bar.

- Tests live in the `:components:android-test` module, in a package that mirrors the component's
  package, in a `<ComponentName>Test.kt` file.
- A test SHOULD render the component inside `PaletteTheme` with its themed style
  (`style = PaletteTheme.component.<…>`) via the shared `PaparazziTestRule`.
- A test SHOULD cover the states that change appearance. Sweep them with `TestParameterInjector` —
  `@TestParameter isEnabled: Boolean`, an enum/anchor `valuesProvider`, etc. — rather than writing one
  `@Test` per combination. For an interactive component, snapshotting both enabled and disabled is the
  way to guard the disabled dimming.
- Golden images are committed under `src/test/snapshots/images/`. Where a test exists, those goldens
  are what CI verifies (`./gradlew verifyPaparazziDebug`), so a stale golden fails the build.
  Regenerate them with `./gradlew recordPaparazziDebug` and commit the result whenever the intended
  appearance changes.

## Demo app: component demo entry

Each component group is wired into the demo app under
`app/.../demo/components/<group>/`.

### Screen

- A `<Group>ComponentScreen(component, onNavigateUp, onThemeClick)` composable MUST render a
  `Scaffold` with a `DemoTopBar` (title from the component, `onNavigateUp`, `onThemeClick`) and, in
  the content slot, a `when (component)` that dispatches to each `<Component>Demo(modifier =
  Modifier.padding(innerPadding))`.

### Navigation

Under `app/.../demo/components/<group>/navigation/`:

- A `<Group>Component : CatalogItem` enum MUST list the components in the group (its `title` drives
  the catalog entry).
- `<Group>ComponentsRoutes.kt` MUST define:
  - a sealed `<Group>ComponentsRoute : NavKey`;
  - a `<Group>ComponentsGraph : NavGraphRoute` (with a `pathSegment`);
  - a `<Group>ComponentCatalogRoute`;
  - a per-component `<Group>ComponentRoute : EnumNavKey<<Group>Component>`.
- `<Group>ComponentsNav.kt` MUST define:
  - `NavGraphBuilder.<group>ComponentsNavGraph()` — a `navGraph(root, start)` declaring the catalog
    route and the (wildcard) component route;
  - `EntryProviderScope<NavKey>.<group>ComponentsEntryProvider(navController)` — a
    `catalogEntry<…Catalog, <Group>Component>` (navigating to each component route) plus an
    `entry<<Group>ComponentRoute>` that renders `<Group>ComponentScreen`.
- The group MUST be registered in `demo/components/navigation/ComponentsNav.kt`: add its `…NavGraph()`
  to `componentsNavGraph()`, its `…EntryProvider(navController)` to `componentsEntryProvider`, and a
  corresponding entry to the top-level `Component` enum.

## Demo app: theme editor

Editors for token-set-backed component styles live under `app/.../theme/component/core/`. The
primitive and semantic tiers have parallel editors under `app/.../theme/primitive/` and
`app/.../theme/semantic/`; the requirements below describe the component-style editors, and the other
tiers follow the same shape.

### Screen

- A `<X>StyleScreen(themeController, onNavigateUp)` composable MUST render a `Scaffold` +
  `DemoTopBar`, then a `DemoList` over the token family's `*Token.entries`, with per-token controls.
- Controls MUST read the current token set from `themeController` (via `ThemeState.component`) and
  write edits back with
  `themeController.updateComponent { it.copy(<family> = it.<family> + (token to …)) }`. The screen
  MUST NOT mutate resolved styles directly.

### Navigation

Under `app/.../theme/component/core/navigation/`:

- A `CoreStyleItem : CatalogItem` enum MUST list the editable style families.
- `CoreRoutes.kt` MUST define a sealed `CoreRoute : NavKey`, a `CoreGraph : NavGraphRoute`, a
  `CoreCatalogRoute`, and one route per family (`ButtonStylesRoute`, `TextStylesRoute`, …).
- `CoreNav.kt` MUST define `NavGraphBuilder.coreNavGraph()` (declaring the catalog + per-family
  routes) and `EntryProviderScope<NavKey>.coreEntryProvider(navController, themeController)` (a
  `catalogEntry<CoreCatalogRoute, CoreStyleItem>` plus an `entry<…>` per family rendering the matching
  `<X>StyleScreen(themeController, onNavigateUp)`).
- The group MUST be registered in `theme/component/navigation/ComponentNav.kt`: add `coreNavGraph()`
  to `componentNavGraph()` and `coreEntryProvider(navController, themeController)` to
  `componentEntryProvider`, with a corresponding `ComponentItem` entry.
