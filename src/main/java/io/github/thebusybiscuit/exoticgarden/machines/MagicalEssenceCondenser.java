package io.github.thebusybiscuit.exoticgarden.machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.exoticgarden.items.MagicalEssence;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.exoticgarden.util.CustomItemStack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class MagicalEssenceCondenser extends AContainer implements RecipeDisplayItem {

    private static final int ENERGY_CONSUMPTION = 20;
    private static final int CAPACITY = 256;
    private static final int PROCESSING_TIME = 4;

    public MagicalEssenceCondenser(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        addItemHandler(onBreak());
    }

    @Override
    protected void registerDefaultRecipes() {
        // Will be populated dynamically after essences are registered
    }

    public void registerEssenceRecipes(List<MagicalEssence> essences) {
        for (MagicalEssence essence : essences) {
            ItemStack output = essence.getRecipeOutput();
            if (output != null) {
                registerRecipe(PROCESSING_TIME, new ItemStack[] { new CustomItemStack(essence.getItem(), 8) }, new ItemStack[] { output.clone() });
            }
        }
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);
        for (MachineRecipe recipe : recipes) {
            displayRecipes.add(recipe.getInput()[0]);
            displayRecipes.add(recipe.getOutput()[recipe.getOutput().length - 1]);
        }
        return displayRecipes;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.RESPAWN_ANCHOR);
    }

    @Override
    public String getInventoryTitle() {
        return "&6Condensador de Esencias Mágicas";
    }

    @Override
    public String getMachineIdentifier() {
        return "MAGICAL_ESSENCE_CONDENSER";
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    public BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                Block b = e.getBlock();
                BlockMenu inv = BlockStorage.getInventory(b);
                if (inv != null) {
                    inv.dropItems(b.getLocation(), getInputSlots());
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }
            }
        };
    }
}
