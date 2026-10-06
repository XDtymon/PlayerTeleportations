package com.jakisniekot.playerTeleportations.command;

import com.jakisniekot.playerTeleportations.PlayerTeleportations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class tpadenyCommand implements CommandExecutor {

    private PlayerTeleportations plugin;

    public tpadenyCommand(PlayerTeleportations plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args
    ) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("playertp.tpa.deny")) {
            plugin.playerMessage(player, "noPermission");
            return true;
        }



        return true;
    }
}
