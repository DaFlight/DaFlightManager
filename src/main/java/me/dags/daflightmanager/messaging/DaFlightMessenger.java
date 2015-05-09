package me.dags.daflightmanager.messaging;

import me.dags.daflightmanager.DaFlightManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author dags_ <dags@dags.me>
 */

/**
 * This class handles out-bound messages (Server to Client) on the 'DaFlight' plugin channel.
 */
public class DaFlightMessenger
{
    public static final String FLY_NODE = "DaFlight.flymod";
    public static final String SOFT_FALL_NODE = "DaFlight.softfall";
    public static final String FB_NODE = "DaFlight.fullbright";
    public static final String SPEED_NODE = "DaFlight.speed";
    public static final String NO_CLIP_NODE = "DaFlight.noclip";
    
    private final List<Integer> speeds;
    
    public DaFlightMessenger(List<Integer> configSpeeds)
    {
        speeds = configSpeeds;
    }

    public void refreshAll()
    {
        Bukkit.getScheduler().runTaskLater(DaFlightManager.inst(), new Runnable()
        {
            @Override
            public void run()
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    refreshPlayer(p);
                }
            }
        }, 10L);
    }
    
    public void refreshPlayer(final Player target)
    {
        Bukkit.getScheduler().runTaskLater(DaFlightManager.inst(), new Runnable()
        {
            @Override
            public void run()
            {
                returnNoClipPerms(target);
                returnFBPerms(target);
                returnFlyPerms(target);
                returnSoftFallPerms(target);
                returnMaxSpeed(target);
                sendUpdateRequest(target);
            }
        }, 10L);
    }

    public void sendUpdateRequest(Player target)
    {
        dispatch(target, DFData.getBooleanData(DFData.REFRESH, false));
    }

    public void returnNoClipPerms(Player target)
    {
        byte[] data = DFData.getBooleanData(DFData.NOCLIP, target.hasPermission(NO_CLIP_NODE) || target.isOp());
        dispatch(target, data);
    }

    public void returnFBPerms(Player target)
    {
        byte[] data = DFData.getBooleanData(DFData.FULL_BRIGHT, target.hasPermission(FB_NODE) || target.isOp());
        dispatch(target, data);
    }

    public void returnFlyPerms(Player target)
    {
        byte[] data = DFData.getBooleanData(DFData.FLY_MOD, target.hasPermission(FLY_NODE) || target.isOp());
        dispatch(target, data);
    }

    public void returnSoftFallPerms(Player target)
    {
        byte[] data = DFData.getBooleanData(DFData.NO_FALL_DAMAGE, target.hasPermission(SOFT_FALL_NODE) || target.isOp());
        dispatch(target, data);
    }

    public void returnMaxSpeed(Player target)
    {
        if (target.hasPermission(FLY_NODE))
        {
            int limit = 0;
            for (int i : speeds)
            {
                if (target.hasPermission(SPEED_NODE + "." + i))
                {
                    if (i > limit)
                    {
                        limit = i;
                    }
                }
            }
            if (limit > 0)
            {
                byte[] data = DFData.getValueData(DFData.SPEED, (byte) limit);
                dispatch(target, data);
            }
        }
    }

    private void dispatch(Player p, byte[] b)
    {
        p.sendPluginMessage(DaFlightManager.inst(), "DaFlight", b);
    }
}
