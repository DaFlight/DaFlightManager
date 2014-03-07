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

    public ChannelListener(boolean b)
    {
        this.ncp = b;
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
                DaFlightManager.getManager().daFlightMessenger.refeshPlayer(p);
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

    private void exempt(Player p)
    {
        if (this.ncp && !p.isOp())
        {
            NCPExemptionManager.exemptPermanently(p, CheckType.MOVING);
        }
        DaFlightManager.getManager().setDaFlyer(p, false);
    }

    private void unExempt(Player p)
    {
        if (this.ncp && !p.isOp())
        {
            NCPExemptionManager.unexempt(p, CheckType.MOVING);
        }
        if (p.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR))
        {
            DaFlightManager.getManager().setDaFlyer(p, true);
            return;
        }
        DaFlightManager.getManager().removeDaFlyer(p);
    }

    public void log(String s)
    {
        DaFlightManager.inst().getLogger().info(s);
    }

}
