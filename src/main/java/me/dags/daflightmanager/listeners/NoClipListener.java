package me.dags.daflightmanager.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author dags_ <dags@dags.me>
 */

public class NoClipListener implements Listener
{
    private static final Set<UUID> noClippers = new HashSet<>();

    @EventHandler (priority = EventPriority.LOWEST)
    public void onTp(PlayerTeleportEvent e)
    {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN)
        {
            UUID id = e.getPlayer().getUniqueId();
            if (noClippers.contains(id))
            {
                e.setCancelled(true);
            }
        }
    }

    public static void addNoClipper(Player player)
    {
        noClippers.add(player.getUniqueId());
    }

    public static void removeNoClipper(Player player)
    {
        if (noClippers.contains(player.getUniqueId()))
        {
            noClippers.remove(player.getUniqueId());
        }
    }
}
