// am aware of how excessive this is, and half of these tests are an insult to my skills

import mc.xalitoria.color.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.text.iterator

fun basicTest(expect: String, input: String) {
    assertEquals(expect, input.applyColorsAndDecor())
    assertEquals(expect, input.applyColors())
}

fun basicMinifyTest(expect: String, input: String) {
    assertEquals(expect, input.applyColorsAndDecor().minifyColors())
}

fun fullBasicTest(expect: String, input: String) {
    basicTest(expect, input)
    basicMinifyTest(expect, input)
}

fun minifyTest(expect: String, input: String) {
    assertEquals(expect, input.applyColors(true))
}

class ColorCodes {
    @Test fun sanity() {
        for (c in "0123456789abcdefklmnor") {
            fullBasicTest("§$c", "&$c")
        }
        fullBasicTest("§0&z", "&0&z")
    }

    @Test fun `format codes`() {
        for (c in "klmno") {
            fullBasicTest("§0§$c", "&0&$c")
            fullBasicTest("§0§$c", "&0§$c")
        }
    }

    @Test fun `minify with format codes`() {
        basicMinifyTest("§0§l§m§n--",      "&0&l&m&n-&l&m&n-")
        basicMinifyTest("§0§l§m§n--§r",    "&0&l&m&n-&l&m&n-&r")
        basicMinifyTest("§0§l§n-§m-",      "&0&l&n-&l&m&n-")
    }

    @Test fun `minify with whitespace`() {
        basicTest ("§0   a   §0a",    "&0   a   &0a")
        basicTest ("§0   a  §l §0a",  "&0   a  &l &0a")
        basicMinifyTest("§0   a   a", "&0   a   &0a")
        basicMinifyTest("§0   a   a", "&0   a  &l &0a")
    }
}

class SpecialCodes {
    @Test fun sanity() {
        mapOf(
            "&x&0&0&0" to '0', "&x&0&0&a" to '1', "&x&0&a&0" to '2', "&x&0&a&a" to '3',
            "&x&a&0&0" to '4', "&x&a&0&a" to '5', "&x&f&a&0" to '6', "&x&a&a&a" to '7',
            "&x&5&5&5" to '8', "&x&5&5&f" to '9', "&x&5&f&5" to 'a', "&x&5&f&f" to 'b',
            "&x&f&5&5" to 'c', "&x&f&5&f" to 'd', "&x&f&f&5" to 'e', "&x&f&f&f" to 'f'
        ).forEach { (key, value) ->
            basicMinifyTest("§$value", key)
        }
    }

    // §1, §2, §3, §4, and §5 are valid colors, but only the last color is necessary
    @Test fun `minify mojang hex codes`() {
        minifyTest("§x§1", "§x§1")
        minifyTest("§x§2", "§x§1§2")
        minifyTest("§x§3", "§x§1§2§3")
        minifyTest("§x§4", "§x§1§2§3§4")
        minifyTest("§x§5", "§x§1§2§3§4§5")
        minifyTest("§x§1§2§3§4§5§6", "§x§1§2§3§4§5§6")
    }

    @Test fun `minify bukkit hex codes`() {
        minifyTest("§5", "&x&a&0&a")
        minifyTest("§0", "&x&0&0&0&0&0&0")
        minifyTest("§x§1§1§2§2§3§3", "&x&1&2&3")
        // &x&1&2&3, &4, &5 are valid colors, but only the last color is necessary
        minifyTest("§4", "&x&1&2&3&4")
        minifyTest("§5", "&x&1&2&3&4&5")
        minifyTest("§x§1§2§3§4§5§6", "&x&1&2&3&4&5&6")
        minifyTest("§7", "&x&1&2&3&4&5&6&7")
        minifyTest(" §x§4§4§5§5§6§6test", "&x&1&2&3 &x&4&5&6test")
    }

    @Test fun `minify amp codes`() {
        minifyTest("§0", "&#000")
        minifyTest("§x§1§1§2§2§3§3", "&#123")
        // &#123 is valid
        minifyTest("§x§1§1§2§2§3§345", "&#12345")
        minifyTest("§x§1§2§3§4§5§6", "&#123456")
        minifyTest("§x§1§2§3§4§5§67", "&#1234567") // for consistency
        minifyTest(" §x§4§4§5§5§6§6test", "&#123 &#456test")

    }

    @Test fun `minify cmi hex codes`() {
        minifyTest("§0", "{#000}")
        minifyTest("§5", "{#a0a}")
        minifyTest("§x§1§1§2§2§3§3", "{#123}")
        minifyTest("{#12345}", "{#12345}")
        minifyTest("§x§1§2§3§4§5§6", "{#123456}")
        minifyTest(" §x§4§4§5§5§6§6test", "{#123} {#456}test")
    }

