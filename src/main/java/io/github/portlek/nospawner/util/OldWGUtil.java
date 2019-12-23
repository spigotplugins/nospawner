package io.github.portlek.nospawner.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class OldWGUtil {

    private OldWGUtil() {
    }

    public static boolean thereIsRegion(@NotNull Location location) throws Exception {
        final Class<?> worldGuardPluginClass = Class.forName("com.sk89q.worldguard.bukkit.WorldGuardPlugin");
        final Object getRegionManager = worldGuardPluginClass
            .getMethod("getRegionManager", World.class)
            .invoke(worldGuardPluginClass.getMethod("inst").invoke(null), location.getWorld());
        final Object getApplicableRegions = getRegionManager
            .getClass()
            .getMethod("getApplicableRegions", Location.class)
            .invoke(getRegionManager, location);

        return !((Set<?>)getApplicableRegions
            .getClass()
            .getMethod("getRegions")
            .invoke(getApplicableRegions))
            .isEmpty();
    }

}
