package me.dags.DaFlightManager;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightListener implements PluginMessageListener
{

    private boolean ncp;

    public DaFlightListener(Plugin p, boolean b)
    {
        ncp = b;
        ncpCheck(p);
    }

    @Override
    public void onPluginMessageReceived(String s, Player p, byte[] b)
    {
        if (s.equalsIgnoreCase("DaFlight"))
        {
            if (b.length == 1 && b[0] == 1)
            {
                log("DaFlight query received from " + p.getName());
                DaFlightMessenger.getMessenger().refeshPlayer(p);
            }
            else if (b.length == 2 && b[0] == 2)
            {
                switch (b[1])
                {
                    case 1:
                        if (p.hasPermission("DaFlight.flymod"))
                        {
                            exempt(p);
                        }
                        break;
                    case 2:
                        if (p.hasPermission("DaFlight.flymod"))
                        {
                            unExempt(p);
                        }
                        break;
                }
            }
        }
    }

    public void exempt(Player p)
    {
        if (this.ncp && !p.isOp())
        {
            NCPExemptionManager.exemptPermanently(p, CheckType.MOVING);
        }
    }

    public void unExempt(Player p)
    {
        if (this.ncp && !p.isOp())
        {
            NCPExemptionManager.unexempt(p, CheckType.MOVING);
        }
    }

    private void ncpCheck(final Plugin p)
    {
        if (this.ncp)
        {
            Bukkit.getScheduler().runTask(p, new Runnable()
            {
                @Override
                public void run()
                {
                    if (Bukkit.getPluginManager().getPlugin("NoCheatPlus") != null)
                    {
                        ncp = true;
                        log("Found NoCheatPlus!");
                    }
                    else
                    {
                        ncp = false;
                        log("Did not find NoCheatPlus!");
                    }
                }
            });
        }
    }

    private void log(String s)
    {
        DaFlightManager.inst().getLogger().info(s);
    }

}
