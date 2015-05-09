package me.dags.daflightmanager;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.entity.Player;

/**
 * @author dags_ <dags@dags.me>
 */

public class NCPHelper
{
    public void movementExempt(Player p)
    {
        if (!p.isOp())
        {
            NCPExemptionManager.exemptPermanently(p, CheckType.MOVING);
        }
    }

    public void movementUnExempt(Player p)
    {
        if (!p.isOp())
        {
            NCPExemptionManager.unexempt(p, CheckType.MOVING);
        }
    }

    public void noClipExempt(Player p)
    {
    }

    public void noClipUnExempt(Player p)
    {
    }
}
