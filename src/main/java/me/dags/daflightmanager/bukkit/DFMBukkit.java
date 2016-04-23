package me.dags.daflightmanager.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.ByteBuffer;

public class DFMBukkit extends JavaPlugin implements PluginMessageListener
{
    private Config config = Config.defaultConfig();

    @Override
    public void onEnable()
    {
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "DAFLIGHT-CONNECT", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "DAFLIGHT-FLY");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "DAFLIGHT-SPRINT");
        config = loadConfig();
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes)
    {
        getLogger().info(String.format("DaFlight connect message received from user %s", player.getName()));

        Float fly = config.getMaxFlySpeed(player);
        Float sprint = config.getMaxSprintSpeed(player);

        byte[] flyData = ByteBuffer.allocate(4).putFloat(fly).array();
        byte[] sprintData = ByteBuffer.allocate(4).putFloat(sprint).array();

        player.sendPluginMessage(this, "DAFLIGHT-FLY", flyData);
        player.sendPluginMessage(this, "DAFLIGHT-SPRINT", sprintData);
    }

    private Config loadConfig()
    {
        Configuration configuration = getConfig();
        if (configuration.contains("flySpeeds") && configuration.contains("sprintSpeeds"))
        {
            Config config = new Config();
            configuration.getConfigurationSection("flySpeeds").getValues(false).entrySet().forEach(e -> {
                config.flySpeeds.put(e.getKey(), ((Double) e.getValue()).floatValue());
            });
            configuration.getConfigurationSection("sprintSpeeds").getValues(false).entrySet().forEach(e -> {
                config.flySpeeds.put(e.getKey(), ((Double) e.getValue()).floatValue());
            });
            return config;
        }
        configuration.set("flySpeeds", config.flySpeeds);
        configuration.set("sprintSpeeds", config.sprintSpeeds);
        saveConfig();
        return config;
    }
}
