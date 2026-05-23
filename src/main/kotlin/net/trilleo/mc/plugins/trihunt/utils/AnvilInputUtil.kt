package net.trilleo.mc.plugins.trihunt.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.trilleo.mc.plugins.trihunt.registration.GUIManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin

class AnvilInputUtil(private val plugin: JavaPlugin) : Listener {

    fun open(player: Player, title: String, defaultText: String, onComplete: (String) -> Unit) {
        val inventory = Bukkit.createInventory(player, InventoryType.ANVIL, Component.text(title))

        val item = itemStack(Material.NAME_TAG) {
            name(defaultText)
        }

        inventory.setItem(0, item)

        val listener = object : Listener {
            @EventHandler
            fun onClick(event: InventoryClickEvent) {
                if (event.inventory != inventory) return

                if (event.rawSlot == 2) {
                    event.isCancelled = true
                    val result = event.currentItem?.itemMeta?.displayName()?.let {
                        PlainTextComponentSerializer.plainText().serialize(it)
                    } ?: defaultText

                    HandlerList.unregisterAll(this)
                    player.closeInventory()
                    onComplete(result)
                }
            }

            @EventHandler
            fun onClose(event: InventoryCloseEvent) {
                if (event.inventory == inventory) {
                    HandlerList.unregisterAll(this)
                }
            }
        }

        plugin.server.pluginManager.registerEvents(listener, plugin)
        player.openInventory(inventory)
    }
}
