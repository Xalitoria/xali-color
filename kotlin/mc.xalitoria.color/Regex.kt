package mc.xalitoria.color

import kotlin.text.RegexOption.DOT_MATCHES_ALL
import kotlin.text.RegexOption.IGNORE_CASE

const val COLOR_DELIM = '&'
const val SHADOW_DELIM = '^'
const val SECTION = '\u00a7'

val mojangCodeRegex = Regex("&x(?:&[0-9a-f]){6}|&[0-9a-fk-or]".replace('&', SECTION), IGNORE_CASE)
val mojangColorRegex = Regex("&x(?:&[0-9a-f]){6}|&[0-9a-f]".replace('&', SECTION), IGNORE_CASE)

/**
 * @return the [Regex] for color codes ("&C")
 */
val codeColorRegex = Regex("$COLOR_DELIM([0-9a-f])", IGNORE_CASE)
val codeShadowRegex = Regex("$SHADOW_DELIM([0-9a-f])", IGNORE_CASE)

/**
 * The [Regex] for decor codes ("&k-o" and "&r")
 */
val codeDecorRegex = Regex("$COLOR_DELIM([k-or])", IGNORE_CASE)

/**
 * The [Regex] for bukkit hex codes ("&x&R&R&G&G&B&B" and "&x&R&G&B")
 */
val bukkitColorRegex = Regex("${COLOR_DELIM}x((?:(?:$COLOR_DELIM[0-9a-f]){3}){1,2})", IGNORE_CASE)
val bukkitShadowRegex = Regex("${SHADOW_DELIM}x((?:(?:$SHADOW_DELIM[0-9a-f]){3,4}){1,2})", IGNORE_CASE)

/**
 * The [Regex] for hex codes ("&amp;#RRGGBB" and "&amp;#RGB")
 */
val hexColorRegex = Regex("$COLOR_DELIM#((?:[0-9a-f]{3}){1,2})", IGNORE_CASE)
val hexShadowRegex = Regex("$SHADOW_DELIM#((?:[0-9a-f]{3,4}){1,2})", IGNORE_CASE)

const val namedColorPattern = "[0-9a-z]{3,}?"
val namedColorRegex             = Regex("\\{#($namedColorPattern)}", IGNORE_CASE)
val namedColorSeparatorRegex    = Regex("\\{#($namedColorPattern)<>}", IGNORE_CASE)
val namedColorGradientRegex     = Regex("\\{#($namedColorPattern)>}(.*?)\\{#($namedColorPattern)<}", [IGNORE_CASE, DOT_MATCHES_ALL])
val namedColorGradientEscapeRegex = Regex("\\\\<(?=>?})")

/** A MiniMessage tag such as `<bold>`, `</red>` or `<click:open_url:...>`; a gradient leaves these uncolored. */
val miniTagRegex = Regex("<[/!]?[a-z#][^<>]*>", IGNORE_CASE)

val messageLinkRegex = Regex("(\\[.+])(\\(.+\\))", IGNORE_CASE)

val mmObfuscatedRegex    = Regex("<[\\\\/]?obf(?:uscated)?>")
val mmBoldRegex          = Regex("<[\\\\/]?b(?:old)?>")
val mmStrikethroughRegex = Regex("<[\\\\/]?st(?:rikethrough)?>")
val mmUnderlineRegex     = Regex("<[\\\\/]?u(?:nderlined)?>")
val mmItalicRegex        = Regex("<[\\\\/]?(?:italic|i|em)>")
val mmResetRegex         = Regex("<[\\\\/]?reset>")
