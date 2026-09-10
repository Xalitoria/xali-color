package mc.xalitoria.color

@Suppress("unused")
enum class CodedColor(override val rgb: Int, val code: Char, private val ansi: Byte): AnsiColor {
    BLACK       (0x000000, '0', 30),
    BLUE        (0x0000AA, '1', 34),
    GREEN       (0x00AA00, '2', 32),
    AQUA        (0x00AAAA, '3', 36),
    RED         (0xAA0000, '4', 31),
    PURPLE      (0xAA00AA, '5', 35), MAGENTA(PURPLE),
    GOLD        (0xFFAA00, '6', 33), YELLOW(GOLD),
    GRAY        (0xAAAAAA, '7', 37), LIGHT_GRAY(GRAY), GREY(GRAY), LIGHT_GREY(GREY),
    DARK_GRAY   (0x555555, '8', 90), DARK_GREY(DARK_GRAY),
    LIGHT_BLUE  (0x5555FF, '9', 94),
    LIGHT_GREEN (0x55FF55, 'a', 92),
    LIGHT_AQUA  (0x55FFFF, 'b', 96),
    LIGHT_RED   (0xFF5555, 'c', 91),
    LIGHT_PURPLE(0xFF55FF, 'd', 95), LIGHT_MAGENTA(LIGHT_PURPLE),
    LIGHT_GOLD  (0xFFFF55, 'e', 93), LIGHT_YELLOW(LIGHT_GOLD),
    WHITE       (0xFFFFFF, 'f', 97), LIGHT_WHITE(WHITE),
    RESET       (0xFFFFFF, 'r', 0) {
        override val ansiBg = "\u001B[49m"
    };

    constructor(copy: CodedColor) : this(copy.rgb, copy.code, copy.ansi)

    override val ansiFg = "\u001B[${ansi}m"
    override val ansiBg = "\u001B[${ansi+10}m"
    val color: Color by lazy { Color(rgb) }

    override fun toString(): String = "${DELIMITER}$code"

    companion object {
        operator fun get(code: Char): CodedColor? = entries.find { it.code.equals(code, true) }
        operator fun get(query: String): CodedColor? =
            if (query.length == 1) get(query.first())
            else entries.find { it.name.equals(query, true) || it.rgb == Color[query]?.rgb }

        fun nearest(color: Color): CodedColor = entries.minBy { color.distance(it.color) }
    }
}