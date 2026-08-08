package io.github.thebusybiscuit.exoticgarden.machines;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.exoticgarden.util.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class MagicalHarvester extends SimpleSlimefunItem<BlockTicker> implements EGInventoryBlock, EnergyNetComponent {

    public enum Tier {
        ONE(2, 24, 128),
        TWO(4, 48, 256);

        private final int radius;
        private final int energyConsumption;
        private final int capacity;

        Tier(int radius, int energyConsumption, int capacity) {
            this.radius = radius;
            this.energyConsumption = energyConsumption;
            this.capacity = capacity;
        }

        public int getRadius() {
            return radius;
        }

        public int getEnergyConsumption() {
            return energyConsumption;
        }

        public int getCapacity() {
            return capacity;
        }
    }

    private final Tier tier;
    private static final int[] BORDER = {
        0, 1, 2, 3, 5, 6, 7, 8,
        9, 10, 11, 12, 13, 14, 15, 16, 17,
        18, 27, 36,
        26, 35, 44,
        45, 46, 47, 48, 49, 50, 51, 52, 53
    };
    private static final int STATUS_SLOT = 4;
    private static final int[] OUTPUT_SLOTS = {
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    public MagicalHarvester(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Tier tier) {
        super(category, item, recipeType, recipe);
        this.tier = tier;

        createPreset(this, this::constructMenu);
        addItemHandler(onBreak());
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(STATUS_SLOT, new CustomItemStack(Material.REDSTONE_TORCH, "&cEstado: Esperando energía"), ChestMenuUtils.getEmptyClickHandler());

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {
                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                    return cursor == null || cursor.getType() == Material.AIR;
                }
            });
        }
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return OUTPUT_SLOTS;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return tier.getCapacity();
    }

    public BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                Block b = e.getBlock();
                BlockMenu inv = BlockStorage.getInventory(b);
                if (inv != null) {
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }
            }
        };
    }

    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {
            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                Location loc = b.getLocation();
                BlockMenu menu = BlockStorage.getInventory(b);
                if (menu == null) return;

                int charge = getCharge(loc);
                if (charge < tier.getEnergyConsumption()) {
                    if (menu.hasViewer()) {
                        menu.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.REDSTONE_TORCH, "&cSin energía", "&7Energía requerida: &e" + tier.getEnergyConsumption() + " J/s", "&7Buffer actual: &c" + charge + " J"));
                    }
                    return;
                }

                int radius = tier.getRadius();
                boolean harvestedAny = false;

                // Scan 3D area around the machine
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        for (int y = -1; y <= 2; y++) {
                            Block target = b.getRelative(x, y, z);
                            if (target.equals(b)) continue;

                            ItemStack harvested = ExoticGarden.harvestPlant(target);
                            if (harvested != null) {
                                if (menu.fits(harvested, getOutputSlots())) {
                                    menu.pushItem(harvested, getOutputSlots());
                                    harvestedAny = true;
                                } else {
                                    target.getWorld().dropItemNaturally(target.getLocation(), harvested);
                                    harvestedAny = true;
                                }
                            }
                        }
                    }
                }

                if (harvestedAny) {
                    removeCharge(loc, tier.getEnergyConsumption());
                    if (menu.hasViewer()) {
                        menu.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.LIME_DYE, "&aOperando activamente", "&7Cosechando plantas en área &e" + (radius * 2 + 1) + "x" + (radius * 2 + 1), "&7Consumo: &e" + tier.getEnergyConsumption() + " J/s"));
                    }
                } else {
                    if (menu.hasViewer()) {
                        menu.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.CYAN_DYE, "&bEn espera", "&7Escaneando área &e" + (radius * 2 + 1) + "x" + (radius * 2 + 1), "&7Consumo: &e" + tier.getEnergyConsumption() + " J/s"));
                    }
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }
}
