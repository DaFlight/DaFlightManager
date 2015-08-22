package me.dags.daflightmanager.commands;

import me.dags.daflightmanagercommon.DaFlightManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author dags_ <dags@dags.me>
 */

public class ReloadCommand implements CommandExecutor
{
    private final static String DF_RELOAD = "daflight.reload";
    private final DaFlightManager manager;

    public ReloadCommand(DaFlightManager daFlightManager)
    {
        manager = daFlightManager;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String c, String[] a)
    {
        if (cs.hasPermission(ReloadCommand.DF_RELOAD))
        {
            manager.getPluginBase().reload();
            cs.sendMessage("[DaFlight] Reloading config...");
            return true;
        }
        return false;
    }
}
