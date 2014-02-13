package me.dags.DaFlightManager.Listeners;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import me.dags.DaFlightManager.DaFlightManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author dags_ <dags@dags.me>
 */

public class ChannelListener implements PluginMessageListener
{
    private boolean ncp;
    private int[] speeds = new int[]{2, 5, 7, 10, 15, 25, 50};

    public ChannelListener(boolean b)
    {
        ncp = b;
    }

    @Override
    public void onPluginMessageReceived(String s, Player p, byte[] b)
    {
        if (s.equalsIgnoreCase("DaFlight"))
        {
            // DaFlight query
            if (b.length == 1 && b[0] == 1)
            {
                log("DaFlight query received from " + p.getName());
                refeshPlayer(p);
            }
            // Toggle sprint/flight
            else if (b.length == 2 && b[0] == 2)
            {
                if (p.hasPermission("DaFlight.flymod"))
                {
                    if (b[1] == 1)
                    {
                        exempt(p);
                    }
                    else if (b[1] == 0)
                    {
                        unExempt(p);
                    }
                }
            }
        }
    }

    public void refeshPlayer(Player p)
    {
        returnFBPerms(p);
        returnFlyPerms(p);
        returnMaxSpeed(p);
    }

    public void returnFBPerms(Player p)
    {
        byte[] b = new byte[2];
        b[0] = 1;
        b[1] = 0;

        if (p.hasPermission("DaFlight.fullbright"))
        {
            b[1] = 1;
        }
        dispatch(p, b);
    }

    public void returnFlyPerms(Player p)
    {
        byte[] b = new byte[2];
        b[0] = 2;
        b[1] = 0;

        if (p.hasPermission("DaFlight.flymod"))
        {
            b[1] = 1;
        }
        dispatch(p, b);
    }

    public void returnMaxSpeed(Player p)
    {
        if (p.hasPermission("DaFlight.flymod"))
        {
            byte[] b = new byte[2];
            b[0] = 100;
            b[1] = 50;

            int limit = 0;

            for (int i : speeds)
            {
                if (p.hasPermission("DaFlight.speed." + i))
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
                dispatch(p, b);
            }
        }
    }

    private void exempt(Player p)
    {
        if (ncp && !p.isOp())
        {
            NCPExemptionManager.exemptPermanently(p, CheckType.MOVING);
        }
        DaFlightManager.inst().setDaFlyer(p, false);
    }

    private void unExempt(Player p)
    {
        if (ncp && !p.isOp())
        {
            NCPExemptionManager.unexempt(p, CheckType.MOVING);
        }
        if (p.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR))
        {
            DaFlightManager.inst().setDaFlyer(p, true);
            return;
        }
        DaFlightManager.inst().removeDaFlyer(p);
    }

    public void dispatch(Player p, byte[] b)
    {
        p.sendPluginMessage(DaFlightManager.inst(), "DaFlight", b);
    }

    public void log(String s)
    {
        DaFlightManager.inst().getLogger().info(s);
    }

}
