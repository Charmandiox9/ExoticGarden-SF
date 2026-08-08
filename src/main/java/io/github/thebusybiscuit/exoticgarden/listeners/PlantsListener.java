package io.github.thebusybiscuit.exoticgarden.listeners;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.exoticgarden.Berry;
import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import io.github.thebusybiscuit.exoticgarden.PlantType;
import io.github.thebusybiscuit.exoticgarden.Tree;
import io.github.thebusybiscuit.exoticgarden.schematics.Schematic;
import io.github.thebusybiscuit.exoticgarden.items.BonemealableItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class PlantsListener implements Listener {

    private final Config cfg;
    private final ExoticGarden plugin;
    private static final BlockFace[] FACES = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

    public PlantsListener(ExoticGarden plugin) {
        this.plugin = plugin;
        cfg = plugin.getCfg();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGrow(StructureGrowEvent e) {
        World world = e.getLocation().getWorld();
        int chunkX = e.getLocation().getBlockX() >> 4;
        int chunkZ = e.getLocation().getBlockZ() >> 4;

        if (world != null && world.isChunkGenerated(chunkX, chunkZ)) {
            growStructure(e);
        } else if (world != null) {
            world.getChunkAtAsync(chunkX, chunkZ).thenRun(() -> growStructure(e));
        } else {
            growStructure(e);
        }
    }

    @EventHandler
    public void onGenerate(ChunkPopulateEvent e) {
        final World world = e.getWorld();

        if (BlockStorage.getStorage(world) == null) {
            return;
        }

        if (!Slimefun.getWorldSettingsService().isWorldEnabled(world)) {
            return;
        }

        if (!cfg.getStringList("world-blacklist").contains(world.getName())) {
            Random random = ThreadLocalRandom.current();

            final int worldLimit = getWorldBorder(world);

            if (random.nextInt(100) < cfg.getInt("chances.BUSH")) {
                Berry berry = ExoticGarden.getBerries().get(random.nextInt(ExoticGarden.getBerries().size()));
                if (berry.getType().equals(PlantType.ORE_PLANT)) return;

                int chunkX = e.getChunk().getX();
                int chunkZ = e.getChunk().getZ();

                int x = chunkX * 16 + random.nextInt(16);
                int z = chunkZ * 16 + random.nextInt(16);

                if ((x < worldLimit && x > -worldLimit) && (z < worldLimit && z > -worldLimit)) {
                    if (world.isChunkGenerated(chunkX, chunkZ)) {
                        growBush(e, x, z, berry, random, true);
                    } else {
                        world.getChunkAtAsync(chunkX, chunkZ).thenRun(() -> growBush(e, x, z, berry, random, true));
                    }
                }
            }
            else if (random.nextInt(100) < cfg.getInt("chances.TREE")) {
                Tree tree = ExoticGarden.getTrees().get(random.nextInt(ExoticGarden.getTrees().size()));

                int chunkX = e.getChunk().getX();
                int chunkZ = e.getChunk().getZ();

                int x = chunkX * 16 + random.nextInt(16);
                int z = chunkZ * 16 + random.nextInt(16);

                if ((x < worldLimit && x > -worldLimit) && (z < worldLimit && z > -worldLimit)) {
                    if (world.isChunkGenerated(chunkX, chunkZ)) {
                        pasteTree(e, x, z, tree);
                    } else {
                        world.getChunkAtAsync(chunkX, chunkZ).thenRun(() -> pasteTree(e, x, z, tree));
                    }
                }
            }
        }
    }

    private int getWorldBorder(World world) {
        return (int) world.getWorldBorder().getSize();
    }

    private void growStructure(StructureGrowEvent e) {
        SlimefunItem item = BlockStorage.check(e.getLocation().getBlock());

        if (item != null) {
            e.setCancelled(true);
            for (Tree tree : ExoticGarden.getTrees()) {
                if (item.getId().equalsIgnoreCase(tree.getSapling())) {
                    BlockStorage.clearBlockInfo(e.getLocation());
                    Schematic.pasteSchematic(e.getLocation(), tree);
                    return;
                }
            }

            for (Berry berry : ExoticGarden.getBerries()) {
                if (item.getId().equalsIgnoreCase(berry.toBush())) {
                    growBerryPlant(e.getLocation().getBlock(), berry);
                    break;
                }
            }
        }
    }

    public static void growBerryPlant(Block block, Berry berry) {
        Location loc = block.getLocation();
        switch (berry.getType()) {
        case BUSH:
            block.setType(Material.OAK_LEAVES);
            BlockStorage.deleteLocationInfoUnsafely(loc, false);
            BlockStorage.store(block, berry.getHeadItem());
            loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.OAK_LEAVES);
            break;
        case ORE_PLANT:
        case DOUBLE_PLANT:
            Block blockAbove = block.getRelative(BlockFace.UP);
            SlimefunItem itemAbove = BlockStorage.check(blockAbove);
            if (itemAbove != null) return;

            if (!Tag.SAPLINGS.isTagged(blockAbove.getType()) && !Tag.LEAVES.isTagged(blockAbove.getType())) {
                switch (blockAbove.getType()) {
                case AIR:
                case CAVE_AIR:
                case SNOW:
                    break;
                default:
                    return;
                }
            }

            BlockStorage.store(blockAbove, berry.getHeadItem());
            block.setType(Material.OAK_LEAVES);
            blockAbove.setType(Material.PLAYER_HEAD);
            if (blockAbove.getBlockData() instanceof Rotatable rotatable) {
                rotatable.setRotation(FACES[ThreadLocalRandom.current().nextInt(FACES.length)]);
                blockAbove.setBlockData(rotatable);
            }

            try {
                PlayerHead.setSkin(blockAbove, PlayerSkin.fromHashCode(berry.getTexture()), true);
            } catch (Exception ignored) {}

            BlockStorage.deleteLocationInfoUnsafely(loc, false);
            BlockStorage.store(block, berry.getHeadItem());
            loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.OAK_LEAVES);
            break;
        default:
            block.setType(Material.PLAYER_HEAD);
            if (block.getBlockData() instanceof Rotatable s) {
                s.setRotation(FACES[ThreadLocalRandom.current().nextInt(FACES.length)]);
                block.setBlockData(s);
            }

            try {
                PlayerHead.setSkin(block, PlayerSkin.fromHashCode(berry.getTexture()), true);
            } catch (Exception ignored) {}

            BlockStorage.deleteLocationInfoUnsafely(loc, false);
            BlockStorage.store(block, berry.getHeadItem());
            loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.OAK_LEAVES);
            break;
        }
    }

    private void pasteTree(ChunkPopulateEvent e, int x, int z, Tree tree) {
        for (int y = e.getWorld().getMaxHeight(); y > 30; y--) {
            Block current = e.getWorld().getBlockAt(x, y, z);
            if (!current.getType().isSolid() && current.getType() != Material.WATER && current.getType() != Material.SEAGRASS && current.getType() != Material.TALL_SEAGRASS && !(current.getBlockData() instanceof Waterlogged && ((Waterlogged) current.getBlockData()).isWaterlogged()) && tree.isSoil(current.getRelative(0, -1, 0).getType()) && isFlat(current)) {
                Schematic.pasteSchematic(new Location(e.getWorld(), x, y, z), tree);
                break;
            }
        }
    }

    private void growBush(ChunkPopulateEvent e, int x, int z, Berry berry, Random random, boolean isPaper) {
        for (int y = e.getWorld().getMaxHeight(); y > 30; y--) {
            Block current = e.getWorld().getBlockAt(x, y, z);
            if (!current.getType().isSolid() && current.getType() != Material.WATER && berry.isSoil(current.getRelative(BlockFace.DOWN).getType())) {
                BlockStorage.store(current, berry.getHeadItem());
                switch (berry.getType()) {
                case BUSH:
                    if (isPaper) {
                        current.setType(Material.OAK_LEAVES);
                    }
                    else {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> current.setType(Material.OAK_LEAVES));
                    }
                    break;
                case FRUIT:
                    if (isPaper) {
                        current.setType(Material.PLAYER_HEAD);
                        Rotatable s = (Rotatable) current.getBlockData();
                        s.setRotation(FACES[random.nextInt(FACES.length)]);
                        current.setBlockData(s);
                        PlayerHead.setSkin(current, PlayerSkin.fromHashCode(berry.getTexture()), true);
                    }
                    else {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            current.setType(Material.PLAYER_HEAD);
                            Rotatable s = (Rotatable) current.getBlockData();
                            s.setRotation(FACES[random.nextInt(FACES.length)]);
                            current.setBlockData(s);
                            PlayerHead.setSkin(current, PlayerSkin.fromHashCode(berry.getTexture()), true);
                        });
                    }
                    break;
                case ORE_PLANT:
                case DOUBLE_PLANT:
                    if (isPaper) {
                        current.setType(Material.PLAYER_HEAD);
                        Rotatable s = (Rotatable) current.getBlockData();
                        s.setRotation(FACES[random.nextInt(FACES.length)]);
                        current.setBlockData(s);
                        PlayerHead.setSkin(current, PlayerSkin.fromHashCode(berry.getTexture()), true);
                    }
                    else {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            BlockStorage.store(current.getRelative(BlockFace.UP), berry.getHeadItem());
                            current.setType(Material.OAK_LEAVES);
                            current.getRelative(BlockFace.UP).setType(Material.PLAYER_HEAD);
                            Rotatable ss = (Rotatable) current.getRelative(BlockFace.UP).getBlockData();
                            ss.setRotation(FACES[random.nextInt(FACES.length)]);
                            current.getRelative(BlockFace.UP).setBlockData(ss);
                            PlayerHead.setSkin(current.getRelative(BlockFace.UP), PlayerSkin.fromHashCode(berry.getTexture()), true);
                        });
                    }
                    break;
                default:
                    break;
                }
                break;
            }
        }
    }

    private boolean isFlat(Block current) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 6; k++) {
                    if (current.getRelative(i, k, j).getType().isSolid() || Tag.LEAVES.isTagged(current.getRelative(i, k, j).getType()) || !current.getRelative(i, -1, j).getType().isSolid()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHarvest(BlockBreakEvent e) {
        if (Slimefun.getProtectionManager().hasPermission(e.getPlayer(), e.getBlock().getLocation(), Interaction.BREAK_BLOCK)) {
            if (e.getBlock().getType().equals(Material.PLAYER_HEAD) || Tag.LEAVES.isTagged(e.getBlock().getType())) {
                dropFruitFromTree(e.getBlock());
            }

            if (e.getBlock().getType() == Material.SHORT_GRASS) {
                if (!ExoticGarden.getGrassDrops().keySet().isEmpty() && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    Random random = ThreadLocalRandom.current();

                    if (random.nextInt(100) < 6) {
                        ItemStack[] items = ExoticGarden.getGrassDrops().values().toArray(new ItemStack[0]);
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), items[random.nextInt(items.length)]);
                    }
                }
            }
            else {
                ItemStack item = ExoticGarden.harvestPlant(e.getBlock());

                if (item != null) {
                    e.setCancelled(true);
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDecay(LeavesDecayEvent e) {
        if (!Slimefun.getWorldSettingsService().isWorldEnabled(e.getBlock().getWorld())) {
            return;
        }

        String id = BlockStorage.checkID(e.getBlock());

        if (id != null) {
            for (Berry berry : ExoticGarden.getBerries()) {
                if (id.equalsIgnoreCase(berry.getID())) {
                    e.setCancelled(true);
                    return;
                }
            }
        }

        dropFruitFromTree(e.getBlock());
        ItemStack item = BlockStorage.retrieve(e.getBlock());

        if (item != null) {
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getPlayer().isSneaking()) return;

        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock == null) return;

        // Check if player is applying Bone Meal to a sapling/bush
        ItemStack inHand = e.getItem();
        if (inHand != null && inHand.getType() == Material.BONE_MEAL) {
            SlimefunItem item = BlockStorage.check(clickedBlock);
            if (item != null) {
                // Check if it's a tree sapling
                for (Tree tree : ExoticGarden.getTrees()) {
                    if (item.getId().equalsIgnoreCase(tree.getSapling())) {
                        if (item instanceof BonemealableItem && ((BonemealableItem) item).isBonemealDisabled()) {
                            e.setCancelled(true);
                            clickedBlock.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, clickedBlock.getLocation().clone().add(0.5, 0.8, 0.5), 4);
                            clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return;
                        }

                        if (!Slimefun.getProtectionManager().hasPermission(e.getPlayer(), clickedBlock.getLocation(), Interaction.BREAK_BLOCK)) {
                            return;
                        }

                        e.setCancelled(true);
                        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                            inHand.setAmount(inHand.getAmount() - 1);
                        }

                        clickedBlock.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, clickedBlock.getLocation().clone().add(0.5, 0.5, 0.5), 6, 0.2, 0.2, 0.2);
                        clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ITEM_BONE_MEAL_USE, 1, 1);

                        BlockStorage.clearBlockInfo(clickedBlock.getLocation());
                        Schematic.pasteSchematic(clickedBlock.getLocation(), tree);
                        return;
                    }
                }

                // Check if it's a Berry / Magical Plant sapling
                for (Berry berry : ExoticGarden.getBerries()) {
                    if (item.getId().equalsIgnoreCase(berry.toBush())) {
                        if (item instanceof BonemealableItem && ((BonemealableItem) item).isBonemealDisabled()) {
                            e.setCancelled(true);
                            clickedBlock.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, clickedBlock.getLocation().clone().add(0.5, 0.8, 0.5), 4);
                            clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return;
                        }

                        if (!Slimefun.getProtectionManager().hasPermission(e.getPlayer(), clickedBlock.getLocation(), Interaction.BREAK_BLOCK)) {
                            return;
                        }

                        e.setCancelled(true);
                        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                            inHand.setAmount(inHand.getAmount() - 1);
                        }

                        clickedBlock.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, clickedBlock.getLocation().clone().add(0.5, 0.5, 0.5), 6, 0.2, 0.2, 0.2);
                        clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ITEM_BONE_MEAL_USE, 1, 1);

                        growBerryPlant(clickedBlock, berry);
                        return;
                    }
                }
            }
        }

        if (Slimefun.getProtectionManager().hasPermission(e.getPlayer(), clickedBlock.getLocation(), Interaction.BREAK_BLOCK)) {
            ItemStack item = ExoticGarden.harvestPlant(clickedBlock);

            if (item != null) {
                clickedBlock.getWorld().playEffect(clickedBlock.getLocation(), Effect.STEP_SOUND, Material.OAK_LEAVES);
                clickedBlock.getWorld().dropItemNaturally(clickedBlock.getLocation(), item);
            } else {
                // The block wasn't a plant, we try harvesting a fruit instead
                ExoticGarden.getInstance().harvestFruit(clickedBlock);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e) {
        e.blockList().removeAll(getAffectedBlocks(e.blockList()));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e) {
        e.blockList().removeAll(getAffectedBlocks(e.blockList()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBonemealPlant(BlockFertilizeEvent e) {
        Block b = e.getBlock();
        if (b.getType() == Material.OAK_SAPLING) {
            SlimefunItem item = BlockStorage.check(b);

            if (item instanceof BonemealableItem && ((BonemealableItem) item).isBonemealDisabled()) {
                e.setCancelled(true);
                b.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, b.getLocation().clone().add(0.5, 0, 0.5), 4);
                b.getWorld().playSound(b.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            }
        }
    }

    private Set<Block> getAffectedBlocks(List<Block> blockList) {
        Set<Block> blocksToRemove = new HashSet<>();

        for (Block block : blockList) {
            ItemStack item = ExoticGarden.harvestPlant(block);

            if (item != null) {
                blocksToRemove.add(block);
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }

        return blocksToRemove;
    }

    private void dropFruitFromTree(Block block) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    // inspect a cube at the reference
                    Block fruit = block.getRelative(x, y, z);
                    if (fruit.isEmpty()) continue;


                    Location loc = fruit.getLocation();
                    SlimefunItem check = BlockStorage.check(loc);
                    if (check == null) continue;
                    for (Tree tree : ExoticGarden.getTrees()) {
                        if (check.getId().equalsIgnoreCase(tree.getFruitID())) {
                            BlockStorage.clearBlockInfo(loc);
                            ItemStack fruits = check.getItem();
                            fruit.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.OAK_LEAVES);
                            fruit.getWorld().dropItemNaturally(loc, fruits);
                            fruit.setType(Material.AIR);
                            break;
                        }
                    }
                }
            }
        }
    }

}
