package mc.xalitoria.color.resolver

import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer


class CmiResolver: TagResolver {
    override fun resolve(
        name: String,
        arguments: ArgumentQueue,
        ctx: Context
    ): Tag? {
        TODO("Not yet implemented")
        LegacyComponentSerializer.builder()
    }

    override fun has(name: String): Boolean {
        TODO("Not yet implemented")
    }

    fun cmiHexResolver(): TagResolver = TagResolver.resolver("cmi_hex") { argumentQueue: ArgumentQueue, _: Context ->
        // Read the argument inside the tag, e.g. <cmi:#ff0000>
        val hexArg = argumentQueue.popOr("Expected hex color code").value()

        if (HEX_6.matcher(hexArg).matches()) {
            val color = TextColor.fromHexString(hexArg)
            if (color != null) return@resolver Tag.styling(color)
        }
        null // Fallback / invalid tag
    }
}