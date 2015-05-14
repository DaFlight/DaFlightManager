package me.dags.daflightmanager.utils;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import me.dags.daflightmanagercommon.utils.ExemptionHandler;
import org.bukkit.entity.Player;

/**
 * @author dags_ <dags@dags.me>
 */

public class NCPExemptionHandler implements ExemptionHandler<Player>
{
    @Override
    public void exemptFlyer(Player player)
    {
        if (!player.isOp())
        {
            NCPExemptionManager.exemptPermanently(player, CheckType.MOVING);
        }
    }

    @Override
    public void unExemptFlyer(Player player)
    {
        if (!player.isOp())
        {
            NCPExemptionManager.unexempt(player, CheckType.MOVING);
        }
    }

    @Override
    public void exemptNoClipper(Player player)
    {

    }

    @Override
    public void unExemptNoCliiper(Player player)
    {

    }
}
