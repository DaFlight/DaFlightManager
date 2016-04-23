package me.dags.daflightmanager.sponge;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.network.*;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author dags <dags@dags.me>
 */
@Plugin(name = "DaFlightManager", id = "me.dags.daflightmanager", version = "2.0")
public class DFMSponge implements RawDataListener
{
    private final Logger logger = LoggerFactory.getLogger("DaFlightManager");

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    private Config config;
    private ChannelBinding.RawDataChannel flyChannel;
    private ChannelBinding.RawDataChannel sprintChannel;

    @Listener
    public void init(GamePreInitializationEvent event)
    {
        config = loadConfig();

        Sponge.getChannelRegistrar().createRawChannel(this, "DAFLIGHT-CONNECT").addListener(this);
        flyChannel = Sponge.getChannelRegistrar().createRawChannel(this, "DAFLIGHT-FLY");
        sprintChannel = Sponge.getChannelRegistrar().createRawChannel(this, "DAFLIGHT-SPRINT");
    }

    @Override
    public void handlePayload(ChannelBuf data, RemoteConnection connection, Platform.Type side)
    {
        if (connection instanceof PlayerConnection)
        {
            Player player = ((PlayerConnection) connection).getPlayer();
            logger.info("DaFlight connect message received from user {}", player.getName());

            Float fly = config.getMaxFlySpeed(player);
            Float sprint = config.getMaxSprintSpeed(player);

            flyChannel.sendTo(player, b -> b.writeFloat(fly));
            sprintChannel.sendTo(player, b -> b.writeFloat(sprint));
        }
    }

    private Config loadConfig()
    {
        Path configPath = configDir.resolve("config.conf");
        if (Files.exists(configPath))
        {
            try
            {
                HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(configPath).build();
                ConfigurationNode node = loader.load();
                return ObjectMapper.forClass(Config.class).bindToNew().populate(node);
            }
            catch (IOException | ObjectMappingException e)
            {
                e.printStackTrace();
            }
        }

        Config config = Config.defaultConfig();
        saveConfig(config);
        return config;
    }

    private void saveConfig(Config config)
    {
        Path configPath = configDir.resolve("config.conf");
        try
        {
            if (!Files.exists(configPath.getParent()))
            {
                Files.createDirectories(configPath.getParent());
            }
            if (!Files.exists(configPath))
            {
                Files.createFile(configPath);
            }
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(configPath).build();
            ConfigurationNode node = loader.createEmptyNode();
            ObjectMapper.forObject(config).populate(node);
            loader.save(node);
        }
        catch (IOException | ObjectMappingException e)
        {
            e.printStackTrace();
        }
    }
}
