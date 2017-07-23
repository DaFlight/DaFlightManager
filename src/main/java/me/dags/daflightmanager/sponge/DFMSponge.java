package me.dags.daflightmanager.sponge;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.network.*;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;

/**
 * @author dags <dags@dags.me>
 */
@Plugin(name = "DaFlightManager", id = "daflightmanager", version = "2.2.1")
public class DFMSponge implements RawDataListener {

    private static Config config = Config.defaultConfig();
    private static ChannelBinding.RawDataChannel flyChannel;
    private static ChannelBinding.RawDataChannel sprintChannel;

    private final Logger logger = LoggerFactory.getLogger("DaFlightManager");
    private final ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    public DFMSponge(@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> loader) {
        this.loader = loader;
    }

    @Listener
    public void init(GameInitializationEvent event) {
        config = loadConfig();
        Sponge.getChannelRegistrar().createRawChannel(this, "DAFLIGHT-CONNECT").addListener(this);
        flyChannel = Sponge.getChannelRegistrar().createRawChannel(this, "DAFLIGHT-FLY");
        sprintChannel = Sponge.getChannelRegistrar().createRawChannel(this, "DAFLIGHT-SPRINT");
    }

    @Listener
    public void reload(GameReloadEvent event) {
        config = loadConfig();
        Sponge.getServer().getOnlinePlayers().forEach(DFMSponge::resetSpeeds);
    }

    @Override
    public void handlePayload(ChannelBuf data, RemoteConnection connection, Platform.Type side) {
        if (connection instanceof PlayerConnection) {
            Player player = ((PlayerConnection) connection).getPlayer();
            logger.info("DaFlight connect message received from user {}", player.getName());
            resetSpeeds(player);
        }
    }

    private Config loadConfig() {
        try {
            ConfigurationNode node = loader.load();
            Config config = ObjectMapper.forClass(Config.class).bindToNew().populate(node);
            if (config.isEmpty()) {
                config = Config.defaultConfig();
                saveConfig(config);
            }
            return config;
        } catch (IOException | ObjectMappingException e) {
            Config config = Config.defaultConfig();
            saveConfig(config);
            return config;
        }
    }

    private void saveConfig(Config config) {
        try {
            ConfigurationNode node = loader.createEmptyNode();
            ObjectMapper.forObject(config).populate(node);
            loader.save(node);
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public static void resetSpeeds(Player player) {
        float fly = config.getMaxFlySpeed(player);
        float sprint = config.getMaxSprintSpeed(player);
        sendFlySpeed(player, fly);
        sendSprintSpeed(player, sprint);
    }

    public static void sendFlySpeed(Player player, float speed) {
        flyChannel.sendTo(player, b -> b.writeFloat(speed));
    }

    public static void sendSprintSpeed(Player player, float speed) {
        sprintChannel.sendTo(player, b -> b.writeFloat(speed));
    }
}
