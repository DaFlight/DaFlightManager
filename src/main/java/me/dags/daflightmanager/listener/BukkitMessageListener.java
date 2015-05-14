package me.dags.daflightmanager.listener;

import me.dags.daflightmanager.BukkitClient;
import me.dags.daflightmanagercommon.messaging.DFMessageListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author dags_ <dags@dags.me>
 */

public class BukkitMessageListener implements PluginMessageListener
{
    private final DFMessageListener<Player> listener;

    public BukkitMessageListener(DFMessageListener<Player> dfListener)
    {
        listener = dfListener;
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes)
    {
        listener.handleData(new BukkitClient(player), s, bytes);
    }
}
