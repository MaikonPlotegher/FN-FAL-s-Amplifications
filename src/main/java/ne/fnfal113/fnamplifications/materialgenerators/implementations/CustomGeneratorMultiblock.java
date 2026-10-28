package ne.fnfal113.fnamplifications.materialgenerators.implementations;

import ne.fnfal113.fnamplifications.utils.Utils;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun5.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;

public class CustomGeneratorMultiblock extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public CustomGeneratorMultiblock(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.MULTIBLOCK, Utils.recipe(
                null, null, null,
                null, new ItemStack(Material.CHEST), null,
                null, CustomItemStack.create(Material.DIAMOND_BLOCK, "Any FN Material generator"), null
        ));
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();
            e.getPlayer().sendMessage("This item is just a dummy. You need to place the actual generator down.");
        };
    }
}
