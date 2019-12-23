package io.github.portlek.nospawner.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.portlek.nospawner.NoSpawnerAPI;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("nospawner|removeblock")
public final class NoSpawnerCommand extends BaseCommand {

    @NotNull
    private final NoSpawnerAPI api;

    public NoSpawnerCommand(@NotNull NoSpawnerAPI api) {
        this.api = api;
    }

    @Default
    @CommandPermission("nospawner.command.main")
    public void defaultCommand(CommandSender sender) {
        sender.sendMessage(
            api.language.helpMessage
        );
    }

    @Subcommand("help")
    @CommandPermission("nospawner.command.help")
    public void helpCommand(CommandSender sender) {
        defaultCommand(sender);
    }

    @Subcommand("version")
    @CommandPermission("nospawner.command.version")
    public void versionCommand(CommandSender sender) {
        api.checkForUpdate(sender);
    }

    @Subcommand("reload")
    @CommandPermission("kekorank.command.reload")
    public void reloadCommand(CommandSender sender) {
        final long ms = System.currentTimeMillis();

        api.reloadPlugin(false);
        sender.sendMessage(
            api.language.getGeneralReloadComplete(System.currentTimeMillis() - ms)
        );
    }

    @Subcommand("remove")
    @CommandPermission("nospawner.command.remove")
    @CommandCompletion("@materials <radius>")
    public void removeBlock(Player player, String[] args) {
        if (args.length != 2) {
            helpCommand(player);
            return;
        }

        final Material type;

        try {
            type = Material.valueOf(args[0]);
        } catch (Exception exception) {
            player.sendMessage(
                api.language.errorUnknownBlockName
            );
            return;
        }

        final int radius;

        try {
            radius = Integer.parseInt(args[1]);
        } catch (Exception exception) {
            defaultCommand(player);
            return;
        }

        long ms = System.currentTimeMillis();

        api.removeBlock(type, player.getLocation(), radius);

        player.sendMessage(
            api.language.getGeneralBlocksDeleted(System.currentTimeMillis() - ms)
        );
    }

}
