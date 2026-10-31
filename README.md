# xali-color

**xali-color** is a small library that converts and strips Minecraft color formats in text: legacy `&` codes, hex
colors, Bukkit and Mojang section-sign codes, CMI gradients and named colors. Its output is plain text or MiniMessage
tags, so it can sit in front of any Adventure serializer, and it can produce ANSI escapes for the console. It has no
server dependency, is published as `mc.xalitoria:color` and targets Java 8.

## Installation

[`util`](../util/README.md) uses `color` as a published artifact (`libs.xalicraft.color` in `libs.versions.toml`), and the
plugins reach it through `util`.

**File** `build.gradle.kts`

```kotlin
dependencies {
    api(libs.xalicraft.color)
}
```

### Publishing

```console
user $ ./gradlew :color:publish
```

The artifact goes to `https://maven.xali.host/releases`. The credentials are read from the environment variables
`MAVEN_USER` and `MAVEN_TOKEN`, or the Gradle properties `maven.user` and `maven.token`.
`./gradlew :color:publishToMavenLocal` publishes to the local Maven repository to try a change.

> [!NOTE]
> The catalog pins the published version (`0.0.9`), which can differ from the version in `color/build.gradle.kts`. After
> a release, raise `xalicraft-color` in `libs.versions.toml`.

## Usage

The functions are extensions on `String` (and, for the ones that build text, on collections of strings), in the package
`mc.xalitoria.color`.

### Formats

| Format | Example | Notes |
|---|---|---|
| Color and decoration codes | `&c`, `&l`, `&r` | Colors `0-9a-f`, decorations `k-o` and `r`. |
| Hex codes | `&#RRGGBB`, `&#RGB` | |
| Bukkit hex codes | `&x&R&R&G&G&B&B` | Also the short form `&x&R&G&B`. |
| Mojang codes | the same with `§` | What the client receives. |
| Shadow codes | `^c`, `^#RRGGBB` | The same forms with `^` for the shadow color. |
| Named colors | `{#name}`, `{#RRGGBB}` | `NamedColor` has the names. |
| CMI gradients | `{#a0a>}text{#fff<}` | A start color, the text, and an end color. `{#color<>}` splits a gradient. |

### Functions

| Prefix | What it does |
|---|---|
| `strip...` | Removes a format: `stripColors`, `stripColorCodes`, `stripDecorCodes`, `stripHexCodes`, `stripBukkitCodes`, `stripBukkitHexCodes`, `stripMojangCodes`, `stripNamedColors`, `stripCmiGradients`, `stripStyling` and `stripMatching`. |
| `apply...` | Turns a format into Mojang codes: `applyColors`, `applyColorCodes`, `applyDecorCodes`, `applyHexCodes`, `applyBukkitColors`, `applyNamedColors`, `applyCmiGradients` and `applyColorsAndDecor`. Run `applyCmiGradients` first: stripping does not remove gradient codes. |
| `component...` | Turns a format into MiniMessage tags: `componentColors`, `componentColorCodes`, `componentDecorCodes`, `componentHexCodes`, `componentBukkitCodes`, `componentNamedColors` and `componentCmiGradients`. |
| others | `gradient(start, end)`, `minifyColors`, `unescapeGradients`, `expandCmiGradientSeparators` and `codesToAnsi`. |

```kotlin
"&cRed &#00ff88green {#a0a>}fading{#fff<}".componentColors()
// <red>Red <color:#00FF88>green <gradient:#AA00AA:#FFFFFF>fading</gradient>

"&cRed &lbold".stripColors()
// Red bold
```

A MiniMessage tag in the input (`<bold>`, `<click:...>`) is left as it is, and an escaped `\<` counts as one character.

The colors themselves are `Color` (an RGB value that can be mixed with another through Mixbox, with ANSI output), `CodedColor`
(the sixteen legacy colors, with aliases such as `GREY` for `GRAY`) and `NamedColor` (an enum of named colors).

## Development

Sources are under `color/kotlin/mc.xalitoria.color` (the folder name has dots, matching the package), tests under
`color/test`.

```console
user $ ./gradlew :color:test
```

| Test | Covers |
|---|---|
| `Common` | Every conversion, minifying and deduplicating codes, CMI gradients with typos and formats, and tags and newlines inside a gradient. |
| `Benchmark` | A timing run over random strings. It prints its results and asserts nothing. |

`color` targets Java 8, so avoid APIs newer than that. `NamedColor.kt` is a large table of names; add a name there
rather than in callers.

## See also

- [util](../util/README.md) — builds on `color` for messages
- [XaliChat](../chat/README.md#formatting-syntax) — the syntax as players see it
