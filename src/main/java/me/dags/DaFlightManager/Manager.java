package me.dags.DaFlightManager;

import me.dags.DaFlightManager.API.DaFlightMessenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dags_ <dags@dags.me>
 */

public class Manager
{

    private Map<String, Boolean> daFlyers;
    public DaFlightMessenger daFlightMessenger;

    public Manager()
    {
        this.daFlyers = new HashMap<String, Boolean>();
        this.daFlightMessenger = new DaFlightMessenger(DaFlightManager.inst());
    }

    public void setDaFlyer(Player p, boolean b)
    {
        this.daFlyers.put(p.getName(), b);
    }

    public void removeDaFlyer(Player p)
    {
        if (this.daFlyers.containsKey(p.getName()))
        {
            if (this.daFlyers.get(p.getName()))
            {
                this.daFlyers.put(p.getName(), false);
                return;
            }
            this.daFlyers.remove(p.getName());
        }
    }

    public void refreshPlayers()
    {
        Bukkit.getScheduler().runTask(DaFlightManager.inst(), new Runnable()
        {
            @Override
            public void run()
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    daFlightMessenger.refeshPlayer(p);
                }
            }
        });
    }

    public void clear()
    {
        this.daFlyers.clear();
    }

    public boolean cancelFallDamage(Player p)
    {
        if (this.daFlyers.containsKey(p.getName()))
        {
            if (this.daFlyers.get(p.getName()))
            {
                this.daFlyers.remove(p.getName());
            }
            return true;
        }
        return false;
    }
}
