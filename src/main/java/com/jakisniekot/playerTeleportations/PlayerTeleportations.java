package com.jakisniekot.playerTeleportations;

import com.jakisniekot.playerTeleportations.handler.TPAHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTeleportations extends JavaPlugin {

    private PlayerTeleportations plugin;

    private TPAHandler tpaHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        //Config
        saveDefaultConfig();

        //Instances
        tpaHandler = new TPAHandler(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public TPAHandler tpaHandler() {
        return tpaHandler;
    }
}
