package io.github.portlek.nospawner.file;

import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.itemstack.util.ListToString;
import io.github.portlek.mcyaml.IYaml;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.jetbrains.annotations.NotNull;

public final class LanguageOptions implements Scalar<Language> {

    @NotNull
    private final IYaml yaml;

    @NotNull
    private final Config config;

    public LanguageOptions(@NotNull IYaml yaml, @NotNull Config config) {
        this.yaml = yaml;
        this.config = config;
    }

    @Override
    public Language value() {
        yaml.create();

        final String errorPermission = prefix(
            yaml.getOrSet("error.permission", "%prefix% &cYou don''t have permissions to do that!")
        );
        final String errorUnknownWorldName = prefix(yaml.getOrSet("error.unknown-world-name", "%prefix% &cThere is not world such like that!"));
        final String errorUnknownBlockName = prefix(yaml.getOrSet("error.unknown-block-name", "%prefix% &cThere is not block such like that!"));
        final String generalReloadComplete = prefix(yaml.getOrSet("general.reload-complete", "&aReload complete!"));
        final String generalNewVersionFound = prefix(yaml.getOrSet("general.new-version-found", "%prefix% &r>> &eNew version found (v%version%)"));
        final String generalLatestVersion = prefix(yaml.getOrSet("general.latest-version", "%prefix% &r>> &aYou''re using the latest version (v%version%)"));
        final String generalBlocksDeleted = prefix(
            yaml.getOrSet("general.blocks-deleted", "%prefix% &aAll blocks removed! &7Took (%ms% ms)")
        );
        final String helpMessage = prefix(
            new ListToString(
            yaml.getOrSet(
                "help-message",
                new ListOf<>(
                    "====== %prefix% Player Commands ======",
                    "&7/rank &r> &eShows help message.",
                    "&7/rank help &r> &eShows help message.",
                    "&7/rank reload &r> &eReload the plugin.",
                    "&7/rank version &r> &eChecks for update.",
                    "&7/rank remove <material> <radius> &r> &eRemove blocks."
                )
            )
        ).toString()
        );

        return new Language(
            errorPermission,
            errorUnknownWorldName,
            errorUnknownBlockName,
            generalReloadComplete,
            generalNewVersionFound,
            generalLatestVersion,
            generalBlocksDeleted,
            helpMessage
        );
    }

    @NotNull
    private String prefix(@NotNull String text) {
        return new Colored(
            text.replace("%prefix%", config.pluginPrefix)
        ).value();
    }

}
