package yt.syntax.melonmc.events.player;

import yt.syntax.melonmc.stats.PointsManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * created on 03/12/2018 / 22:53
 * @author Daniel Riegler
 */
public class PlayerJoinEventListener implements Listener
{

    /**
     * This gets called on any {@link org.bukkit.entity.Player} join
     * @param event is the dispatched event
     */
    @EventHandler ( priority = EventPriority.HIGHEST )
    public void handlePlayerJoin ( final PlayerJoinEvent event )
    {
        final Player player = event.getPlayer ( );
        PointsManager.createIfNotExist ( player, ( ) -> PointsManager.loadAndCreateSession (
                player,
                ( ) -> player.sendMessage ( String.format (
                        ChatColor.YELLOW + "Deine Daten wurden geladen, du hast %s Punkte.",
                        PointsManager.getPoints ( player )
                ) )
        ) );
    }
}
