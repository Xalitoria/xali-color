package mc.xalitoria.color

interface AnsiColor {
    val hexFg: Int
    val hexBg: Int? get() = null
    val ansiFg: String
    val ansiBg: String
}