package me.dags.daflightmanager;

import me.dags.daflightmanager.commands.RefreshCommand;
import me.dags.daflightmanager.commands.ReloadCommand;
import me.dags.daflightmanager.listener.BukkitMessageListener;
import me.dags.daflightmanager.listener.BukkitNoClipListener;
import me.dags.daflightmanager.utils.BukkitConfig;
import me.dags.daflightmanager.utils.NCPExemptionHandler;
import me.dags.daflightmanagercommon.DaFlightManager;
import me.dags.daflightmanagercommon.PluginBase;
import me.dags.daflightmanagercommon.messaging.DFClient;
import me.dags.daflightmanagercommon.messaging.DFMessageListener;
import me.dags.daflightmanagercommon.messaging.DFMessenger;
import me.dags.daflightmanagercommon.utils.DFConfig;
import me.dags.daflightmanagercommon.utils.ExemptionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightManagerBukkit extends JavaPlugin implements PluginBase<Player>
{
    private final DaFlightManager<Player> manager = new DaFlightManager<Player>(this);
    private ExemptionHandler<Player> exemptionHandler;
    private BukkitConfig bukkitConfig;

    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        bukkitConfig = new BukkitConfig(getConfig());
        manager.initManager();
    }

    public void reload()
    {
        reloadConfig();
        bukkitConfig = new BukkitConfig(getConfig());
    }

    @Override
    public void registerMessenger(DFMessenger<Player> dfMessenger)
    {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "DaFlight");
    }

    @Override
    public void registerMessageListener(DFMessageListener<Player> dfMessageListener)
    {
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "DaFlight", new BukkitMessageListener(dfMessageListener));
    }

    @Override
    public void registerNoClipListener(DaFlightManager<Player> manager)
    {
        Bukkit.getPluginManager().registerEvents(new BukkitNoClipListener(manager), this);
    }

    @Override
    public void registerCommands(DaFlightManager<Player> manager)
    {
        getCommand("dfrefresh").setExecutor(new RefreshCommand(manager));
        getCommand("dfreload").setExecutor(new ReloadCommand(manager));
    }

    @Override
    public void runTask(Runnable runnable, long l, boolean b)
    {
        if (b)
        {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, runnable, l, l);
        }
        else
        {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, runnable, l);
        }
    }

    @Override
    public void dispatchMessage(DFClient<Player> dfClient, byte[] bytes)
    {
        dfClient.get().sendPluginMessage(this, "DaFlight", bytes);
    }

    @Override
    public void log(String s)
    {
        getLogger().info(s);
    }

    @Override
    public DFConfig getDFConfig()
    {
        return bukkitConfig;
    }

    @Override
    public Set<DFClient<Player>> getOnlineClients()
    {
        Set<DFClient<Player>> clients = new HashSet<>();
        for (Player p : Bukkit.getOnlinePlayers())
        {
            clients.add(new BukkitClient(p));
        }
        return clients;
    }

    @Override
    public ExemptionHandler<Player> getExemptionHandler()
    {
        if (exemptionHandler == null)
        {
            exemptionHandler = new NCPExemptionHandler();
        }
        return exemptionHandler;
    }
}
