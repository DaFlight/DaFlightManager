package me.dags.DaFlightManager.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightMessenger
{

    private int[] speeds = new int[]{2, 3, 5, 7, 10, 13, 15, 25, 50};
    private String plugin;
    private String flyNode;
    private String fbNode;
    private String speedNode;

    public DaFlightMessenger(Plugin p)
    {
        this.plugin = p.getName();
        this.fbNode = "DaFlight.fullbright";
        this.flyNode = "DaFlight.flymod";
        this.speedNode = "DaFlight.speed";
    }

    public void refeshPlayer(Player p)
    {
        this.returnFBPerms(p);
        this.returnFlyPerms(p);
        this.returnMaxSpeed(p);
    }

    public void setFbNode(String s)
    {
        this.fbNode = s;
    }

    public void setFlyNode(String s)
    {
        this.flyNode = s;
    }

    public void setSpeedNode(String s)
    {
        this.speedNode = s;
    }

    public void setPlayerFB(Player p, boolean toggle)
    {
        byte[] b = new byte[2];
        b[0] = 1;
        b[1] = 0;

        if (toggle)
        {
            b[1] = 1;
        }
        this.dispatch(p, b);
    }

    public void setPlayerFlight(Player p, boolean toggle)
    {
        byte[] b = new byte[2];
        b[0] = 2;
        b[1] = 0;

        if (toggle)
        {
            b[1] = 1;
        }
        this.dispatch(p, b);
    }

    public void setPlayerSpeed(Player p, int i)
    {
        if (i <= 0)
        {
            return;
        }
        byte[] b = new byte[2];
        b[0] = 100;
        b[1] = (byte) i;

        this.dispatch(p, b);
    }

    public void returnFBPerms(Player p)
    {
        byte[] b = new byte[2];
        b[0] = 1;
        b[1] = 0;

        if (p.hasPermission(this.fbNode))
        {
            b[1] = 1;
        }
        this.dispatch(p, b);
    }

    public void returnFlyPerms(Player p)
    {
        byte[] b = new byte[2];
        b[0] = 2;
        b[1] = 0;

        if (p.hasPermission(this.flyNode))
        {
            b[1] = 1;
        }
        this.dispatch(p, b);
    }

    public void returnMaxSpeed(Player p)
    {
        if (p.hasPermission(this.flyNode))
        {
            byte[] b = new byte[2];
            b[0] = 100;
            b[1] = 50;

            int limit = 0;

            for (int i : this.speeds)
            {
                if (p.hasPermission(this.speedNode + "." + i))
                {
                    if (i > limit)
                    {
                        limit = i;
                    }
                }
            }

            if (limit > 0)
            {
                b[1] = (byte) limit;
                this.dispatch(p, b);
            }
        }
    }

    private Plugin getPlugin()
    {
        return Bukkit.getPluginManager().getPlugin(this.plugin);
    }

    private void dispatch(Player p, byte[] b)
    {
        p.sendPluginMessage(this.getPlugin(), "DaFlight", b);
    }

}
