package com.jakisniekot.playerTeleportations.command;

import com.jakisniekot.playerTeleportations.PlayerTeleportations;
import com.jakisniekot.playerTeleportations.handler.TPARequestError;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class tpaCommand implements CommandExecutor {

    private PlayerTeleportations plugin;

    public tpaCommand(PlayerTeleportations plugin) {
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

        if (!player.hasPermission("playertp.tpa.send")) {
            plugin.playerMessage(player, "noPermission");
            return true;
        }

        if (args.length != 1) {
            //USAGE ERROR
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            //NO TARGET
            return true;
        }

        TPARequestError error = plugin.tpaHandler().sendRequest(player, target);
        String messageID = null;
        switch (error) {
            case NONE -> messageID = "tpa.sender";
        }


        return true;
    }
}