    @Test fun `format spacing minify`() {
        val input = "&0&k  &1&k  &1&k  &0&k  "
        minifyTest("      §0§k  ", input)
        minifyTest("      §0§l  ", input.replace("k", "l"))
        minifyTest("      §0§o  ", input.replace("k", "o"))
        minifyTest("§0§m  §1§m    §0§m  ", input.replace("k", "m")) // underline
        minifyTest("§0§n  §1§n    §0§n  ", input.replace("k", "n")) // strikethrough
    }
}

class Gradients {
    @Test fun `basic minify`() {
        minifyTest("§f", "{#000>}{#fff<}")
        minifyTest("§0a§f", "{#000>}a{#fff<}")
        minifyTest("§faaaa", "{#white>}aaaa{#fff<}")
    }

    @Test fun `cmi rgb to rgb with format`() {
        val expect =
            "§0H" +
            "§x§1§1§1§1§1§1e" +
            "§x§2§2§2§2§2§2&" +
            "§x§3§3§3§3§3§3a" +
            "§x§4§4§4§4§4§4l" +
            "§8l" +
            "§x§6§6§6§6§6§6o   " +
            "§7§kW" +
            "§x§b§b§b§b§b§b§ko" +
            "§x§c§c§c§c§c§c§kr" +
            "§x§d§d§d§d§d§d§kl" +
            "§x§e§e§e§e§e§e§k§ld" +
            "§f§k§l!"
        val mixbox =
            "§0H" +
            "§x§1§2§1§1§1§2e" +
            "§x§2§3§2§2§2§3&" +
            "§x§3§4§3§2§3§4a" +
            "§x§4§5§4§2§4§5l" +
            "§x§5§5§5§2§5§5l" +
            "§x§6§5§6§2§6§6o   " +
            "§x§a§5§a§3§a§8§kW" +
            "§x§b§6§b§4§b§9§ko" +
            "§x§c§7§c§5§c§a§kr" +
            "§x§d§9§d§8§d§b§kl" +
            "§x§e§c§e§b§e§d§k§ld" +
            "§f§k§l!"
        minifyTest(mixbox, "{#000>}He&allo   &kWorl&ld!{#fff<}")
    }

    @Test fun `cmi named color to rrggbb with extra code`() {
        val expect =
            "§00" +
            "§x§1§1§1§1§1§11" +
            "§x§2§2§2§2§2§22" +
            "§x§3§3§3§3§3§33" +
            "§x§4§4§4§4§4§44" +
            "§8&" +
            "§x§6§6§6§6§6§66   " +
            "§7a" +
            "§x§b§b§b§b§b§bb" +
            "§x§c§c§c§c§c§cc" +
            "§x§d§d§d§d§d§dd" +
            "§x§e§e§e§e§e§ee" +
            "§ff"
        val mixbox =
            "§00" +
            "§x§1§2§1§1§1§21" +
            "§x§2§3§2§2§2§32" +
            "§x§3§4§3§2§3§43" +
            "§x§4§5§4§2§4§54" +
            "§x§5§5§5§2§5§5&" +
            "§x§6§5§6§2§6§66   " +
            "§x§a§5§a§3§a§8a" +
            "§x§b§6§b§4§b§9b" +
            "§x§c§7§c§5§c§ac" +
            "§x§d§9§d§8§d§bd" +
            "§x§e§c§e§b§e§de" +
            "§ff"
        minifyTest(mixbox, "{#black>}01234&6   abcdef{#ffffff<}")
    }

    @Test fun `cmi splitter with typo`() {
        val expect =
            "§x§0§0§0§0§f§fa" +
            "§x§1§7§1§7§f§fa" +
            "§x§2§e§2§e§f§f{" +
            "§x§4§6§4§6§f§f#" +
            "§x§5§d§5§d§f§fr" +
            "§x§7§4§7§4§f§fe" +
            "§x§8§b§8§b§f§fs" +
            "§x§a§2§a§2§f§f<" +
            "§x§b§9§b§9§f§f>" +
            "§x§d§1§d§1§f§f}" +
            "§x§e§8§e§8§f§fa" +
            "§fa"
        val mixbox =
            "§x§0§0§0§0§f§fa" +
            "§x§0§b§1§7§f§fa" +
            "§x§1§a§2§d§f§f{" +
            "§x§2§b§4§4§f§f#" +
            "§x§3§e§5§b§f§fr" +
            "§x§5§4§7§1§f§fe" +
            "§x§6§b§8§8§f§fs" +
            "§x§8§5§9§f§f§f<" +
            "§x§a§1§b§7§f§f>" +
            "§x§b§f§c§e§f§f}" +
            "§x§d§e§e§6§f§fa" +
            "§fa"
        minifyTest(mixbox, "{#blue>}aa{#res<>}aa{#white<}")
    }

