package io.github.portlek.nospawner;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandCompletions;
import io.github.portlek.nospawner.command.NoSpawnerCommand;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.cactoos.iterable.IterableOf;
import org.cactoos.list.Mapped;
import org.jetbrains.annotations.NotNull;

public final class NoSpawner extends JavaPlugin {

    private static NoSpawnerAPI api;

    @Override
    public void onEnable() {
        final BukkitCommandManager manager = new BukkitCommandManager(this);
        final CommandCompletions<BukkitCommandCompletionContext> commandCompletions = manager.getCommandCompletions();
        api = new NoSpawnerAPI(
            this
        );

        api.reloadPlugin(true);
        commandCompletions.registerAsyncCompletion("materials", context ->
            new Mapped<>(
                Enum::name,
                new IterableOf<>(
                    Material.values()
                )
            )
        );
        manager.registerCommand(
            new NoSpawnerCommand(api)
        );
    }

    @NotNull
    public static NoSpawnerAPI getAPI() {
        if (api == null) {
            throw new IllegalStateException("NoSpawner cannot be used before it start!");
        }

        return api;
    }

}
