package mc.xalitoria.color

import com.scrtwpns.Mixbox
import kotlin.math.roundToInt
import kotlin.text.RegexOption.IGNORE_CASE
import java.awt.Color as JColor

@Suppress("unused")
class Color(red: Int, green: Int, blue: Int, strict: Boolean = false): AnsiColor {
    val red: Int = if (strict) { require(red in range) { "Red component must be between 0 and 255" }; red } else red.coerceIn(range)
    val green: Int = if (strict) { require(green in range) { "Green component must be between 0 and 255" }; green } else green.coerceIn(range)
    val blue: Int = if (strict) { require(blue in range) { "Blue component must be between 0 and 255" }; blue } else blue.coerceIn(range)
    override val rgb: Int = (red shl 16) + (green shl 8) + blue

    constructor(rgb: Int, strict: Boolean = false) : this(
        rgb shr 16 and 0xFF,
        rgb shr 8 and 0xFF,
        rgb and 0xFF,
        strict
    )

    data class Destructured(val red: Int, val green: Int, val blue: Int)

    val destructured: Destructured by lazy { Destructured(red, green, blue) }

    override fun toString(): String = "%06X".format(rgb)
    val ampString by lazy { "&" + toString() }
    val javaColor by lazy { JColor(red, green, blue) }

    override val ansiFg by lazy { "${ANSI_ESC}38;2;$red;$green;$blue" + "m" }
    override val ansiBg by lazy { "${ANSI_ESC}48;2;$red;$green;$blue" + "m" }

    fun distance(color: Color): Int =
        (color.red - red).let { it * it } +
        (color.green - green).let { it * it } +
        (color.blue - blue).let { it * it }

    fun interpolate(mixTo: Color, factor: Float): Color = factor.coerceIn(0F, 1F).let {
        Color(
            (red   * (1 - it) + mixTo.red   * it).roundToInt(),
            (green * (1 - it) + mixTo.green * it).roundToInt(),
            (blue  * (1 - it) + mixTo.blue  * it).roundToInt()
        )
    }

    fun mixbox(mixTo: Color, factor: Float): Color =
        Mixbox.lerp(rgb, mixTo.rgb, factor.coerceIn(0F, 1F)).let(::Color)

    fun interpolateToList(color: Color, steps: Int): List<Color> =
        (0..steps).map { interpolate(color, it.toFloat() / steps) }

    fun hexMojang(): String {
        val result = StringBuilder(14).append(SECTION).append("x")
        for (c in toString()) result.append(SECTION).append(c)
        return result.toString().lowercase()
    }

    fun hexBukkit(): String {
        val result = StringBuilder(14).append("&x")
        for (c in toString()) result.append("&").append(c)
        return result.toString().lowercase()
    }

    fun hexMinify(): String = CodedColor.nearest(this).let {
        if (it.rgb == rgb) "$SECTION${it.code}" else hexMojang()
    }

    companion object {
        val JColor.xColor get() = Color(red, green, blue)
        const val ANSI_ESC = "\u001B["
        val ANSI_LINK = CodedColor.LIGHT_AQUA.ansiFg
        private val range = 0..255

        /**
         * @return The [Color] object or null, if invalid.
         */
        operator fun get(query: String): Color? {
            val match = "^#?((?:[a-f0-9]{3}){1,2})$".toRegex(IGNORE_CASE).find(query)?.groupValues[1]
                ?: return NamedColor[query]?.color
            return if (match.length == 6) match else {
                val result = StringBuilder(6)
                for (c in match) result.append(c).append(c)
                result.toString()
            }.toInt(16).let { Color(it, true) } // Regex will never allow it to throw
        }

        fun mojang(query: String): Color? {
            val result = mojangColorRegex.find(query)?.value ?: return null
            return when (result.length) {
                2 -> CodedColor[result.last()]?.color
                14 -> Color(result.substring(2).replace(SECTION.toString(), "").toInt(16))
                else -> null
            }
        }
    }
}
