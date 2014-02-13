package me.dags.DaFlightManager;

import me.dags.DaFlightManager.Listeners.ChannelListener;
import me.dags.DaFlightManager.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightManager extends JavaPlugin
{

    private static DaFlightManager daFlightManager;
    private boolean ncp;
    private Map<String, Boolean> daFlyers;

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
        daFlyers = new HashMap<String, Boolean>();
    }

    public void onDisable()
    {
        daFlyers.clear();
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
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "DaFlight", new ChannelListener(ncp));
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "DaFlight");
    }

    public boolean cancelFallDamage(Player p)
    {
        if (daFlyers.containsKey(p.getName()))
        {
            if (daFlyers.get(p.getName()))
            {
                daFlyers.remove(p.getName());
            }
            return true;
        }
        return false;
    }

    public void setDaFlyer(Player p, boolean b)
    {
        daFlyers.put(p.getName(), b);
    }

    public void removeDaFlyer(Player p)
    {
        if (daFlyers.containsKey(p.getName()))
        {
            if (daFlyers.get(p.getName()))
            {
                daFlyers.put(p.getName(), false);
                return;
            }
            daFlyers.remove(p.getName());
        }
    }

}
