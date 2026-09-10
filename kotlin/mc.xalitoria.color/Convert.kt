/**
 * "Codes" ("Color Codes" or "Hex Codes") are minecraft-specific coded colors
 * "Colors" ("Hex Colors") are standard hex colors, named [NamedColor], or [Color] objects
 * I did my best, terminology is hard, anyways...
 */

@file:Suppress("NOTHING_TO_INLINE", "unused")

package mc.xalitoria.color

import mc.xalitoria.color.CodedColor.RESET
import mc.xalitoria.color.Color.Companion.ANSI_ESC
import mc.xalitoria.color.Color.Companion.ANSI_LINK
import kotlin.text.RegexOption.IGNORE_CASE

fun String.stripMatching(query: Color): String =
    replace(query.hexMojang(), "").replace(query.hexMinify(), "")

//
// STRIP CODES AND COLORS
//
fun String.stripStyling(): String =
    stripNamedColors().stripHexCodes().stripBukkitCodes()

// STRIP NAMED COLORS
fun String.stripNamedColors(): String =
    replace(namedColorRegex) { if (Color[it.groupValues[1]] != null) "" else it.value }
inline fun <T : Iterable<String>> T.stripNamedColors(): List<String> =
    map { it.stripNamedColors() }
inline fun Array<String>.stripNamedColors() =
    map { it.stripNamedColors() }.toTypedArray()

// STRIP HEX CODES
fun String.stripHexCodes(): String =
    replace(hexCodeRegex, "")
inline fun <T : Iterable<String>> T.stripHexCodes(): List<String> =
    map { it.stripHexCodes() }
inline fun Array<String>.stripHexCodes() =
    map { it.stripHexCodes() }.toTypedArray()

// STRIP BUKKIT CODES
fun String.stripBukkitCodes() =
    stripBukkitHexCodes().stripColorCodes().stripDecorCodes()
inline fun <T : Iterable<String>> T.stripBukkitCodes(): List<String> =
    map { it.stripBukkitCodes() }
inline fun Array<String>.stripBukkitCodes() =
    map { it.stripBukkitCodes() }.toTypedArray()

// STRIP BUKKIT HEX CODES
fun String.stripBukkitHexCodes(): String =
    replace(bukkitHexRegex, "")
inline fun <T : Iterable<String>> T.stripBukkitHexCodes(): List<String> =
    map { it.stripBukkitHexCodes() }
inline fun Array<String>.stripBukkitHexCodes() =
    map { it.stripBukkitHexCodes() }.toTypedArray()

// STRIP DECOR CODES
fun String.stripDecorCodes(): String =
    replace(decorCodeRegex, "")
inline fun <T : Iterable<String>> T.stripDecorCodes(): List<String> =
    map { it.stripDecorCodes() }
inline fun Array<String>.stripDecorCodes() =
    map { it.stripDecorCodes() }.toTypedArray()

// STRIP COLOR CODES
fun String.stripColorCodes(): String =
    replace(colorCodeRegex, "")
inline fun <T : Iterable<String>> T.stripColorCodes(): List<String> =
    map { it.stripColorCodes() }
inline fun Array<String>.stripColorCodes() =
    map { it.stripColorCodes() }.toTypedArray()

// STRIP MOJANG CODES
fun String.stripMojangCodes(): String =
    replace(mojangCodeRegex, "")
inline fun <T : Iterable<String>> T.stripMojangCodes(): List<String> =
    map { it.stripMojangCodes() }
inline fun Array<String>.stripMojangCodes() =
    map { it.stripMojangCodes() }.toTypedArray()

/**
 * Does not remove gradient codes; run [applyCmiGradients] first
 *
 * @return the [String] without any color codes ("&C") and hex codes ("&#RRGGBB" and "&#RGB")
 */
fun String.stripColors(): String =
    stripNamedColors()
        .stripHexCodes()
        .stripBukkitHexCodes()
        .stripColorCodes()
        .stripDecorCodes()
        .stripMojangCodes()
inline fun <T : Iterable<String>> T.stripColors(): List<String> =
    map { it.stripColors() }
inline fun Array<String>.stripColors() =
    map { it.stripColors() }.toTypedArray()


//
// CONVERT COLORS AND CODES
//

/**
 * @return the [String] with all [NamedColor] codes ("{#RRGGBB}", "{#RGB}" or "{#COLORNAME}") applied
 * to minified mojang color codes ("&C" or "§x§R§R§G§G§B§B")
 */
fun String.applyNamedColors(): String =
    replace(namedColorRegex) { Color[it.groupValues[1]]?.hexMojang() ?: it.value }
inline fun <T : Iterable<String>> T.applyNamedColors(): List<String> =
    map { it.applyNamedColors() }
