package me.dags.daflightmanager;

import org.spongepowered.api.Platform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.network.*;

import java.util.function.Function;

/**
 * @author dags <dags@dags.me>
 */
class ChannelHandler implements RawDataListener
{
    private final ChannelBinding.RawDataChannel channel;
    private final Function<Player, Float> getSpeed;

    ChannelHandler(ChannelBinding.RawDataChannel channel, Function<Player, Float> getSpeed)
    {
        this.channel = channel;
        this.getSpeed = getSpeed;
    }

    @Override
    public void handlePayload(ChannelBuf data, RemoteConnection connection, Platform.Type side)
    {
        if (!(connection instanceof PlayerConnection))
        {
            return;
        }
        // User has toggled Sprint/Fly on, send back their max allowed speed
        if (data.array().length == 1 && data.array()[0] == 1)
        {
            Player player = ((PlayerConnection) connection).getPlayer();
            float speed = getSpeed.apply(player);
            channel.sendTo(player, buf -> buf.writeFloat(speed));
        }
    }
}
