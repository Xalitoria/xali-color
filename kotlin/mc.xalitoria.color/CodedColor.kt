package mc.xalitoria.color

@Suppress("unused")
enum class CodedColor(
    val code: Char,
    override val hexFg: Int,
    override val hexBg: Int,
    private val ansiFgIndex: Byte,
    private val ansiBgIndex: Byte = (ansiFgIndex + 10).toByte()
): AnsiColor {
    BLACK       ('0', 0x000000, 0x000000FF, 30),
    DARK_BLUE   ('1', 0x0000AA, 0x00002AFF, 34),
    DARK_GREEN  ('2', 0x00AA00, 0x002A00FF, 32),
    DARK_AQUA   ('3', 0x00AAAA, 0x002A2AFF, 36),
    DARK_RED    ('4', 0xAA0000, 0x2A0000FF, 31),
    DARK_PURPLE ('5', 0xAA00AA, 0x2A002AFF, 35), MAGENTA(DARK_PURPLE),
    GOLD        ('6', 0xFFAA00, 0x2A2A00FF, 33), DARK_YELLOW(GOLD),
    GRAY        ('7', 0xAAAAAA, 0x2A2A2AFF, 37), LIGHT_GRAY(GRAY), GREY(GRAY), LIGHT_GREY(GREY),
    DARK_GRAY   ('8', 0x555555, 0x151515FF, 90), DARK_GREY(DARK_GRAY),
    BLUE        ('9', 0x5555FF, 0x15153FFF, 94),
    GREEN       ('a', 0x55FF55, 0x153F15FF, 92),
    AQUA        ('b', 0x55FFFF, 0x153F3FFF, 96),
    RED         ('c', 0xFF5555, 0x3F1515FF, 91),
    LIGHT_PURPLE      ('d', 0xFF55FF, 0x1F351FFF, 95), PURPLE(LIGHT_PURPLE),
    YELLOW      ('e', 0xFFFF55, 0x3F3F15FF, 93),
    WHITE       ('f', 0xFFFFFF, 0x3F3F3FFF, 97),
    RESET       ('r', 0xFFFFFF, 0x3F3F3FFF, 0) {
        override val ansiFg = "\u001B[49m"
        override val ansiBg = ""
    };

    constructor(copy: CodedColor) : this(copy.code, copy.hexFg, copy.hexBg, copy.ansiFgIndex, copy.ansiBgIndex)

    override val ansiFg = "\u001B[${ansiFgIndex}m"
    override val ansiBg = "\u001B[${ansiBgIndex}m"
    val color: Color by lazy { Color(hexFg) }

    override fun toString(): String = "${COLOR_DELIM}$code"

    companion object {
        operator fun get(code: Char): CodedColor? = entries.find { it.code.equals(code, true) }
        operator fun get(query: String): CodedColor? =
            if (query.length == 1) get(query.first())
            else entries.find { it.name.equals(query, true) || it.hexFg == Color[query]?.hexFg }

        fun nearest(color: Color): CodedColor = entries.minBy { color.distance(it.color) }
    }
}