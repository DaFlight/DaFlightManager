package me.dags.daflightmanager;

import me.dags.daflightmanager.commands.RefreshCommand;
import me.dags.daflightmanager.listeners.NoClipListener;
import me.dags.daflightmanager.messaging.DaFlightListener;
import me.dags.daflightmanager.messaging.DaFlightMessenger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightManager extends JavaPlugin
{
    private static DaFlightManager daFlightManager;
    public static DaFlightMessenger messenger;

    private List<Integer> speeds = new ArrayList<>();
    private boolean ncp;

    public DaFlightManager()
    {
        daFlightManager = this;
    }

    public static DaFlightManager inst()
    {
        return daFlightManager;
    }

    public void onEnable()
    {
        loadConfig();
        messenger = new DaFlightMessenger(speeds);
        register();
        messenger.refreshAll();
    }

    public void loadConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        ncp = getConfig().getBoolean("HookIntoNCP", true);
        speeds = getConfig().getIntegerList("Permissions.speeds");
        if (speeds == null || speeds.isEmpty())
        {
            speeds = defaultSpeeds();
            getConfig().set("Permissions.speeds", speeds);
            saveConfig();
        }
        String msg = ncp ? "Hooking into NoCheatPlus!" : "Not hooking into NoCheatPlus!";
        getLogger().info(msg);
    }

    public void register()
    {
        Bukkit.getScheduler().runTask(this, new Runnable()
        {
            @Override
            public void run()
            {
                if (ncp)
                {
                    ncp = Bukkit.getPluginManager().getPlugin("NoCheatPlus") != null;
                    String msg = ncp ? "Found NoCheatPlus!" : "Did not find NoCheatPlus!";
                    getLogger().info(msg);
                }
                getCommand("dfrefresh").setExecutor(new RefreshCommand());
                Bukkit.getPluginManager().registerEvents(new NoClipListener(), inst());
                Bukkit.getMessenger().registerOutgoingPluginChannel(inst(), "DaFlight");
                Bukkit.getMessenger().registerIncomingPluginChannel(inst(), "DaFlight", new DaFlightListener(ncp));
            }
        });
    }

    private List<Integer> defaultSpeeds()
    {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(10);
        list.add(15);
        list.add(20);
        list.add(30);
        list.add(50);
        return list;
    }
}
