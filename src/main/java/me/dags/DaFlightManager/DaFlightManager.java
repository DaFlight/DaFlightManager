package me.dags.DaFlightManager;

import me.dags.DaFlightManager.Listeners.ChannelListener;
import me.dags.DaFlightManager.Listeners.DamageListener;
import me.dags.DaFlightManager.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightManager extends JavaPlugin
{

    private static DaFlightManager daFlightManager;
    private Manager manager;
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
        loadConfig();
        register();
        this.manager = new Manager();
        this.manager.refreshPlayers();
    }

    public void onDisable()
    {
        this.manager.clear();
    }

    public void loadConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        String msg = "Did not find NoCheatPlus";
        if (this.ncp)
        {
            this.ncp = getConfig().getBoolean("HookIntoNCP");
            if (this.ncp)
            {
                msg = "Hooking into NoCheatPlus!";
            }
            else
            {
                msg = "Not hooking into NoCheatPlus!";
            }
        }
        getLogger().info(msg);
    }

    public void findNCP()
    {
        Plugin p = Bukkit.getPluginManager().getPlugin("NoCheatPlus");
        this.ncp = p != null;

        if (this.ncp)
        {
            getLogger().info("Found NoCheatPlus!");
        }
    }

    public void register()
    {
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "DaFlight", new ChannelListener(ncp));
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "DaFlight");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        if (getConfig().getBoolean("NoFallDamage"))
        {
            Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        }
    }

    public static Manager getManager()
    {
        return inst().manager;
    }

}
