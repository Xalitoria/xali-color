@file:Suppress("NOTHING_TO_INLINE", "unused")

package mc.xalitoria.color

fun String.componentBukkitCodes(): String =
    replace(bukkitColorRegex) { match ->
        val hex = match.value
        when (hex.length) {
            8 -> StringBuilder(7).append("<color:#").apply {
                for (c in hex.drop(3)) {
                    if (c == COLOR_DELIM) continue
                    append(c).append(c)
                }
                append(">")
            }
            14 -> hex.replace("[${COLOR_DELIM}x]".toRegex(), "").let { "<color:#$it>" }
            else -> hex
        }
    }
inline fun <T : Iterable<String>> T.componentBukkitCodes(): List<String> =
    map { it.componentBukkitCodes() }
inline fun Array<String>.componentBukkitCodes() =
    map { it.componentBukkitCodes() }.toTypedArray()


fun String.componentColorCodes(): String =
    replace(codeColorRegex) { match ->
        CodedColor[match.value[1]]?.let { "<${it.name.lowercase()}>" } ?: match.value
    }
inline fun <T : Iterable<String>> T.componentColorCodes(): List<String> =
    map { it.componentColorCodes() }
inline fun Array<String>.componentColorCodes() =
    map { it.componentColorCodes() }.toTypedArray()


fun String.componentDecorCodes(): String =
    replace(codeDecorRegex) { match ->
        when (match.groupValues[1]) {
            "k" -> "obf"
            "l" -> "b"
            "m" -> "st"
            "n" -> "u"
            "o" -> "i"
            "r" -> "reset"
            else -> match.value
        }.let { "<$it>" }
    }
inline fun <T : Iterable<String>> T.componentDecorCodes(): List<String> =
    map { it.componentDecorCodes() }
inline fun Array<String>.componentDecorCodes() =
    map { it.componentDecorCodes() }.toTypedArray()


fun String.componentHexCodes(): String =
    replace(hexColorRegex) { match ->
        Color[match.groupValues[1]]?.toString()?.let { "<color:#$it>" } ?: match.value
    }
inline fun <T : Iterable<String>> T.componentHexCodes(): List<String> =
    map { it.componentHexCodes() }
inline fun Array<String>.componentHexCodes() =
    map { it.componentHexCodes() }.toTypedArray()


fun String.componentNamedColors(): String =
    replace(namedColorRegex) { match -> NamedColor[match.groupValues[1]]?.let { "<color:#${it.color}>"} ?: match.value }
inline fun <T : Iterable<String>> T.componentNamedColors(): List<String> =
    map { it.componentNamedColors() }
inline fun Array<String>.componentNamedColors() =
    map { it.componentNamedColors() }.toTypedArray()


fun String.componentCmiGradients(): String =
    expandCmiGradientSeparators().replace(namedColorGradientRegex) { match ->
        val s = Color[match.groupValues[1]] ?: return@replace match.value
        val e = Color[match.groupValues[3]] ?: return@replace match.value
        "<gradient:#$s:#$e>${match.groupValues[2]}</gradient>"
    }
inline fun <T : Iterable<String>> T.componentCmiGradients(): List<String> =
    map { it.componentCmiGradients() }
inline fun Array<String>.componentCmiGradients() =
    map { it.componentCmiGradients() }.toTypedArray()


fun String.componentColors(): String = mojangColorRegex.replace(this) { match ->
    if (match.value.length < 14) match.value
    else match.value.drop(3).replace(SECTION.toString(), "").let { "<color:#$it>" }
}.replace(SECTION, COLOR_DELIM)
    .componentCmiGradients().componentNamedColors()
    .componentHexCodes().componentBukkitCodes()
    .componentDecorCodes().componentColorCodes()
inline fun <T : Iterable<String>> T.componentColors(): List<String> =
    map { it.componentColors() }
inline fun Array<String>.componentColors() =
    map { it.componentColors() }.toTypedArray()