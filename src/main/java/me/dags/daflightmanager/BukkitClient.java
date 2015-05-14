package me.dags.daflightmanager;

import me.dags.daflightmanagercommon.messaging.DFClient;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author dags_ <dags@dags.me>
 */

public class BukkitClient implements DFClient<Player>
{
    private final Player player;

    public BukkitClient(Player p)
    {
        player = p;
    }

    @Override
    public String getName()
    {
        return player.getName();
    }

    @Override
    public UUID getUUID()
    {
        return player.getUniqueId();
    }

    @Override
    public boolean isOnline()
    {
        return player.isOnline();
    }

    @Override
    public boolean hasPermission(String s)
    {
        return player.hasPermission(s);
    }

    @Override
    public Player get()
    {
        return player;
    }
}
