package io.github.portlek.nospawner;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class NoSpawner extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Objects.requireNonNull(getCommand("removespawner")).setExecutor(this);

        if (getConfig().getBoolean("remove-on-chunk-load")) {
            getServer().getPluginManager().registerEvents(this, this);
        }
    }

    @EventHandler
    public void chunkPopulate(ChunkLoadEvent event) {
        Arrays.stream(event.getChunk().getTileEntities())
            .filter(blockState -> blockState instanceof CreatureSpawner)
            .forEach(blockState ->
                getServer().getScheduler().runTaskLater(this, () -> blockState.getBlock().breakNaturally(), 2L));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final String permission = c(Optional.ofNullable(getConfig().getString("permission")).orElse(""));

        if (!sender.hasPermission("nospawner.command")) {
            sender.sendMessage(permission);
            return true;
        }

        final String deleted = c(Optional.ofNullable(getConfig().getString("spawners-deleted")).orElse(""));
        final String unknown = c(Optional.ofNullable(getConfig().getString("unknown-world-name")).orElse(""));

        if (args.length == 0) {
            getServer().getWorlds().stream()
                .flatMap(world -> Arrays.stream(world.getLoadedChunks()))
                .flatMap(chunk -> Arrays.stream(chunk.getTileEntities()))
                .filter(blockState -> blockState instanceof CreatureSpawner)
                .forEach(blockState -> blockState.getBlock().breakNaturally());
            sender.sendMessage(deleted);

            return true;
        }

        final World world = getServer().getWorld(args[0]);

        if (world == null) {
            sender.sendMessage(unknown);
            return true;
        }

        Arrays.stream(world.getLoadedChunks())
            .flatMap(chunk -> Arrays.stream(chunk.getTileEntities()))
            .filter(blockState -> blockState instanceof CreatureSpawner)
            .forEach(blockState -> blockState.getBlock().breakNaturally());

        sender.sendMessage(deleted);

        return true;
    }

    private String c(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
