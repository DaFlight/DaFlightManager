package me.dags.daflightmanager.listener;

import me.dags.daflightmanagercommon.DaFlightManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

/**
 * @author dags_ <dags@dags.me>
 */

public class BukkitNoClipListener implements Listener
{
    private final DaFlightManager manager;

    public BukkitNoClipListener(DaFlightManager dfManager)
    {
        manager = dfManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTp(PlayerTeleportEvent e)
    {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN)
        {
            UUID id = e.getPlayer().getUniqueId();
            if (manager.isNoClipper(id))
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        manager.removeNoClipper(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        manager.removeNoClipper(e.getPlayer().getUniqueId());
    }
}
