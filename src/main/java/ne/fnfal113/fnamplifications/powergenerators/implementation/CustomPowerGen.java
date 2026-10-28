package ne.fnfal113.fnamplifications.powergenerators.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun5.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun5.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun5.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun5.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun5.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun5.utils.LoreBuilder;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.utils.Utils;

public class CustomPowerGen extends SlimefunItem implements HologramOwner, EnergyNetProvider {

    private final Map<Location, Boolean> HOLO_CACHE = new HashMap<>();

    public CustomPowerGen(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int dayRate, int nightRate, int output, int storage) {
        super(itemGroup, item, recipeType, recipe);

        setConfigValues(dayRate, nightRate, output, storage);
        setLore(this.getItem());

        addItemHandler(
                toggleHologram(),
                new BlockBreakHandler(false, false) {
                    @Override
                    public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                        removeHologram(e.getBlock());
                        HOLO_CACHE.remove(e.getBlock().getLocation());
                    }
                },
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                        BlockStorage.addBlockInfo(e.getBlock(), "holo_status", "true");
                        HOLO_CACHE.put(e.getBlock().getLocation(), true);
                    }
                });
    }

    public void setLore(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;
        
        List<String> lore = meta.hasLore() && meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        
        lore.add(Utils.colorTranslator(LoreBuilder.powerBuffer(FNAmplifications.getInstance().getConfigManager().getCustomConfig("power-xpansion-settings").getInt(this.getId() + "." + "storage"))));
        lore.add(Utils.colorTranslator("&8\u21E8 &e\u26A1 &7" + Utils.powerFormat.format(FNAmplifications.getInstance().getConfigManager().getCustomConfig("power-xpansion-settings").getInt(this.getId() + "." + "dayrate")) + " J/t" + " (Day Rate)"));
        lore.add(Utils.colorTranslator("&8\u21E8 &e\u26A1 &7" + Utils.powerFormat.format(FNAmplifications.getInstance().getConfigManager().getCustomConfig("power-xpansion-settings").getInt(this.getId() + "." + "nightrate")) + " J/t" + " (Night Rate)"));
        
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public void setConfigValues(int dayRate, int nightRate, int output, int storage) {
        try {
            FNAmplifications.getInstance().getConfigManager().initializeConfig(this.getId(), "dayrate", dayRate, "power-xpansion-settings");
        FNAmplifications.getInstance().getConfigManager().initializeConfig(this.getId(), "nightrate", nightRate, "power-xpansion-settings");
        FNAmplifications.getInstance().getConfigManager().initializeConfig(this.getId(), "output", output, "power-xpansion-settings");
        FNAmplifications.getInstance().getConfigManager().initializeConfig(this.getId(), "storage", storage, "power-xpansion-settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nonnull
    @Override
    public Vector getHologramOffset(@Nonnull Block b) {
        return new Vector(0.5, 0.7, 0.5);
    }

    public BlockUseHandler toggleHologram(){
        return e -> {
            if(!e.getClickedBlock().isPresent()){
                return;
            }
            Block block = e.getClickedBlock().get();
            Location loc = block.getLocation();

            String holoStatus = BlockStorage.getLocationInfo(loc, "holo_status");
            boolean isCurrentlyActive = holoStatus != null ? Boolean.parseBoolean(holoStatus) : HOLO_CACHE.getOrDefault(loc, true);

            if (isCurrentlyActive) {
                HOLO_CACHE.put(loc, false);
                BlockStorage.addBlockInfo(loc, "holo_status", "false");
                removeHologram(block);
            } else {
                HOLO_CACHE.put(loc, true);
                BlockStorage.addBlockInfo(loc, "holo_status", "true");
                int charge = getCharge(loc);
                String chargeStr = charge <= 0 ? Utils.colorTranslator("&8" + charge) : Utils.colorTranslator("&a" + charge);
                updateHologram(block, Utils.colorTranslator("&eStored &a⚡: " + chargeStr));
            }
        };
    }

    @Override
    public int getGeneratedOutput(@Nonnull Location l, @Nonnull Config data) {
        final int stored = getCharge(l);

        boolean showHolo = HOLO_CACHE.computeIfAbsent(l, loc -> {
            String status = BlockStorage.getLocationInfo(loc, "holo_status");
            if (status != null) {
                return Boolean.parseBoolean(status);
            } else {
                BlockStorage.addBlockInfo(loc, "holo_status", "true");
                return true;
            }
        });

        if (showHolo) {
            String charge = stored <= 0 ? Utils.colorTranslator("&8" + stored) : Utils.colorTranslator("&a" + stored);
            updateHologram(l.getBlock(), Utils.colorTranslator("&eStored &a⚡: " + charge));
        }

        return stored < getCapacity() ? getGeneratingAmount(l.getBlock(), l.getWorld()) : 0;
    }

    @Override
    public boolean willExplode(@Nonnull Location l, @Nonnull Config data) {
        return false;
    }

    private int getGeneratingAmount(@Nonnull Block loc, @Nonnull World world) {
        if (world.getEnvironment() == World.Environment.NETHER) {
            return this.getDayRate();
        }
        if (world.getEnvironment() == World.Environment.THE_END) {
            return this.getNightRate();
        }

        if (world.isThundering() || world.hasStorm() || world.getTime() >= 13000
                || loc.getLocation().add(0, 1, 0).getBlock().getLightFromSky() != 15) {
            return this.getNightRate();
        } else {
            return this.getDayRate();
        }
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @Override
    public int getCapacity() {
        return FNAmplifications.getInstance().getConfigManager().getCustomConfig("power-xpansion-settings").getInt(this.getId() + "." + "storage");
    }

    public int getDayRate(){
        return FNAmplifications.getInstance().getConfigManager().getCustomConfig("power-xpansion-settings").getInt(this.getId() + "." + "dayrate");
    }

    public int getNightRate(){
        return FNAmplifications.getInstance().getConfigManager().getCustomConfig("power-xpansion-settings").getInt(this.getId() + "." + "nightrate");
    }

}