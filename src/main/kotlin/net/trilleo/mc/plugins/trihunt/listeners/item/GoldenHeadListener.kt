package net.trilleo.mc.plugins.trihunt.listeners.item

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.trilleo.mc.plugins.trihunt.utils.PDCEntryUtil
import net.trilleo.mc.plugins.trihunt.utils.PDCUtil
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class GoldenHeadListener(private val plugin: JavaPlugin) : Listener {

    private val cooldownMillis = 1000L
    private val lastConsumeAt = mutableMapOf<UUID, Long>()

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item

        if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
            if (item == null) return
            val identifier = PDCUtil.get(
                item,
                PDCEntryUtil.PDCKey(plugin).itemIdentifierKey,
                PersistentDataType.STRING
            )

            val isGoldenHead = identifier == PDCEntryUtil.PDCValue().goldenHeadItemIdentifier
            val isEnchantedGoldenHead = identifier == PDCEntryUtil.PDCValue().enchantedGoldenHeadItemIdentifier

            if (isGoldenHead || isEnchantedGoldenHead) {
                event.isCancelled = true

                val now = System.currentTimeMillis()
                val last = lastConsumeAt[player.uniqueId] ?: 0L
                if (now - last < cooldownMillis) return
                lastConsumeAt[player.uniqueId] = now

                player.playSound(Sound.sound(Key.key("minecraft:entity.player.burp"), Sound.Source.MASTER, 1.0f, 1.0f))

                if (isGoldenHead) {
                    // Golden Apple effects: Regeneration II (5s), Absorption I (2m)
                    // Request: 2x duration
                    // Regen II: 5s * 20 * 2 = 200 ticks
                    // Absorption I: 2m * 60 * 20 * 2 = 4800 ticks
                    player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 200, 1, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 4800, 0, false, false))
                } else {
                    // Enchanted Golden Apple effects: Regeneration II (20s), Absorption IV (2m), Resistance I (5m), Fire Resistance I (5m)
                    // Request: 2x duration
                    // Regen II: 20s * 20 * 2 = 800 ticks
                    // Absorption IV: 2m * 60 * 20 * 2 = 4800 ticks
                    // Resistance I: 5m * 60 * 20 * 2 = 12000 ticks
                    // Fire Resistance I: 5m * 60 * 20 * 2 = 12000 ticks
                    player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 800, 1, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 4800, 3, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 12000, 0, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0, false, false))
                }

                // Add saturation as well (Golden Apple gives some)
                player.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, 100, 1, false, false))

                if (player.gameMode == GameMode.CREATIVE) return
                item.subtract(1)
            }
        }
    }
}