package me.dags.DaFlightManager;

import me.dags.DaFlightManager.Listeners.ChannelListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightManager extends JavaPlugin
{

    private static DaFlightManager daFlightManager;
    private ChannelListener channelListener;
    private boolean ncp;

    public DaFlightManager()
    {
        super();
        daFlightManager = this;
    }

    public static DaFlightManager inst()
    {
        return daFlightManager;
    }

    public void onEnable()
    {
        findNCP();
        registerChannels();
    }

    public void onDisable()
    {

    }

    public void findNCP()
    {
        Plugin p = Bukkit.getPluginManager().getPlugin("NoCheatPlus");
        ncp = p != null;

        if (ncp)
        {
            getLogger().info("Found NoCheatPlus!");
        }
    }

    public void registerChannels()
    {
        channelListener = new ChannelListener(ncp);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "DaFlight", channelListener);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "DaFlight");
    }

}
