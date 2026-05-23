package net.trilleo.mc.plugins.trihunt.managers

import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class WorldManager(private val plugin: JavaPlugin) {

    private val overworldName = "trihunt_overworld"
    private val netherName = "trihunt_nether"
    private val endName = "trihunt_end"

    fun ensureLobbyPlatform() {
        val world = Bukkit.getWorlds()[0] // Main world
        val center = Location(world, 0.0, 100.0, 0.0)

        for (x in -10..10) {
            for (z in -10..10) {
                world.getBlockAt(center.clone().add(x.toDouble(), 0.0, z.toDouble())).type = Material.SMOOTH_STONE
            }
        }
        world.spawnLocation = center.clone().add(0.0, 1.0, 0.0)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.worldBorder.center = center
        world.worldBorder.size = 21.0
    }

    fun resetWorlds(seed: Long?, onComplete: (World) -> Unit) {
        // Teleport everyone to lobby first
        val lobby = Bukkit.getWorlds()[0].spawnLocation
        Bukkit.getOnlinePlayers().forEach { it.teleport(lobby) }

        // Unload worlds on main thread
        unloadWorld(overworldName)
        unloadWorld(netherName)
        unloadWorld(endName)

        // Delete folders asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            deleteFolder(overworldName)
            deleteFolder(netherName)
            deleteFolder(endName)

            // Back to main thread for creation
            Bukkit.getScheduler().runTask(plugin, Runnable {
                val overworld = createWorld(overworldName, World.Environment.NORMAL, seed)
                createWorld(netherName, World.Environment.NETHER, seed)
                createWorld(endName, World.Environment.THE_END, seed)

                onComplete(overworld)
            })
        })
    }

    private fun unloadWorld(name: String) {
        val world = Bukkit.getWorld(name)
        if (world != null) {
            Bukkit.unloadWorld(world, false)
        }
    }

    private fun deleteFolder(name: String) {
        val folder = File(Bukkit.getWorldContainer(), name)
        if (folder.exists()) {
            deleteDirectory(folder)
        }
    }

    private fun createWorld(name: String, environment: World.Environment, seed: Long?): World {
        val creator = WorldCreator(name)
        creator.environment(environment)
        if (seed != null) {
            creator.seed(seed)
        }
        return creator.createWorld() ?: throw IllegalStateException("Could not create world $name")
    }

    private fun deleteWorld(name: String) {
        val world = Bukkit.getWorld(name)
        if (world != null) {
            Bukkit.unloadWorld(world, false)
        }
        val folder = File(Bukkit.getWorldContainer(), name)
        if (folder.exists()) {
            deleteDirectory(folder)
        }
    }

    private fun deleteDirectory(directory: File) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    deleteDirectory(file)
                } else {
                    file.delete()
                }
            }
        }
        directory.delete()
    }
}
