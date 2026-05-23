package net.trilleo.mc.plugins.trihunt.recipes

import net.trilleo.mc.plugins.trihunt.items.utilityItems.EnchantedGoldenHeadItem
import net.trilleo.mc.plugins.trihunt.registration.PluginRecipe
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class EnchantedGoldenHeadRecipe : PluginRecipe("enchanted-golden-head-craft") {
    override fun build(plugin: JavaPlugin): Recipe {
        val recipe = ShapedRecipe(namespacedKey(plugin), EnchantedGoldenHeadItem(plugin).create())
        recipe.shape(
            "GGG",
            "GPG",
            "GGG"
        )

        recipe.setIngredient('G', vanillaChoice(Material.GOLD_BLOCK))
        recipe.setIngredient('P', vanillaChoice(Material.GOLDEN_APPLE))

        return recipe
    }
}
