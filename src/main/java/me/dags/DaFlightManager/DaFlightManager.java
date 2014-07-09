package me.dags.DaFlightManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightManager extends JavaPlugin
{

    private boolean ncp;
    private static DaFlightManager daFlightManager;

    public DaFlightManager()
    {
        daFlightManager = this;
    }

    public static DaFlightManager inst()
    {
        if(daFlightManager == null)
        {
            daFlightManager = new DaFlightManager();
        }
        return daFlightManager;
    }

    public void onEnable()
    {
        loadConfig();
        register();
        DaFlightMessenger.getMessenger().refreshAll();
    }

    public void onDisable()
    {
    }

    public void loadConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();

        String msg;
        this.ncp = getConfig().getBoolean("HookIntoNCP");

        if (this.ncp)
        {
            msg = "Hooking into NoCheatPlus!";
        }
        else
        {
            msg = "Not hooking into NoCheatPlus!";
        }
        getLogger().info(msg);
    }

    public void register()
    {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "DaFlight");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "DaFlight", new DaFlightListener(this, ncp));
    }

}