inline fun Array<String>.applyNamedColors() =
    map { it.applyNamedColors() }.toTypedArray()

/**
 * @return the [String] with all hex codes ("&amp;#RRGGBB" and "&amp;#RGB") applied
 * to mojang hex codes ("§x§R§R§G§G§B§B")
 */
fun String.applyHexCodes(): String =
    replace(hexCodeRegex) { Color[it.groupValues[1]]?.hexMojang() ?: it.value }
inline fun <T : Iterable<String>> T.applyHexCodes(): List<String> =
    map { it.applyHexCodes() }
inline fun Array<String>.applyHexCodes() =
    map { it.applyHexCodes() }.toTypedArray()

/**
 * @return the [String] with all color codes ("&C") applied to mojang color codes ("§C")
 */
fun String.applyColorsAndDecor(): String =
    applyBukkitColors()
    .applyColorCodes()
    .applyDecorCodes()
inline fun <T : Iterable<String>> T.applyColorsAndDecor(): List<String> =
    map { it.applyColorsAndDecor() }
inline fun Array<String>.applyColorsAndDecor() =
    map { it.applyColorsAndDecor() }.toTypedArray()

// CONVERT BUKKIT COLORS
fun String.applyBukkitColors(): String = replace(bukkitHexRegex) { match ->
    Color[match.groupValues[1].replace("&", "")]!!.hexMojang()
}
inline fun <T : Iterable<String>> T.applyBukkitColors(): List<String> =
    map { it.applyBukkitColors() }
inline fun Array<String>.applyBukkitColors() =
    map { it.applyBukkitColors() }.toTypedArray()

// CONVERT DECOR CODES
fun String.applyDecorCodes(): String =
    replace(decorCodeRegex) { SECTION.toString() + it.value[1] }
inline fun <T : Iterable<String>> T.applyDecorCodes(): List<String> =
    map { it.applyDecorCodes() }
inline fun Array<String>.applyDecorCodes() =
    map { it.applyDecorCodes() }.toTypedArray()

// CONVERT COLOR CODES
fun String.applyColorCodes(): String =
    replace(colorCodeRegex) { SECTION + it.groupValues[1] }
inline fun <T : Iterable<String>> T.applyColorCodes(): List<String> =
    map { it.applyColorCodes() }
inline fun Array<String>.applyColorCodes() =
    map { it.applyColorCodes() }.toTypedArray()


fun String.expandCmiGradientSeparators(): String =
    replace(namedColorSeparatorRegex) { match ->
        val hexCode = Color[match.groupValues[1]] ?: return@replace match.value
        val decor = BooleanArray(5)
        val matches = decorCodeRegex.findAll(substring(0, match.range.first)).toList()
        for (i in matches.indices.reversed()) { // reversed for proper sorting
            val f = matches[i].value[1].lowercaseChar()
            if (f == 'r') break
            decor[f - 'k'] = true
        }

        StringBuilder(22 + decor.size * 2).append("{#$hexCode<}{#$hexCode>}").apply {
            for (i in decor.indices) if (decor[i]) append(DELIMITER).append('k' + i)
        }
    }

/**
 * @return the [String] with all [NamedColor] gradient codes ("{#color1>}{#color2<>}{#color3<}" etc.) applied
 * to simple color codes ("&#RRGGBB")
 */
fun String.applyCmiGradients(): String =
    expandCmiGradientSeparators().replace(namedColorGradientRegex) { match ->
        match.groupValues[2].gradient(
            Color[match.groupValues[1]] ?: return@replace match.value,
            Color[match.groupValues[3]] ?: return@replace match.value
        )
    }

fun String.applyColors(minify: Boolean = false) =
    applyCmiGradients()
    .applyNamedColors()
    .applyHexCodes()
    .applyColorsAndDecor()
    .let { if (minify) it.minifyColors() else it }

private inline fun String.colorMatches(): List<Triple<String, MatchResult, Int>> = (
    hexCodeRegex.findAll(this).map { Triple("hex", it, it.range.first) } +
    decorCodeRegex.findAll(this).map { Triple("fmt", it, it.range.first) } +
    messageLinkRegex.findAll(this).map { Triple("link", it, it.range.first) }
).sortedBy { it.third }.toList()

/**
 * As this does not replace [NamedColor] gradient codes, use String[applyCmiGradients] first
 * @return the following [String]:
 *
 * - shortens color codes, if possible ("§x§a§a§0§0§a§a" -> "§5")
 *
 * - removes unnecessary mojang color codes ("§C" and "§x§R§R§G§G§B§B")
 *
 * - removes unnecessary mojang decor codes ("§D") and sorts leading codes alphabetically
 */
