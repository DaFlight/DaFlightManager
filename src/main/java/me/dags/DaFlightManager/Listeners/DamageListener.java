package me.dags.DaFlightManager.Listeners;

import me.dags.DaFlightManager.DaFlightManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author dags_ <dags@dags.me>
 */

public class DamageListener implements Listener
{

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFallDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            {
                e.setCancelled(DaFlightManager.getManager().cancelFallDamage((Player) e.getEntity()));
            }
        }
    }

}
