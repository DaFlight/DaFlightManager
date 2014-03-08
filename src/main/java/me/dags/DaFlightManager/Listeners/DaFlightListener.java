package me.dags.DaFlightManager.Listeners;

import me.dags.DaFlightManager.DaFlightManager;
import me.dags.DaFlightManager.API.ADaFlightListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightListener extends ADaFlightListener
{

    public DaFlightListener(Plugin plugin, boolean hookIntoNCP)
    {
        super(plugin, hookIntoNCP);
    }

    @Override
    public void onDaFlightMessage(Player p, DFMessageType t)
    {
        if (t.equals(DFMessageType.QUERY))
        {
            DaFlightManager.getManager().daFlightMessenger.refeshPlayer(p);
        }
        else if (t.equals(DFMessageType.DFTOGGLEON))
        {
            if (p.hasPermission("DaFlight.flymod"))
            {
                super.exempt(p);
            }
        }
        else if (t.equals(DFMessageType.DFTOGGLEOFF))
        {
            if (p.hasPermission("DaFlight.flymod"))
            {
                super.unExempt(p);
            }
        }
    }

    @Override
    public void onExempt(Player p)
    {
        DaFlightManager.getManager().setDaFlyer(p, false);
    }

    @Override
    public void onUnExempt(Player p)
    {
        DaFlightManager.getManager().removeDaFlyer(p);
    }

}
