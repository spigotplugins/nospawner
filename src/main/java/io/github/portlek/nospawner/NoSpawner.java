package io.github.portlek.nospawner;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class NoSpawner extends JavaPlugin implements Listener {

    private final List<Material> types = new ArrayList<>();

    private boolean worldGuard = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final PluginCommand removeBlockPluginCommand = getCommand("removeblock");

        if (removeBlockPluginCommand == null) {
            return;
        }

        removeBlockPluginCommand.setExecutor(this);
        removeBlockPluginCommand.setTabCompleter(this);

        worldGuard = getServer().getPluginManager().getPlugin("WorldGuard") != null &&
            getServer().getPluginManager().getPlugin("WorldEdit") != null &&
            getConfig().getBoolean("world-guard-protection");

        if (worldGuard) {
            getLogger().info("WorldGuard hooked!");
        }

        getConfig().getStringList("remove-blocks-on-chunk-load").forEach(s -> {
            final Material material = Material.getMaterial(s);

            if (material == null) {
                getLogger().warning(String.format("%s is a wrong material name!", s));
            } else {
                types.add(material);
            }
        });

        if (getConfig().getBoolean("remove-on-chunk-load")) {
            getServer().getPluginManager().registerEvents(this, this);
        }
    }

    @EventHandler
    public void chunkLoad(ChunkLoadEvent event) {
        types.forEach(material -> removeBlock(material, event.getChunk()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        long time = System.nanoTime();

        final String permission = c(Optional.ofNullable(getConfig().getString("permission")).orElse(""));

        if (!sender.hasPermission("nospawner.command")) {
            sender.sendMessage(permission);
            return true;
        }

        final String deleted = c(Optional.ofNullable(getConfig().getString("blocks-deleted")).orElse(""));
        final String unknown = c(Optional.ofNullable(getConfig().getString("unknown-world-name")).orElse(""));
        final String unkownBlockName = c(Optional.ofNullable(getConfig().getString("unknown-block-name")).orElse(""));
        final String writeBlockName = c(Optional.ofNullable(getConfig().getString("block-name")).orElse(""));

        if (args.length == 0) {
            sender.sendMessage(writeBlockName);
            return true;
        }

        final Material type;

        try {
            type = Material.valueOf(args[0]);
        } catch (Exception exception) {
            sender.sendMessage(unkownBlockName);
            return true;
        }

        if (args.length == 1) {
            for (World world : getServer().getWorlds()) {
                removeBlock(type, world);
            }
            time = System.nanoTime() - time;

            sender.sendMessage(String.format(deleted, time));

            return true;
        }

        final World world = getServer().getWorld(args[1]);

        if (world == null) {
            sender.sendMessage(unknown);
            return true;
        }

        removeBlock(type, world);
        time = System.nanoTime() - time;
        sender.sendMessage(String.format(deleted, time));

        return true;
    }

    private void removeBlock(Material type, World world) {
        Arrays.stream(world.getLoadedChunks()).forEach(chunk -> removeBlock(type, chunk));
    }

    private void removeBlock(Material type, Chunk chunk) {
        getBlocksOfChunk(chunk, type)
            .forEach(block -> getServer().getScheduler().runTaskLater(this, () ->
                block.setType(Material.AIR), 2L)
            );
    }

    private String c(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private List<Block> getBlocksOfChunk(Chunk chunk, Material type) {
        final List<Block> blocks = new ArrayList<>();
        final int minX = chunk.getX() << 4;
        final int minZ = chunk.getZ() << 4;
        final int maxX = minX | 15;
        final int maxY = chunk.getWorld().getMaxHeight();
        final int maxZ = minZ | 15;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = 0; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    final Block block = chunk.getBlock(x,y,z);

                    if (block.getType() != type || thereIsRegion(block.getLocation())) {
                        continue;
                    }

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    private boolean thereIsRegion(Location location) {
        return worldGuard && !WorldGuardPlugin
            .inst()
            .getRegionManager(location.getWorld())
            .getApplicableRegions(location)
            .getRegions()
            .isEmpty();
    }

}
