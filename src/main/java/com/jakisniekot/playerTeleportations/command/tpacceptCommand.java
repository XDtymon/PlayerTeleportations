package com.jakisniekot.playerTeleportations.command;

import com.jakisniekot.playerTeleportations.PlayerTeleportations;
import com.jakisniekot.playerTeleportations.handler.TPARequestError;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class tpacceptCommand implements CommandExecutor {

    private PlayerTeleportations plugin;

    public tpacceptCommand(PlayerTeleportations plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length == 0) {
            TPARequestError error = plugin.tpaHandler().acceptRequest(player);
            String messageID = null;
            switch (error) {
                case NONE -> messageID = "";
            }
        } else if (args.length == 1) {

        } else {

        }

        return true;
    }
}
