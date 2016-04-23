package me.dags.daflightmanager.bukkit;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dags_ <dags@dags.me>
 */
class Config
{
    Map<String, Float> flySpeeds = new HashMap<>();
    Map<String, Float> sprintSpeeds = new HashMap<>();

    Float getMaxFlySpeed(Player player)
    {
        return max(flySpeeds, player, "fly");
    }

    Float getMaxSprintSpeed(Player player)
    {
        return max(sprintSpeeds, player, "sprint");
    }

    private Float max(Map<String, Float> speeds, Player player, String type)
    {
        return speeds.entrySet().stream()
                .filter(e -> player.hasPermission("daflight." + type + "." + e.getKey()))
                .map(Map.Entry::getValue)
                .max((i1, i2) -> i1 > i2 ? 1 : -1).orElse(0F);
    }

    static Config defaultConfig()
    {
        Config config = new Config();
        config.flySpeeds.put("guest", 1F);
        config.flySpeeds.put("member", 5F);
        config.flySpeeds.put("moderator", 15F);
        config.flySpeeds.put("operator", 100F);
        config.sprintSpeeds.putAll(config.flySpeeds);
        return config;
    }
}
