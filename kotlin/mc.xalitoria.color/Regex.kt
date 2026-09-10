package mc.xalitoria.color

import kotlin.text.RegexOption.IGNORE_CASE

const val DELIMITER = '&'
const val SECTION = '\u00a7'

val mojangCodeRegex = Regex("&x(?:&[0-9a-f]){6}|&[0-9a-fk-or]".replace('&', SECTION), IGNORE_CASE)
val mojangColorRegex = Regex("&x(?:&[0-9a-f]){6}|&[0-9a-f]".replace('&', SECTION), IGNORE_CASE)

/**
 * @return the [Regex] for color codes ("&C")
 */
val colorCodeRegex = Regex("$DELIMITER([0-9a-f])", IGNORE_CASE)

/**
 * The [Regex] for decor codes ("&k-o" and "&r")
 */
val decorCodeRegex = Regex("$DELIMITER([k-or])", IGNORE_CASE)

/**
 * The [Regex] for bukkit hex codes ("&x&R&R&G&G&B&B" and "&x&R&G&B")
 */
val bukkitHexRegex = Regex("${DELIMITER}x((?:(?:$DELIMITER[0-9a-f]){3}){1,2})", IGNORE_CASE)

/**
 * The [Regex] for hex codes ("&amp;#RRGGBB" and "&amp;#RGB")
 */
val hexCodeRegex = Regex("$DELIMITER#((?:[0-9a-f]{3}){1,2})", IGNORE_CASE)

const val namedColorPattern = "[0-9a-z]{3,}?"
val namedColorRegex             = Regex("\\{#($namedColorPattern)}", IGNORE_CASE)
val namedColorSeparatorRegex    = Regex("\\{#($namedColorPattern)<>}", IGNORE_CASE)
val namedColorGradientRegex     = Regex("\\{#($namedColorPattern)>}(.*?)\\{#($namedColorPattern)<}", IGNORE_CASE)
val namedColorGradientListRegex = Regex("\\{#(?:$namedColorPattern,)*?$namedColorPattern,?}", IGNORE_CASE)

val messageLinkRegex = Regex("(\\[.+])(\\(.+\\))", IGNORE_CASE)

val mmObfuscatedRegex    = Regex("<[\\\\/]?obf(?:uscated)?>")
val mmBoldRegex          = Regex("<[\\\\/]?b(?:old)?>")
val mmStrikethroughRegex = Regex("<[\\\\/]?st(?:rikethrough)?>")
val mmUnderlineRegex     = Regex("<[\\\\/]?u(?:nderlined)?>")
val mmItalicRegex        = Regex("<[\\\\/]?(?:italic|i|em)>")
val mmResetRegex         = Regex("<[\\\\/]?reset>")
