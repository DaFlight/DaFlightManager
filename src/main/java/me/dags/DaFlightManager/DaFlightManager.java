package me.dags.DaFlightManager;

import me.dags.DaFlightManager.Listeners.DaFlightListener;
import me.dags.DaFlightManager.Listeners.DamageListener;
import me.dags.DaFlightManager.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightManager extends JavaPlugin
{

    private Manager manager;
    private boolean ncp;

    public static DaFlightManager inst()
    {
        return (DaFlightManager) Bukkit.getPluginManager().getPlugin("DaFlightManager");
    }

    public void onEnable()
    {
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

    public void register()
    {
        new DaFlightListener(this, ncp);
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
