package me.dags.daflightmanager.commands;

import com.google.common.base.Optional;
import me.dags.daflightmanager.DaFlightManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author dags_ <dags@dags.me>
 */

public class RefreshCommand implements CommandExecutor
{
    private final static String REFRESH_SELF = "daflight.refresh.self";
    private final static String REFRESH_OTHER = "daflight.refresh.other";
    private final static String REFRESH_ALL = "daflight.refresh.all";

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a)
    {
        if (a.length == 0)
        {
            self(cs);
        }
        else if (a.length == 1)
        {
            if (a[0].equalsIgnoreCase("all"))
            {
                all(cs);
            }
            else if (a[0].equalsIgnoreCase("?"))
            {
                cs.sendMessage("/dfrefresh");
                cs.sendMessage("/dfrefresh <all, target>");
            }
            else
            {
                other(cs, a[0]);
            }
        }
        return true;
    }

    private boolean hasPerm(CommandSender cs, String node)
    {
        if (cs.hasPermission(node))
        {
            return true;
        }
        cs.sendMessage("Ain't got perms fo dat!");
        return false;
    }

    private void self(CommandSender cs)
    {
        if (cs instanceof Player)
        {
            if (hasPerm(cs, REFRESH_SELF))
            {
                Player p = (Player) cs;
                p.sendMessage("Refreshing DaFlight perms...");
                DaFlightManager.messenger.refreshPlayer(p);
            }
        }
        else
        {
            cs.sendMessage("Command can only be run by a Player!");
        }
    }

    private void all(CommandSender cs)
    {
        if (hasPerm(cs, REFRESH_ALL))
        {
            cs.sendMessage("Refreshing DaFlight perms for all...");
            DaFlightManager.messenger.refreshAll();
        }
    }

    private void other(CommandSender cs, String name)
    {
        if (hasPerm(cs, REFRESH_OTHER))
        {
            Optional<Player> target = matchFor(name);
            if (target.isPresent())
            {
                cs.sendMessage("Refreshing DaFlight perms for user: " + target.get().getName());
                DaFlightManager.messenger.refreshPlayer(target.get());
            }
            else
            {
                cs.sendMessage("User: " + name + " not found!");
            }
        }
    }

    private Optional<Player> matchFor(String name)
    {
        String match = name.toLowerCase();
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.getName().equalsIgnoreCase(name))
                return Optional.of(p);
            if (p.getName().toLowerCase().startsWith(match))
                return Optional.of(p);
        }
        return Optional.absent();
    }
}
