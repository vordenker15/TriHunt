package net.trilleo.mc.plugins.trihunt.listeners.game

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent

class PortalListener : Listener {

    private val overworldName = "trihunt_overworld"
    private val netherName = "trihunt_nether"
    private val endName = "trihunt_end"

    @EventHandler
    fun onPlayerPortal(event: PlayerPortalEvent) {
        val fromWorld = event.from.world
        val isNether = event.cause == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL

        val newToWorld = getTargetWorld(fromWorld, isNether)
        if (newToWorld != null) {
            event.to?.world = newToWorld
        }
    }

    @EventHandler
    fun onEntityPortal(event: EntityPortalEvent) {
        val fromWorld = event.from.world
        val toWorld = event.to?.world ?: return
        val isNether = toWorld.environment == World.Environment.NETHER

        val newToWorld = getTargetWorld(fromWorld, isNether)
        if (newToWorld != null) {
            event.to?.world = newToWorld
        }
    }

    private fun getTargetWorld(fromWorld: World, isNether: Boolean): World? {
        return when (fromWorld.name) {
            overworldName -> if (isNether) Bukkit.getWorld(netherName) else Bukkit.getWorld(endName)
            netherName -> Bukkit.getWorld(overworldName)
            endName -> Bukkit.getWorld(overworldName)
            else -> null
        }
    }
}
