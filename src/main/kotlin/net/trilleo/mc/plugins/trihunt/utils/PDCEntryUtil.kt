package net.trilleo.mc.plugins.trihunt.utils

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

class PDCEntryUtil {
    // Namespaced Keys
    class PDCKey(private val plugin: JavaPlugin) {
        // Key for identifying plugin items
        val itemIdentifierKey = NamespacedKey(plugin, "itemIdentifier")

        // Key for Silex enriched items
        val silexEnrichedItemIdentifierKey = NamespacedKey(plugin, "silexEnrichedItemIdentifier")
    }

    // Values
    class PDCValue {
        // itemIdentifier - Main Item
        val mainItemIdentifier = "main-item"

        // itemIdentifier - Hunter Compass
        val compassItemIdentifier = "hunter-compass"

        // itemIdentifier - Assassin Sword
        val assassinSwordItemIdentifier = "assassin-sword"

        // itemIdentifier - Bridge Egg
        val bridgeEggItemIdentifier = "bridge-egg"

        // itemIdentifier - Shootable Fireball
        val shootableFireballItemIdentifier = "shootable-fireball"

        // itemIdentifier - Golden Head
        val goldenHeadItemIdentifier = "golden-head"

        // itemIdentifier - Enchanted Golden Head
        val enchantedGoldenHeadItemIdentifier = "enchanted-golden-head"

        // itemIdentifier - Silex
        val silexItemIdentifier = "silex"

        // itemIdentifier - Throwable TNT
        val throwableTNTItemIdentifier = "throwable-tnt"

        // itemIdentifier - Knockback Stick
        val knockbackStickItemIdentifier = "knockback-stick"

        // silexEnrichedItemIdentifier
        val isSilexEnrichedItemIdentifier = true
    }
}