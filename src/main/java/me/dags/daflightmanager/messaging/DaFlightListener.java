package me.dags.daflightmanager.messaging;

import me.dags.daflightmanager.DaFlightManager;
import me.dags.daflightmanager.NCPHelper;
import me.dags.daflightmanager.NoClipListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightListener implements PluginMessageListener
{
    private final boolean ncp;
    private final NCPHelper ncpHelper = new NCPHelper();

    public DaFlightListener(boolean b)
    {
        ncp = b;
    }

    @Override
    public void onPluginMessageReceived(String s, Player p, byte[] b)
    {
        if (s.equalsIgnoreCase("DaFlight"))
        {
            if (b.length == 1 && b[0] == 1)
            {
                DaFlightManager.inst().getLogger().info("DaFlight query received from " + p.getName());
                DaFlightManager.messenger.refreshPlayer(p);
            }
            else if (b.length == 2)
            {
                byte setting = b[0];
                byte value = b[1];
                switch (setting)
                {
                    case DFData.NOCLIP:
                        if (value == DFData.DISABLED && p.hasPermission(DaFlightMessenger.NO_CLIP_NODE))
                        {
                            NoClipListener.removeNoClipper(p);
                            ncpHelper.noClipUnExempt(p);
                        }
                        else if (value == DFData.ENABLED && p.hasPermission(DaFlightMessenger.NO_CLIP_NODE))
                        {
                            NoClipListener.addNoClipper(p);
                            ncpHelper.noClipExempt(p);
                        }
                        break;
                    case DFData.FLY_MOD:
                        if (value == DFData.DISABLED && ncp && p.hasPermission(DaFlightMessenger.FLY_NODE))
                        {
                            ncpHelper.movementUnExempt(p);
                        }
                        else if (value == DFData.ENABLED && ncp && p.hasPermission(DaFlightMessenger.FLY_NODE))
                        {
                            ncpHelper.movementExempt(p);
                        }
                }
            }
        }
    }
}
