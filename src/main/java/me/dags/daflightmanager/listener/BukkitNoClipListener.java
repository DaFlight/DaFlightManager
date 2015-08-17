package me.dags.daflightmanager.listener;

import me.dags.daflightmanagercommon.DaFlightManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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

    private Location to = null;

    @EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onMoveEarly(PlayerMoveEvent e)
    {
        to = e.getTo();
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMoveLate(PlayerMoveEvent e)
    {
        if (!e.getTo().equals(to) && manager.isNoClipper(e.getPlayer().getUniqueId()))
        {
            e.getPlayer().teleport(e.getTo());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTp(PlayerTeleportEvent e)
    {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN && manager.isNoClipper(e.getPlayer().getUniqueId()))
        {
            e.setCancelled(true);
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
