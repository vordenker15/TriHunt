package net.trilleo.mc.plugins.trihunt.items.utilityItems

import net.trilleo.mc.plugins.trihunt.registration.PluginItem
import net.trilleo.mc.plugins.trihunt.utils.PDCEntryUtil
import net.trilleo.mc.plugins.trihunt.utils.itemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.net.URL
import java.util.*

class EnchantedGoldenHeadItem(private val plugin: JavaPlugin) : PluginItem("enchanted_golden_head") {

    private val textureValue =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGYwOTcxNmM5YjJhNmZlZGM2YzVlNTVhODY3YWUxOGU5Y2Q5OWQ0YjU1MDFhNDNiMGZlNzIzNjRhYmYzZDJmYyJ9fX0="

    override fun buildItem(amount: Int): ItemStack = itemStack(Material.PLAYER_HEAD) {
        name("<light_purple>Enchanted Golden Head")
        lore(
            "<gray>An enchanted head that grants",
            "<gray>immense power when consumed."
        )
        pdc(
            PDCEntryUtil.PDCKey(plugin).itemIdentifierKey,
            PersistentDataType.STRING,
            PDCEntryUtil.PDCValue().enchantedGoldenHeadItemIdentifier
        )

        meta {
            val skullMeta = this as SkullMeta
            applyTexture(skullMeta, textureValue)
        }
    }

    private fun applyTexture(meta: SkullMeta, base64: String) {
        val profile = Bukkit.createProfile(UUID.randomUUID(), "EnchantedGoldenHead")
        val textures = profile.textures

        val textureUrl = extractTextureUrl(base64)
        textures.skin = URL(textureUrl)

        profile.setTextures(textures)
        meta.playerProfile = profile
    }

    private fun extractTextureUrl(base64: String): String {
        val decoded = String(Base64.getDecoder().decode(base64))
        val regex = """"url"\s*:\s*"([^"]+)"""".toRegex()
        return regex.find(decoded)?.groupValues?.get(1)
            ?: error("Invalid skull texture payload: missing texture URL")
    }
}
