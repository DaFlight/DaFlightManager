package me.dags.DaFlightManager.Listeners;

import me.dags.DaFlightManager.DaFlightManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author dags_ <dags@dags.me>
 */

public class PlayerListener implements Listener
{

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        DaFlightManager.inst().removeDaFlyer(e.getPlayer());
    }

    @EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFallDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            {
                e.setCancelled(DaFlightManager.inst().cancelFallDamage((Player) e.getEntity()));
            }
        }
    }

}
