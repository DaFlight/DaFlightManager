package me.dags.DaFlightManager.Listeners;

import me.dags.DaFlightManager.DaFlightManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author dags_ <dags@dags.me>
 */

public class PlayerListener implements Listener
{

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        DaFlightManager.getManager().removeDaFlyer(e.getPlayer());
    }

}