fun String.minifyColors(): String {
    val matches = mojangCodeRegex.findAll(this)
    val result = StringBuilder(length)
        .append(substring(0, matches.firstOrNull()?.range?.first ?: return this))

    var lastColor = ""
    val lastDecor = BooleanArray(5)
    val sift = mutableListOf<String>()

    val iterator = matches.iterator()
    for ((index, match = value) in iterator.withIndex()) {
        val text = substring(match.range.last + 1..<(matches.elementAtOrNull(index + 1)?.range?.first ?: length))
        val m = match.value
        sift.add(m)

        // whitespace handling
        if (iterator.hasNext()) {
            if (text.isEmpty()) continue
            if (text.isBlank() && !(m.contains(SECTION + "m") || m.contains(SECTION + "n"))) {
                result.append(text)
                sift.clear()
                continue
            }
        }

        val newDecor = BooleanArray(5) // me trying to optimize lol
        val newColor = buildString {
            for (s in sift.indices.reversed()) { // reversing the sift allows alphabetical sorting
                val c = sift[s][1].lowercaseChar()
                if (c in 'k'..'o') {
                    newDecor[c - 'k'] = true
                    continue // this is stupidly important
                } else if (c in "0123456789abcdefr") { // r is considered a color in this case
                    sift[s].let { if (lastColor != it) append(it) }
                } else if (c == 'x') {
                    Color.mojang(sift[s])!!.hexMinify().let { if (lastColor != it) append(it) }
                }
                break
            }
        }
        sift.clear()

        if (newColor.isEmpty()) {
            for (index in newDecor.indices) {
                if (newDecor[index] && !lastDecor[index]) {
                    lastDecor[index] = true
                    result.append(SECTION).append('k' + index)
                }
            }
        } else {
            lastColor = newColor
            newDecor.copyInto(lastDecor)
            result.append(newColor)
            for (index in lastDecor.indices) {
                if (lastDecor[index]) result.append(SECTION).append('k' + index)
            }
        }

        result.append(text)
    }
    return result.toString()
}
inline fun <T : Iterable<String>> T.minifyColors(): List<String> =
    map { it.minifyColors() }
inline fun Array<String>.minifyColors(): Array<String> =
    map { it.minifyColors() }.toTypedArray()


fun String.codesToAnsi(): String {
    val text = applyColors()
    val matches = text.colorMatches()
    if (matches.isEmpty()) return text + RESET.ansiFg

    val result = StringBuilder(length + 32)
    var cursor = 0

    // TODO Find a better way to do this without a string map
    for ((kind = first, match = second) in matches) {
        val start = match.range.first
        val endEx = match.range.last + 1

        if (start > cursor) result.append(text, cursor, start)

        when (kind) {
            "hex" -> Color[match.groupValues[1]]?.let {
                result.append(RESET.ansiFg)
                result.append(it.ansiFg)
            } ?: result.append(match.value)

            "fmt" -> when (match.value.last().lowercaseChar()) {
                'k' -> { /* obfuscate: ignore */ }
                'l' -> result.append("${ANSI_ESC}1m") // bold
                'm' -> { /* monospace: no ANSI */ }
                'n' -> result.append("${ANSI_ESC}4m") // underline
                'o' -> result.append("${ANSI_ESC}3m") // italic
                'r' -> result.append(RESET.ansiFg)
            }

            "link" -> {
                result.append('[').append(match.groupValues[0].codesToAnsi()).append(RESET.ansiFg).append(']')
                result.append('(').append(ANSI_LINK).append(match.groupValues[1]).append(RESET.ansiFg).append(')')
            }
        }

        cursor = endEx
    }
    if (cursor < text.length) result.append(text, cursor, text.length)
    result.append(RESET.ansiFg)
    return result.toString()
}

/**
 * @param start the starting [Color]
 * @param end the ending [Color]
 * @return the [String] with a gradient from [start] to [end]
 */
fun String.gradient(start: Color, end: Color): String {
    if (isEmpty()) return end.hexMojang() + this
    if (start == end) return start.hexMojang() + this
    if (length < 2) return start.hexMojang() + this + end.hexMojang()

    val strip = stripDecorCodes().lastIndex
    val factor = 1F / strip

    val result = StringBuilder(length * 14 + (length - strip + 1) * 2)
    var decor = ""
    var textIndex = 0
    var gradientIndex = 0
    while (textIndex < length) {
        val char = this[textIndex]
        if ((char in [SECTION, DELIMITER]) && textIndex < length - 1) {
            val preDecor = substring(textIndex, textIndex + 2)
            if (decorCodeRegex.matches(preDecor)) {
                decor = if (preDecor[1] == 'r') "" else decor + preDecor
                textIndex += 2
                continue
            }
        }
        result
            .append(start.mixbox(end, gradientIndex * factor).hexMojang())
            .append(decor)
            .append(char)
        textIndex++
        gradientIndex++
    }
    return result.toString()
}