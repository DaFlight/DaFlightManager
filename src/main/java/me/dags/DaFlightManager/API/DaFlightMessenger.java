package me.dags.DaFlightManager.API;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author dags_ <dags@dags.me>
 */

/**
 * This class handles out-bound messages (Server to Client) on the 'DaFlight' plugin channel.
 */
public class DaFlightMessenger
{

    private int[] speeds = new int[]{2, 3, 5, 7, 10, 13, 15, 25, 50};
    private String plugin;
    private String flyNode;
    private String fbNode;
    private String speedNode;

    /**
     * Create an instance of the DaFlight Messenger
     * @param plugin Plugin - instance of the host plugin
     */
    public DaFlightMessenger(Plugin plugin)
    {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "DaFlight");

        this.plugin = plugin.getName();
        this.fbNode = "DaFlight.fullbright";
        this.flyNode = "DaFlight.flymod";
        this.speedNode = "DaFlight.speed";
    }

    /**
     * Resend DaFlight permissions based on the player's current permissions
     * @param target - Player - targeted player
     */
    public void refeshPlayer(Player target)
    {
        this.returnFBPerms(target);
        this.returnFlyPerms(target);
        this.returnMaxSpeed(target);
    }

    /**
     * Set a custom permission node to replace 'DaFLight.fullbright'
     * @param s String - new permission node
     */
    public void setFbNode(String s)
    {
        this.fbNode = s;
    }

    /**
     * Set a custom permission node to replace 'DaFLight.flymod'
     * @param s String - new permission node
     */
    public void setFlyNode(String s)
    {
        this.flyNode = s;
    }

    /**
     * Set a custom permission node to replace 'DaFLight.speed'
     * @param s String - new permission node
     */
    public void setSpeedNode(String s)
    {
        this.speedNode = s;
    }

    /**
     * Manually send a FullBright enable/disable message to the target player
     * @param target - Player - targeted player
     * @param toggle boolean - true = enabled, false = disabled
     */
    public void setPlayerFB(Player target, boolean toggle)
    {
        byte[] b = new byte[2];
        b[0] = 1;
        b[1] = 0;

        if (toggle)
        {
            b[1] = 1;
        }
        this.dispatch(target, b);
    }

    /**
     * Manually send a FlyMod enable/disable message to the target player
     * @param target - Player - targeted player
     * @param toggle boolean - true = enabled, false = disabled
     */
    public void setPlayerFlight(Player target, boolean toggle)
    {
        byte[] b = new byte[2];
        b[0] = 2;
        b[1] = 0;

        if (toggle)
        {
            b[1] = 1;
        }
        this.dispatch(target, b);
    }

    /**
     * Manually send a Fly-Speed message to the target player
     * @param target - Player - targeted player
     * @param i int - speed value (should be greater than 0)
     */
    public void setPlayerSpeed(Player target, int i)
    {
        if (i <= 0)
        {
            return;
        }
        byte[] b = new byte[2];
        b[0] = 100;
        b[1] = (byte) i;

        this.dispatch(target, b);
    }

    /**
     * Send a FullBright enable/disable message based on the player's permissions
     * @param target - Player - targeted player
     */
    public void returnFBPerms(Player target)
    {
        byte[] b = new byte[2];
        b[0] = 1;
        b[1] = 0;

        if (target.hasPermission(this.fbNode))
        {
            b[1] = 1;
        }
        this.dispatch(target, b);
    }

    /**
     * Send a FlyMod enable/disable message based on the player's permissions
     * @param target - Player - targeted player
     */
    public void returnFlyPerms(Player target)
    {
        byte[] b = new byte[2];
        b[0] = 2;
        b[1] = 0;

        if (target.hasPermission(this.flyNode))
        {
            b[1] = 1;
        }
        this.dispatch(target, b);
    }

    /**
     * Send a Fly-Speed message based on the player's permissions
     * @param target - Player - targeted player
     */
    public void returnMaxSpeed(Player target)
    {
        if (target.hasPermission(this.flyNode))
        {
            byte[] b = new byte[2];
            b[0] = 100;
            b[1] = 50;

            int limit = 0;

            for (int i : this.speeds)
            {
                if (target.hasPermission(this.speedNode + "." + i))
                {
                    if (i > limit)
                    {
                        limit = i;
                    }
                }
            }

            if (limit > 0)
            {
                b[1] = (byte) limit;
                this.dispatch(target, b);
            }
        }
    }

    private Plugin getPlugin()
    {
        return Bukkit.getPluginManager().getPlugin(this.plugin);
    }

    private void dispatch(Player p, byte[] b)
    {
        p.sendPluginMessage(this.getPlugin(), "DaFlight", b);
    }

}
