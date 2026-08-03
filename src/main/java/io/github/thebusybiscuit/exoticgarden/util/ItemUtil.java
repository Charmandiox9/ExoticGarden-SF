package io.github.thebusybiscuit.exoticgarden.util;

import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class ItemUtil {

    private ItemUtil() {}

    public static void consumeItem(@Nullable ItemStack item, boolean replaceWithContainer) {
        if (item == null || item.getType() == Material.AIR || item.getAmount() <= 0) {
            return;
        }

        if (replaceWithContainer) {
            if (item.getType() == Material.MILK_BUCKET || item.getType() == Material.WATER_BUCKET || item.getType() == Material.LAVA_BUCKET) {
                item.setType(Material.BUCKET);
                return;
            } else if (item.getType() == Material.POTION || item.getType() == Material.HONEY_BOTTLE) {
                item.setType(Material.GLASS_BOTTLE);
                return;
            }
        }

        item.subtract(1);
    }
}
