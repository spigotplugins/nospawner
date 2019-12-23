package io.github.portlek.nospawner.util;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class NewWGUtil {

    private NewWGUtil() {
    }

    public static boolean thereIsRegion(@NotNull Location location) {
        return !WorldGuard
            .getInstance()
            .getPlatform()
            .getRegionContainer()
            .createQuery()
            .getApplicableRegions(
                new com.sk89q.worldedit.util.Location(
                    new BukkitWorld(location.getWorld()),
                    location.getX(),
                    location.getY(),
                    location.getZ()
                )
            )
            .getRegions()
            .isEmpty();
    }

}
