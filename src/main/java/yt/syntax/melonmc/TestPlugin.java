package yt.syntax.melonmc;

import lombok.Getter;
import yt.syntax.melonmc.database.MongoManager;
import yt.syntax.melonmc.events.entity.EntityDeathEventListener;
import yt.syntax.melonmc.events.player.PlayerJoinEventListener;
import yt.syntax.melonmc.events.player.PlayerQuitEventListener;
import yt.syntax.melonmc.misc.Constants;
import yt.syntax.melonmc.stats.PointsManager;
import yt.syntax.melonmc.tasks.PointReminderRunnable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * created on 03/12/2018 / 21:27
 * @author Daniel Riegler
 */
public class TestPlugin extends JavaPlugin
{

    /** instance of our {@link JavaPlugin} */
    @Getter
    private static TestPlugin instance;

    /* database related mongodb manager */
    @Getter
    private MongoManager mongoManager;

    /* called before the main world from the 'server.properties' get's initialized */
    @Override
    public void onLoad ( )
    {
        instance = this;
    }

    /* called after the main world's been loaded and we're able to manipulate the game */
    @Override
    public void onEnable ( )
    {

        final PluginManager pluginManager = super.getServer ( ).getPluginManager ( );

        /* connect to database */
        try
        {
            mongoManager = new MongoManager ( Constants.MONGO_CREDENTIAL, "playerData" );
        }
        catch ( final Exception e )
        {
            System.err.println ( String.format (
                    "Fehler beim Verbinden zur Datenbank. (%s: %s)",
                    e.getClass ( ).getCanonicalName ( ),
                    e.getLocalizedMessage ( )
            ) );
            pluginManager.disablePlugin ( instance );
            return;
        }

        final PlayerJoinEventListener joinEventListener = new PlayerJoinEventListener ( );

        /* register events */
        pluginManager.registerEvents ( joinEventListener, instance );
        pluginManager.registerEvents ( new PlayerQuitEventListener ( ), instance );
        pluginManager.registerEvents ( new EntityDeathEventListener ( ), instance );

        /* start reminding task */
        super.getServer ( ).getScheduler ( ).scheduleSyncRepeatingTask (
                instance,
                new PointReminderRunnable ( ),
                0L,
                20 * 10L
        );

        /* loads all players from database */
        super.getServer ( ).getOnlinePlayers ( ).forEach ( player -> {
            final PlayerJoinEvent event = new PlayerJoinEvent ( player, null );
            joinEventListener.handlePlayerJoin ( event );
        } );

    }

    /* called on stop of the plugin (be it a server restart/stop or plugin reload) */
    @Override
    public void onDisable ( )
    {

        /* stops all tasks */
        super.getServer ( ).getScheduler ( ).cancelTasks ( instance );

        /* removes all listeners */
        HandlerList.unregisterAll ( instance );

        /* saves all players to the database */
        super.getServer ( ).getOnlinePlayers ( ).forEach ( PointsManager :: storeAndRemoveSession );

        /* close database connection */
        if ( this.mongoManager != null ) this.mongoManager.getClient ( ).close ( );

        /* unregisters reference of this class */
        instance = null;

    }
}
