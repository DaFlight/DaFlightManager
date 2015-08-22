package me.dags.daflightmanager.utils;

import me.dags.daflightmanagercommon.utils.DFConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

import java.util.List;

/**
 * @author dags_ <dags@dags.me>
 */

public class BukkitConfig extends DFConfig
{
    private final Configuration config;
    private boolean exemptsExist = false;

    public BukkitConfig(Configuration config1)
    {
        config = config1;
        loadProtections();
    }

    private void loadProtections()
    {
        List<String> protections = config.getStringList("NoClipProtection");
        if (protections != null)
        {
            for (String s : protections)
            {
                super.addProtection(s);
            }
        }
    }

    @Override
    public boolean exemptionsEnabled()
    {
        return exemptsExist && config.getBoolean("HookIntoNCP", true);
    }

    @Override
    public DFConfig checkForExemptionPlugins()
    {
        exemptsExist = Bukkit.getPluginManager().getPlugin("NoCheatPlus") != null;
        return this;
    }

    @Override
    public List<Integer> getSpeeds()
    {
        List<Integer> speeds = config.getIntegerList("Permissions.speeds");
        if (speeds == null || speeds.isEmpty())
        {
            return super.getDefaultSpeeds();
        }
        return speeds;
    }
}
