package yt.syntax.melonmc.events.entity;

import yt.syntax.melonmc.stats.PointsManager;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * created on 04/12/2018 / 16:45
 * @author Daniel Riegler
 */
public class EntityDeathEventListener implements Listener
{

    /**
     * This gets called on every {@link org.bukkit.entity.Entity} death
     * @param event is the dispatched event
     */
    @EventHandler ( priority = EventPriority.HIGHEST )
    public void handleEntityDeath ( final EntityDeathEvent event )
    {

        final LivingEntity entity = event.getEntity ( );
        if ( entity.getType ( ) != EntityType.COW ) return;

        final Player player = entity.getKiller ( );
        if ( player == null ) return;

        PointsManager.addPoint ( player );
        player.playSound ( player.getLocation ( ), Sound.LEVEL_UP, 1.0f, 1.0f );

    }

}
