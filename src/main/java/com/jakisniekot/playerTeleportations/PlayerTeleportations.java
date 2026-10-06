package com.jakisniekot.playerTeleportations;

import com.jakisniekot.playerTeleportations.handler.TPAHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

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

    private String getMessage(String messageID) {
        return getConfig().getString(messageID, "ERROR");
    }

    private Component getComponentMessage(String messageID) {
        return Component.text(getConfig().getString(messageID, "ERROR"));
    }


    /**
     *
     * @param message - accepts messageID and normal messages
     * @return - Colorful & Managed message component
     */
    public Component message(String message) {
        String textFromConfig = getConfig().getString(message);
        if (textFromConfig == null) return Component.text(message);
        return Component.text(textFromConfig);
    }

    public Component message(String message, Map<String, String> placeholder) {
        String textFromConfig = getConfig().getString(message);
        if (textFromConfig == null) {
            for (Map.Entry<String, String> entry : placeholder.entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
            return Component.text(message);
        } else {
            for (Map.Entry<String, String> entry : placeholder.entrySet()) {
                textFromConfig = textFromConfig.replace(entry.getKey(), entry.getValue());
            }
            return Component.text(textFromConfig);
        }
    }

    public void playerMessage(Player player, String string) {
        player.sendMessage(message(string));
    }

    public void playerMessage(Player player, String string, Map<String, String> placeholder) {
        player.sendMessage(message(string, placeholder));
    }
}
