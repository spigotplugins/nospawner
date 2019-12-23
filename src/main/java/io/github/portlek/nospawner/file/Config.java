package io.github.portlek.nospawner.file;

import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Config {

    @NotNull
    public final String pluginPrefix;

    @NotNull
    public final String pluginLanguage;

    public final boolean checkForUpdate;

    public final boolean worldGuardProtection;

    public final boolean removeOnChunkLoad;

    @NotNull
    public final List<World> removeOnChunkLoadWorlds;

    @NotNull
    public final List<Material> removeBlocksOnChunkLoad;

    public Config(@NotNull String pluginPrefix, @NotNull String pluginLanguage, boolean checkForUpdate,
                  boolean worldGuardProtection, boolean removeOnChunkLoad, @NotNull List<World> removeOnChunkLoadWorlds,
                  @NotNull List<Material> removeBlocksOnChunkLoad) {
        this.pluginPrefix = pluginPrefix;
        this.pluginLanguage = pluginLanguage;
        this.checkForUpdate = checkForUpdate;
        this.worldGuardProtection = worldGuardProtection;
        this.removeOnChunkLoad = removeOnChunkLoad;
        this.removeOnChunkLoadWorlds = removeOnChunkLoadWorlds;
        this.removeBlocksOnChunkLoad = removeBlocksOnChunkLoad;
    }
}
