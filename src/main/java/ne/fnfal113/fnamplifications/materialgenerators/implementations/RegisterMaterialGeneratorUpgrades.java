package ne.fnfal113.fnamplifications.materialgenerators.implementations;

import ne.fnfal113.fnamplifications.utils.Utils;

import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import ne.fnfal113.fnamplifications.items.FNAmpItems;
import ne.fnfal113.fnamplifications.materialgenerators.upgrades.FastProduce;
import ne.fnfal113.fnamplifications.materialgenerators.upgrades.RepairItem;
import ne.fnfal113.fnamplifications.multiblocks.FnAssemblyStation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RegisterMaterialGeneratorUpgrades {

    public static void setup(SlimefunAddon instance) {
        new RepairItem(
                FNAmpItems.MATERIAL_GENERATORS_UPGRADES,
                FNAmpItems.FN_MAT_GEN_UPGRADES_REPAIR_ITEM,
                FnAssemblyStation.RECIPE_TYPE,
                Utils.recipe(
                        SlimefunItems.BRONZE_INGOT, null, SlimefunItems.ALUMINUM_INGOT,
                        null, new ItemStack(Material.DIAMOND_PICKAXE), null,
                        SlimefunItems.STEEL_INGOT, null, SlimefunItems.BRASS_INGOT)
        ).register(instance);

        new FastProduce(
                FNAmpItems.MATERIAL_GENERATORS_UPGRADES,
                FNAmpItems.FN_MAT_GEN_UPGRADES_FAST_PRODUCE,
                FnAssemblyStation.RECIPE_TYPE,
                Utils.recipe(
                        new ItemStack(Material.DIAMOND_SHOVEL), SlimefunItems.DURALUMIN_INGOT, new ItemStack(Material.DIAMOND_SHOVEL),
                        SlimefunItems.HARDENED_METAL_INGOT, null, SlimefunItems.HARDENED_METAL_INGOT,
                        new ItemStack(Material.DIAMOND_PICKAXE), SlimefunItems.BILLON_INGOT, new ItemStack(Material.DIAMOND_PICKAXE))
        ).register(instance);
    }
}
