package me.dags.DaFlightManager.API;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import me.dags.DaFlightManager.DaFlightManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author dags_ <dags@dags.me>
 */

/**
 * This class should be extended in-order to provide your plugin a MessageListener that 'listens' specifically for DaFlight messages.
 * You can provide your own code for events by overriding the abstract methods of this class.
 * A 'message type' will be provided to allow easy differentiation of the incoming messages.
 */
public abstract class ADaFlightListener implements PluginMessageListener
{

    private boolean ncp;

    /**
     * Initialises and registers the DaFlight Listener. This listens for messages sent from DaFlight clients.
     * @param plugin Plugin - the host plugin registering the Listener
     * @param hookIntoNCP boolean - set whether the Listener should hook into NCP to exempt players from movement checks
     */
    public ADaFlightListener(Plugin plugin, boolean hookIntoNCP)
    {
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "DaFlight", this);
        this.ncp = hookIntoNCP;
        this.ncpCheck(plugin);
    }

    public void onPluginMessageReceived(String s, Player p, byte[] b)
    {
        if (s.equalsIgnoreCase("DaFlight"))
        {
            DFMessageType t = DFMessageType.GENERIC;
            if (b.length == 1 && b[0] == 1)
            {
                t = DFMessageType.QUERY;
                log("DaFlight query received from " + p.getName());
            }
            else if (b.length == 2 && b[0] == 2)
            {
                if (b[1] == 1)
                {
                    t = DFMessageType.DFTOGGLEON;
                }
                else if (b[1] == 0)
                {
                    t = DFMessageType.DFTOGGLEOFF;
                }
            }
            if (!t.equals(DFMessageType.GENERIC))
            {
                onDaFlightMessage(p, t);
            }
        }
    }

    public void exempt(Player p)
    {
        if (this.ncp && !p.isOp())
        {
            NCPExemptionManager.exemptPermanently(p, CheckType.MOVING);
        }
        onExempt(p);
    }

    public void unExempt(Player p)
    {
        if (this.ncp && !p.isOp())
        {
            NCPExemptionManager.unexempt(p, CheckType.MOVING);
        }
        onUnExempt(p);
    }

    private void ncpCheck(final Plugin p)
    {
        if (this.ncp)
        {
            Bukkit.getScheduler().runTask(p, new Runnable()
            {
                @Override
                public void run()
                {
                    if (Bukkit.getPluginManager().getPlugin("NoCheatPlus") != null)
                    {
                        ncp = true;
                        log("Found NoCheatPlus!");
                    }
                    else
                    {
                        ncp = false;
                        log("Did not find NoCheatPlus!");
                    }
                }
            });
        }
    }

    /**
     * Called when a DaFlight message is received from a client
     * @param p Player - from whom the DaFlight message came
     * @param t DFMessageType - the type of message that the player has sent
     */
    public abstract void onDaFlightMessage(Player p, DFMessageType t);

    /**
     * Called after a player has been exempted from NCP's movement checks
     * @param p Player - the player that has been exempted
     */
    public abstract void onExempt(Player p);

    /**
     * Called after a player has been un-exempted from NCP's movement checks
     * @param p Player - the player that has been un-exempted
     */
    public abstract void onUnExempt(Player p);

    private void log(String s)
    {
        DaFlightManager.inst().getLogger().info(s);
    }

    /**
     * The DFMessageType can be used to differentiate the various messages sent from the client
     */
    public enum DFMessageType
    {
        /**
         * used as an initializer only
         */
        GENERIC,

        /**
         * sent by the client when joining a server, the response should be to check their permissions and return the appropriate enable/disable messages
         */
        QUERY,

        /**
         * sent by the client when toggling Sprint/Fly mod on
         */
        DFTOGGLEON,

        /**
         * DFTOGGLEOFF - sent by the client when toggling Sprint/Fly mod off
         */
        DFTOGGLEOFF
    }

}
