package io.github.thebusybiscuit.exoticgarden.util;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Internal CustomItemStack extending Bukkit's {@link ItemStack} directly.
 * Ensures binary compatibility across all Slimefun4 builds and eliminates VerifyError issues.
 */
public class CustomItemStack extends ItemStack {

    public CustomItemStack(@Nonnull ItemStack item, @Nullable String name, @Nullable String... lore) {
        super(item);
        applyMeta(name, lore);
    }

    public CustomItemStack(@Nonnull ItemStack item, int amount) {
        super(item);
        setAmount(amount);
    }

    public CustomItemStack(@Nonnull Material type, @Nullable String name, @Nullable String... lore) {
        super(type);
        applyMeta(name, lore);
    }

    public CustomItemStack(@Nonnull Material type, int amount) {
        super(type, amount);
    }

    private void applyMeta(@Nullable String name, @Nullable String... lore) {
        ItemMeta meta = getItemMeta();
        if (meta != null) {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }
            if (lore != null && lore.length > 0) {
                List<String> list = new ArrayList<>();
                for (String line : lore) {
                    list.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(list);
            }
            setItemMeta(meta);
        }
    }
}
