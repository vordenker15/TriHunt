package net.trilleo.mc.plugins.trihunt.guis.mainMenus

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.trilleo.mc.plugins.trihunt.enums.FillMode
import net.trilleo.mc.plugins.trihunt.managers.GameManager
import net.trilleo.mc.plugins.trihunt.registration.GUIManager
import net.trilleo.mc.plugins.trihunt.registration.PluginGUI
import net.trilleo.mc.plugins.trihunt.utils.TeamUtil
import net.trilleo.mc.plugins.trihunt.utils.itemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MainUI(private val plugin: JavaPlugin) : PluginGUI(
    id = "main",
    title = Component.text("TriHunt Main UI").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD),
    rows = 6,
    fillMode = FillMode.LIGHT
) {
    val slotIndex: Map<String, Int> = mapOf(
        "startButtonSlot" to 13,
        "creditsButtonSlot" to 15,
        "settingsButtonSlot" to 11,
        "teamSelectButtonSlot" to 31,
        "recipeBookButtonSlot" to 53,
        "worldSettingsButtonSlot" to 40,
        "closeButtonSlot" to 49
    )

    val authorUUID = "28468a45-b78c-4968-9782-f4f893216066"

    override fun setup(player: Player, inventory: Inventory) {
        val closeButton = itemStack(Material.BARRIER) {
            name("<bold><red>Close")
        }
        val startButton = itemStack(Material.GREEN_CONCRETE) {
            name("<bold><gradient:green:dark_green>Start</gradient></bold>")
            lore(
                " ",
                "<dark_gray>=====================",
                "<gray>Start the Manhunt",
                "<dark_gray>====================="
            )
            enchant(Enchantment.KNOCKBACK, 1)
            flag(ItemFlag.HIDE_ENCHANTS)
        }
        val creditsButton = itemStack(Material.PLAYER_HEAD) {
            name("<bold><light_purple>Credits")
            lore(
                " ",
                "<dark_gray>=====================",
                "<gray>View plugin contributors",
                "<dark_gray>====================="
            )
            meta {
                (this as SkullMeta)
                    .owningPlayer = Bukkit.getPlayer(UUID.fromString(authorUUID))
            }
        }
        val settingsButton = itemStack(Material.COMMAND_BLOCK) {
            name("<bold><dark_gray>Settings")
            lore(
                " ",
                "<dark_gray>=====================",
                "<gray>Configure game settings",
                "<dark_gray>====================="
            )
        }
        val teamSelectButton = itemStack(Material.DIAMOND_SWORD) {
            name("<bold><dark_blue>Team")
            lore(
                " ",
                "<dark_gray>=====================",
                "<gray>Select your team",
                "<dark_gray>=====================",
                "   ",
                "<white>Current team: ${TeamUtil.getPlayerTeam(player)?.displayName ?: "<dark_gray>None"}"
            )
            flag(ItemFlag.HIDE_ATTRIBUTES)
        }
        val recipeBookButton = itemStack(Material.ENCHANTED_BOOK) {
            name("<bold><dark_green>Recipe Book")
            lore(
                " ",
                "<dark_gray>=====================",
                "<gray>View plugin recipes",
                "<dark_gray>====================="
            )
        }
        val worldSettingsButton = itemStack(Material.GRASS_BLOCK) {
            name("<bold><green>World Settings")
            lore(
                " ",
                "<dark_gray>=====================",
                "<gray>Manage world and seed settings",
                "<dark_gray>====================="
            )
        }

        inventory.setItem(slotIndex.getValue("startButtonSlot"), startButton)
        inventory.setItem(slotIndex.getValue("creditsButtonSlot"), creditsButton)
        inventory.setItem(slotIndex.getValue("settingsButtonSlot"), settingsButton)
        inventory.setItem(slotIndex.getValue("teamSelectButtonSlot"), teamSelectButton)
        inventory.setItem(slotIndex.getValue("recipeBookButtonSlot"), recipeBookButton)
        inventory.setItem(slotIndex.getValue("worldSettingsButtonSlot"), worldSettingsButton)
        inventory.setItem(slotIndex.getValue("closeButtonSlot"), closeButton)
    }

    override fun onClick(event: InventoryClickEvent) {
        event.isCancelled = true
        val player = event.whoClicked as Player
        if (event.slot in slotIndex.values) {
            player.playSound(
                Sound.sound(Key.key("minecraft:ui.button.click"), Sound.Source.UI, 1f, 1f)
            )
        }

        if (event.slot == slotIndex.getValue("closeButtonSlot")) {
            player.closeInventory()
        }
        if (event.slot == slotIndex.getValue("settingsButtonSlot")) {
            GUIManager.open(player, "settings")
        }
        if (event.slot == slotIndex.getValue("creditsButtonSlot")) {
            GUIManager.open(player, "credits")
        }
        if (event.slot == slotIndex.getValue("teamSelectButtonSlot")) {
            GUIManager.open(player, "team-select")
        }
        if (event.slot == slotIndex.getValue("recipeBookButtonSlot")) {
            GUIManager.open(player, "recipe-book")
        }
        if (event.slot == slotIndex.getValue("worldSettingsButtonSlot")) {
            GUIManager.open(player, "world-settings")
        }
        if (event.slot == slotIndex.getValue("startButtonSlot")) {
            player.closeInventory()

            if (GameManager(plugin).checkCondition(player)) {
                GameManager(plugin).prepareGame()
            }
        }
    }
}