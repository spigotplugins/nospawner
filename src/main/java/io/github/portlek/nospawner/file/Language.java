package io.github.portlek.nospawner.file;

import org.jetbrains.annotations.NotNull;

public final class Language {

    @NotNull
    public final String errorPermission;

    @NotNull
    public final String errorUnknownWorldName;

    @NotNull
    public final String errorUnknownBlockName;

    @NotNull
    public final String generalReloadComplete;

    @NotNull
    public final String generalNewVersionFound;

    @NotNull
    public final String generalLatestVersion;

    @NotNull
    private final String generalBlocksDeleted;

    @NotNull
    public final String helpMessage;

    public Language(@NotNull String errorPermission, @NotNull String errorUnknownWorldName,
                    @NotNull String errorUnknownBlockName, @NotNull String generalReloadComplete,
                    @NotNull String generalNewVersionFound, @NotNull String generalLatestVersion,
                    @NotNull String generalBlocksDeleted, @NotNull String helpMessage) {
        this.errorPermission = errorPermission;
        this.errorUnknownWorldName = errorUnknownWorldName;
        this.errorUnknownBlockName = errorUnknownBlockName;
        this.generalReloadComplete = generalReloadComplete;
        this.generalNewVersionFound = generalNewVersionFound;
        this.generalLatestVersion = generalLatestVersion;
        this.generalBlocksDeleted = generalBlocksDeleted;
        this.helpMessage = helpMessage;
    }

    @NotNull
    public String getGeneralBlocksDeleted(long ms) {
        return ms(generalBlocksDeleted, ms);
    }

    @NotNull
    public String getGeneralReloadComplete(long ms) {
        return ms(generalReloadComplete, ms);
    }

    @NotNull
    public String getGeneralNewVersionFound(@NotNull String version) {
        return version(generalNewVersionFound, version);
    }

    @NotNull
    public String getGeneralLatestVersion(@NotNull String version) {
        return version(generalLatestVersion, version);
    }

    @NotNull
    public String ms(@NotNull String text, long ms) {
        return text.replace("%ms%", String.valueOf(ms));
    }

    @NotNull
    public String version(@NotNull String text, @NotNull String version) {
        return text.replace("%version%", version);
    }

}