    @Test fun `cmi splitter with format`() {
        val expect =
            "§0-" +
            "§x§1§8§0§0§1§8-" +
            "§x§3§1§0§0§3§1§l-" +
            "§x§4§9§0§0§4§9§l-" +
            "§x§6§1§0§0§6§1§l§m-" +
            "§x§7§9§0§0§7§9§l§m-" +
            "§x§9§2§0§0§9§2§l§m§n-" +
            "§5§l§m§n--" +
            "§x§b§6§2§4§b§6§l§m§n-" +
            "§x§c§2§4§9§c§2§l§m§n§o-" +
            "§x§c§e§6§d§c§e§l§m§n§o-" +
            "§x§d§b§9§2§d§b§k§l§m§n§o-" +
            "§x§e§7§b§6§e§7§k§l§m§n§o-" +
            "§x§f§3§d§b§f§3-" +
            "§f-"

        val mixbox =
            "§0-" +
            "§x§1§a§0§5§1§7-" +
            "§x§3§3§0§8§2§f§l-" +
            "§x§4§c§0§8§4§7§l-" +
            "§x§6§4§0§7§5§f§l§m-" +
            "§x§7§b§0§5§7§7§l§m-" +
            "§x§9§3§0§3§9§0§l§m§n-" +
            "§5§l§m§n--" +
            "§x§b§6§1§c§b§7§l§m§n-" +
            "§x§c§2§3§b§c§3§l§m§n§o-" +
            "§x§c§d§5§e§c§e§l§m§n§o-" +
            "§x§d§8§8§4§d§a§k§l§m§n§o-" +
            "§x§e§4§a§b§e§5§k§l§m§n§o-" +
            "§x§f§1§d§5§f§2-" +
            "§f-"
        minifyTest(mixbox, "{#black>}--&l--&m--&n--{#a0a<>}--&o--&k--&r--{#white<}")
    }
}

class Compress {
    @Test fun reduce() =
        minifyTest("§1", "&0&1")

    @Test fun `no reduce`() =
        "&0text&1text&0text&1".let { minifyTest(it.replace("&", "§"), it) }

    @Test fun `reduce all`() =
        minifyTest("§r", "&0&1&2&3&4&5&6&7&8&9&a&b&c&d&e&f&k&l&m&n&o&r")

    @Test fun `reduce with text`() =
        minifyTest("§rtext", "&a&b&c&d&e&f&k&l&m&n&o&rtext")

    @Test fun `reduce with text and format`() =
        minifyTest("§rtext§l", "&a&b&c&d&e&f&k&l&m&n&o&rtext&l")

    @Test fun `reduce with text and format and whitespace`() =
        minifyTest("§rtext §l", "&a&b&c&d&e&f&k&l&m&n&o&rtext &l")

    @Test fun `deduplicate color codes`() {
        minifyTest("§0texttext", "&0text&0text")
        minifyTest("§0texttext", "&0text&l&0text")
        minifyTest("§0text§ltext", "&0text&0&ltext")
    }

}

class Features {
    @Test fun `coded color to string`() {
        assertEquals("§5hello", ("" + CodedColor.DARK_PURPLE + "hello").applyColorCodes())
    }

    @Test fun `named color to string`() {
        assertEquals("§0hello", ("" + NamedColor.BLACK + "hello").applyNamedColors().minifyColors())
    }
    @Test
    fun `tags and newlines stay inside gradient`() {
        val out = "{#a0a>}<bold>ab</bold>\ncd{#fff<}".applyCmiGradients()
        assertEquals(true, out.contains("<bold>") && out.contains("</bold>"))
        assertEquals(false, out.contains("{#"))
        // four visible letters plus the newline are colored; tags are not
        assertEquals(5, Regex("§x").findAll(out).count())
    }

    @Test
    fun `escaped angle bracket is kept`() {
        assertEquals(true, "{#a0a>}a\\<b{#fff<}".applyCmiGradients().contains("\\<"))
    }
    @Test
    fun `single codes are not hex`() {
        assertEquals("<green>hi", "§ahi".componentColors())
        assertEquals("<color:#aabbcc>hi", "§x§a§a§b§b§c§chi".componentColors())
    }

    @Test
    fun `light purple uses mini message name`() {
        assertEquals("<light_purple>x<dark_purple>y", "&dx&5y".componentColors())
    }
}
