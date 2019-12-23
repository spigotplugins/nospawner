package io.github.portlek.nospawner;

import io.github.portlek.mcyaml.YamlOf;
import io.github.portlek.nospawner.file.Config;
import io.github.portlek.nospawner.file.ConfigOptions;
import io.github.portlek.nospawner.file.Language;
import io.github.portlek.nospawner.file.LanguageOptions;
import io.github.portlek.nospawner.util.*;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class NoSpawnerAPI {

    @NotNull
    private final NoSpawner noSpawner;

    @NotNull
    private final ConfigOptions configOptions;

    @NotNull
    private final LanguageOptions languageOptions;

    @NotNull
    public Config config;

    @NotNull
    public Language language;

    public NoSpawnerAPI(@NotNull NoSpawner noSpawner) {
        this.noSpawner = noSpawner;
        this.configOptions = new ConfigOptions(
            new YamlOf(noSpawner, "config")
        );
        this.config = configOptions.value();
        this.languageOptions = new LanguageOptions(
            new YamlOf(noSpawner, "languages", config.pluginLanguage),
            config
        );
        this.language = languageOptions.value();
    }

    public void reloadPlugin(boolean first) {
        if (!first) {
            config = configOptions.value();
            language = languageOptions.value();
        } else {
            checkForUpdate(noSpawner.getServer().getConsoleSender());
        }

        HandlerList.unregisterAll(noSpawner);

        new ListenerBasic<>(
            ChunkLoadEvent.class,
            event -> config.removeOnChunkLoad,
            event -> config.removeBlocksOnChunkLoad.forEach(material -> removeBlock(material, event.getChunk()))
        ).register(noSpawner);
    }

    public void removeBlock(@NotNull Material type, @NotNull Chunk chunk) {
        new BlocksOfChunk(
            chunk,
            type
        ).value().forEach(block -> block.setType(Material.AIR));
    }

    public void removeBlock(@NotNull Material type, @NotNull Location location, int radius) {
        if (location.getWorld() == null) {
            return;
        }

        final List<Chunk> chunks = new ArrayList<>();

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                final Chunk chunk = location.getWorld().getChunkAt(
                    new Location(
                        location.getWorld(),
                        x,
                        location.getBlockY(),
                        z
                    )
                );

                if (chunks.contains(chunk)) {
                    continue;
                }

                chunks.add(chunk);
            }
        }

        chunks.forEach(chunk -> removeBlock(type, chunk));
    }

    public void checkForUpdate(@NotNull CommandSender sender) {
        final UpdateChecker updater = new UpdateChecker(noSpawner, 73486);

        try {
            if (updater.checkForUpdates()) {
                sender.sendMessage(
                    language.getGeneralNewVersionFound(updater.getLatestVersion())
                );
            } else {
                sender.sendMessage(
                    language.getGeneralLatestVersion(updater.getLatestVersion())
                );
            }
        } catch (Exception exception) {
            noSpawner.getLogger().warning("Update checker failed, could not connect to the API.");
        }
    }

    public boolean thereIsRegion(@NotNull Location location) {
        if (!config.worldGuardProtection) {
            return false;
        }

        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");

            return NewWGUtil.thereIsRegion(location);
        } catch (Exception ignored) {
            try {
                return OldWGUtil.thereIsRegion(location);
            } catch (Exception ignored2) {
                throw new IllegalStateException("WorldGuard not found");
            }
        }
    }

}
