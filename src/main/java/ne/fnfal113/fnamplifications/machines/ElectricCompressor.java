package ne.fnfal113.fnamplifications.machines;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

import ne.fnfal113.fnamplifications.items.FNAmpItems;
import ne.fnfal113.fnamplifications.utils.Utils;

public class ElectricCompressor extends AContainer implements RecipeDisplayItem {

    public ElectricCompressor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 4);

        for (MachineRecipe recipe : recipes) {
            displayRecipes.add(recipe.getInput()[0]);
            displayRecipes.add(recipe.getOutput()[0]);
        }

        return displayRecipes;
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(16, Utils.recipe(new SlimefunItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 8)),
                Utils.recipe(new SlimefunItemStack(SlimefunItems.REINFORCED_PLATE, 1)));
        registerRecipe(16, Utils.recipe(new SlimefunItemStack(SlimefunItems.STEEL_INGOT, 8)),
                Utils.recipe(new SlimefunItemStack(SlimefunItems.STEEL_PLATE, 1)));
        registerRecipe(16, Utils.recipe(new ItemStack(Material.COAL_BLOCK, 11)),
                Utils.recipe(new SlimefunItemStack(SlimefunItems.CARBON, 12)));
        registerRecipe(16, Utils.recipe(new ItemStack(Material.COAL, 8)),
                Utils.recipe(new SlimefunItemStack(SlimefunItems.CARBON, 1)));
        registerRecipe(16, Utils.recipe(new ItemStack(Material.CHARCOAL, 4)),
                Utils.recipe(new ItemStack(Material.COAL, 1)));
        registerRecipe(16, Utils.recipe(new SlimefunItemStack(SlimefunItems.SMALL_URANIUM, 4)),
                Utils.recipe(new SlimefunItemStack(SlimefunItems.URANIUM, 1)));
        registerRecipe(16, Utils.recipe(new SlimefunItemStack(SlimefunItems.URANIUM, 64)),
                Utils.recipe(new SlimefunItemStack(SlimefunItems.NEPTUNIUM, 1)));
        registerRecipe(16, Utils.recipe(new SlimefunItemStack(SlimefunItems.MAGNESIUM_SALT, 8)),
                Utils.recipe(new SlimefunItemStack(SlimefunItems.MAGNESIUM_INGOT, 1)));
    }

    @Nonnull
    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_COMPRESSOR";
    }

    @Override
    public ItemStack getProgressBar() {
        return FNAmpItems.COMPRESSOR_PART.item();
    }
}
