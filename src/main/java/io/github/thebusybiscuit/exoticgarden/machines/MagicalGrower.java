package io.github.thebusybiscuit.exoticgarden.machines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.exoticgarden.Berry;
import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import io.github.thebusybiscuit.exoticgarden.listeners.PlantsListener;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.exoticgarden.util.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class MagicalGrower extends SimpleSlimefunItem<BlockTicker> implements EGInventoryBlock, EnergyNetComponent {

    public enum Tier {
        ONE(2, 32, 128),
        TWO(4, 64, 256);

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
        9, 10, 11, 15, 16, 17,
        18, 19, 20, 24, 25, 26,
        27, 28, 29, 33, 34, 35,
        36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int STATUS_SLOT = 4;
    private static final int[] INPUT_SLOTS = {
        12, 13, 14,
        21, 22, 23,
        30, 31, 32
    };

    public MagicalGrower(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Tier tier) {
        super(category, item, recipeType, recipe);
        this.tier = tier;

        createPreset(this, this::constructMenu);
        addItemHandler(onBreak());
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(STATUS_SLOT, new CustomItemStack(Material.REDSTONE_TORCH, "&cEstado: Esperando energía"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    public int[] getInputSlots() {
        return INPUT_SLOTS;
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return tier.getCapacity();
    }

    private int findFertilizerSlot(BlockMenu menu) {
        for (int slot : getInputSlots()) {
            ItemStack is = menu.getItemInSlot(slot);
            if (is != null && is.getAmount() > 0) {
                if (is.getType() == Material.BONE_MEAL || is.isSimilar(SlimefunItems.FERTILIZER.item()) || is.getType() == Material.BONE_BLOCK) {
                    return slot;
                }
            }
        }
        return -1;
    }

    public BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                Block b = e.getBlock();
                BlockMenu inv = BlockStorage.getInventory(b);
                if (inv != null) {
                    inv.dropItems(b.getLocation(), getInputSlots());
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

                int fertilizerSlot = findFertilizerSlot(menu);
                if (fertilizerSlot == -1) {
                    if (menu.hasViewer()) {
                        menu.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.YELLOW_DYE, "&eEsperando Fertilizante", "&7Coloca Polvo de Hueso o Fertilizante", "&7en los slots de entrada."));
                    }
                    return;
                }

                int radius = tier.getRadius();
                int grownCount = 0;

                // Collect candidate blocks in the full 3D area
                List<Block> candidates = new ArrayList<>();
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        for (int y = -1; y <= 2; y++) {
                            Block target = b.getRelative(x, y, z);
                            if (!target.equals(b)) {
                                candidates.add(target);
                            }
                        }
                    }
                }
                Collections.shuffle(candidates);

                // Grow all candidate plants in area as long as fertilizer lasts
                for (Block target : candidates) {
                    fertilizerSlot = findFertilizerSlot(menu);
                    if (fertilizerSlot == -1) {
                        break;
                    }

                    SlimefunItem item = BlockStorage.check(target);
                    if (item != null) {
                        for (Berry berry : ExoticGarden.getBerries()) {
                            if (item.getId().equalsIgnoreCase(berry.toBush())) {
                                // Consume 1 fertilizer
                                menu.consumeItem(fertilizerSlot, 1);
                                grownCount++;

                                // Apply growth
                                PlantsListener.growBerryPlant(target, berry);
                                target.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, target.getLocation().clone().add(0.5, 0.5, 0.5), 4, 0.2, 0.2, 0.2);
                                target.getWorld().playSound(target.getLocation(), Sound.ITEM_BONE_MEAL_USE, 0.5f, 1f);
                                break;
                            }
                        }
                    }
                }

                if (grownCount > 0) {
                    removeCharge(loc, tier.getEnergyConsumption());
                    if (menu.hasViewer()) {
                        menu.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.LIME_DYE, "&aFertilizando activamente", "&7Plantas aceleradas: &e" + grownCount + " &7en área &e" + (radius * 2 + 1) + "x" + (radius * 2 + 1), "&7Consumo: &e" + tier.getEnergyConsumption() + " J/s"));
                    }
                } else {
                    if (menu.hasViewer()) {
                        menu.replaceExistingItem(STATUS_SLOT, new CustomItemStack(Material.CYAN_DYE, "&bEn espera (Todas maduras)", "&7Escaneando área &e" + (radius * 2 + 1) + "x" + (radius * 2 + 1), "&7Fertilizante listo"));
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
