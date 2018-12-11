package yt.syntax.melonmc.events.player;

import yt.syntax.melonmc.stats.PointsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * created on 03/12/2018 / 22:53
 * @author Daniel Riegler
 */
public class PlayerQuitEventListener implements Listener
{

    /**
     * This gets called on any {@link org.bukkit.entity.Player} quit
     * @param event is the dispatched event
     */
    @EventHandler ( priority = EventPriority.HIGHEST )
    public void handlePlayerQuit ( final PlayerQuitEvent event )
    {
        PointsManager.storeAndRemoveSession ( event.getPlayer ( ) );
    }

    /**
     * This gets called on any {@link org.bukkit.entity.Player} kick
     * @param event is the dispatched event
     */
    @EventHandler ( priority = EventPriority.HIGHEST )
    public void handlePlayerKick ( final PlayerKickEvent event )
    {
        PointsManager.storeAndRemoveSession ( event.getPlayer ( ) );
    }

}
