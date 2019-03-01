package me.dags.daflightmanager.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.ByteBuffer;

public class DFMBukkit extends JavaPlugin implements PluginMessageListener {

    private static DFMBukkit instance;
    private static Config config = Config.defaultConfig();

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "daflight:connect", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "daflight:fly");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "daflight:sprint");
        config = loadConfig();
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        getLogger().info(String.format("DaFlight connect message received from user %s", player.getName()));
        resetSpeeds(player);
    }

    private Config loadConfig() {
        Configuration configuration = getConfig();

        if (configuration.contains("flySpeeds") && configuration.contains("sprintSpeeds")) {
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

    public static void sendFlySpeed(Player player, float speed) {
        byte[] data = ByteBuffer.allocate(4).putFloat(speed).array();
        player.sendPluginMessage(instance, "DAFLIGHT-FLY", data);
    }

    public static void sendSprintSpeed(Player player, float speed) {
        byte[] data = ByteBuffer.allocate(4).putFloat(speed).array();
        player.sendPluginMessage(instance, "DAFLIGHT-SPRINT", data);
    }

    public static void resetSpeeds(Player player) {
        float fly = config.getMaxFlySpeed(player);
        float sprint = config.getMaxSprintSpeed(player);
        sendFlySpeed(player, fly);
        sendSprintSpeed(player, sprint);
    }
}
