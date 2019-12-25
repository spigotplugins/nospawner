package io.github.portlek.nospawner.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.itemstack.util.XMaterial;
import io.github.portlek.mcyaml.IYaml;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ConfigOptions implements Scalar<Config> {

    @NotNull
    private final IYaml yaml;

    public ConfigOptions(@NotNull IYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public Config value() {
        yaml.create();

        final String pluginPrefix = new Colored(
            yaml.getOrSet("plugin-prefix", "&6[&eNoSpawner&6]")
        ).value();
        final String pluginLanguage = yaml.getOrSet("plugin-language", "en");
        final boolean checkForUpdate = yaml.getOrSet("check-for-update", true);
        final boolean worldGuardProtection = yaml.getOrSet("world-guard-protection", true);
        final boolean removeOnChunkLoad = yaml.getOrSet("remove-on-chunk-load", true);
        final List<World> removeOnChunkLoadWorlds = new ArrayList<>();

        for (String worldName : yaml.getOrSet("remove-on-chunk-load-worlds", new ArrayList<String>())) {
            final World world = Bukkit.getWorld(worldName);

            if (world != null) {
                removeOnChunkLoadWorlds.add(world);
            }
        }

        final List<Material> removeBlocksOnChunkLoad = new ArrayList<>();

        yaml.getStringList("remove-blocks-on-chunk-load").forEach(s ->
            XMaterial.matchXMaterial(s).flatMap(xMaterial ->
                Optional.ofNullable(xMaterial.parseMaterial())
            ).ifPresent(removeBlocksOnChunkLoad::add)
        );

        return new Config(
            pluginPrefix,
            pluginLanguage,
            checkForUpdate,
            worldGuardProtection,
            removeOnChunkLoad,
            removeOnChunkLoadWorlds,
            removeBlocksOnChunkLoad
        );
    }
}
