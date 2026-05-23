package net.trilleo.mc.plugins.trihunt.guis.configMenus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.trilleo.mc.plugins.trihunt.data.ServerDataManager
import net.trilleo.mc.plugins.trihunt.enums.FillMode
import net.trilleo.mc.plugins.trihunt.registration.GUIManager
import net.trilleo.mc.plugins.trihunt.registration.PluginGUI
import net.trilleo.mc.plugins.trihunt.utils.AnvilInputUtil
import net.trilleo.mc.plugins.trihunt.utils.itemStack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin

class WorldSettingsUI(private val plugin: JavaPlugin) : PluginGUI(
    id = "world-settings",
    title = Component.text("World Settings").color(NamedTextColor.GREEN),
    rows = 3,
    fillMode = FillMode.DARK
) {
    override fun setup(player: Player, inventory: Inventory) {
        val serverData = ServerDataManager.get()
        val mode = serverData.getString("seedMode", "RANDOM")
        val customSeed = serverData.getString("customSeed", "")

        val randomButton = itemStack(Material.GRASS_BLOCK) {
            name("<bold><green>Random Seed")
            lore("<gray>Generate a random map each game.", if (mode == "RANDOM") "<yellow>Selected" else "")
        }

        val sameButton = itemStack(Material.REPEATING_COMMAND_BLOCK) {
            name("<bold><blue>Same Seed")
            lore("<gray>Reuse the seed from the last game.", if (mode == "SAME") "<yellow>Selected" else "")
        }

        val customButton = itemStack(Material.NAME_TAG) {
            name("<bold><light_purple>Custom Seed")
            lore(
                "<gray>Enter a specific seed to use.",
                "<gray>Selected Seed: <white>${if (customSeed.isEmpty()) "None" else customSeed}",
                if (mode == "CUSTOM") "<yellow>Selected" else ""
            )
        }

        val backButton = itemStack(Material.ARROW) {
            name("<red>Back")
        }

        inventory.setItem(11, randomButton)
        inventory.setItem(13, sameButton)
        inventory.setItem(15, customButton)
        inventory.setItem(22, backButton)
    }

    override fun onClick(event: InventoryClickEvent) {
        event.isCancelled = true
        val player = event.whoClicked as Player
        val serverData = ServerDataManager.get()

        when (event.slot) {
            11 -> {
                serverData.set("seedMode", "RANDOM")
                GUIManager.open(player, id)
            }
            13 -> {
                serverData.set("seedMode", "SAME")
                GUIManager.open(player, id)
            }
            15 -> {
                AnvilInputUtil(plugin).open(player, "Enter Seed", "Seed") { input ->
                    serverData.set("customSeed", input)
                    serverData.set("seedMode", "CUSTOM")
                    GUIManager.open(player, id)
                }
            }
            22 -> {
                GUIManager.open(player, "main")
            }
        }
    }
}
