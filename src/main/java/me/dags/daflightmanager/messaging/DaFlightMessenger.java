package me.dags.daflightmanager.messaging;

import me.dags.daflightmanager.DaFlightManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author dags_ <dags@dags.me>
 */

/**
 * This class handles out-bound messages (Server to Client) on the 'DaFlight' plugin channel.
 */
public class DaFlightMessenger
{

    private static DaFlightMessenger instance;
    private int[] speeds;
    private String flyNode;
    private String softFallNode;
    private String fbNode;
    private String speedNode;

    public static DaFlightMessenger getMessenger()
    {
        if (instance == null)
        {
            return new DaFlightMessenger();
        }
        return instance;
    }

    /**
     * Create an instance of the DaFlight Messenger
     */
    private DaFlightMessenger()
    {
        instance = this;
        this.speeds = new int[]{2, 3, 5, 7, 10, 13, 15, 25, 50};
        this.fbNode = "DaFlight.fullbright";
        this.flyNode = "DaFlight.flymod";
        this.softFallNode = "DaFlight.softfall";
        this.speedNode = "DaFlight.speed";
    }

    public void refreshAll()
    {
        Bukkit.getScheduler().runTaskLater(DaFlightManager.inst(), new Runnable()
        {
            @Override
            public void run()
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    refreshPlayer(p);
                }
            }
        }, 10L);
    }

    /**
     * Resend DaFlight permissions based on the player's current permissions
     * @param target - Player - targeted player
     */
    public void refreshPlayer(final Player target)
    {
        Bukkit.getScheduler().runTaskLater(DaFlightManager.inst(), new Runnable()
        {
            @Override
            public void run()
            {
                returnFBPerms(target);
                returnFlyPerms(target);
                returnSoftFallPerms(target);
                returnMaxSpeed(target);
                sendUpdateRequest(target);
            }
        }, 10L);
    }

    public void sendUpdateRequest(Player target)
    {
        this.dispatch(target, new byte[]{4, 0});
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
        byte[] b = new byte[]{1, 0};

        if (target.hasPermission(this.fbNode) || target.isOp())
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
        byte[] b = new byte[]{2, 0};

        if (target.hasPermission(this.flyNode) || target.isOp())
        {
            b[1] = 1;
        }
        this.dispatch(target, b);
    }

    /**
     * Send a FlyMod enable/disable message based on the player's permissions
     * @param target - Player - targeted player
     */
    public void returnSoftFallPerms(Player target)
    {
        byte[] b = new byte[]{3, 0};

        if (target.hasPermission(this.softFallNode) || target.isOp())
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
            byte[] b = new byte[]{100, 50};

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

    private void dispatch(Player p, byte[] b)
    {
        p.sendPluginMessage(DaFlightManager.inst(), "DaFlight", b);
    }

}
