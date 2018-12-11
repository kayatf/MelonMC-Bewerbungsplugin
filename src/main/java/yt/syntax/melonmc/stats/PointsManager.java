package yt.syntax.melonmc.stats;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import yt.syntax.melonmc.TestPlugin;
import yt.syntax.melonmc.database.MongoDocument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * created on 03/12/2018 / 22:57
 * @author Daniel Riegler
 */
@UtilityClass
public class PointsManager
{

    /* reference of the plugin main class */
    private final TestPlugin PLUGIN = TestPlugin.getInstance ( );

    /** local stored {@link PlayerData} "cache" */
    private final Map < UUID, PlayerData > PLAYER_DATA_MAP = Maps.newConcurrentMap ( );

    /**
     * Get the locally stored points of a {@link Player}
     * @param player is the player from whom to get the points
     * @return is the amount of points as a long
     */
    public long getPoints ( final Player player )
    {
        final UUID uuid = player.getUniqueId ( );
        if ( ! player.isOnline ( ) || ! PLAYER_DATA_MAP.containsKey ( uuid ) ) return - 1;
        return PLAYER_DATA_MAP.get ( uuid ).getPoints ( );
    }

    /**
     * Adds a point to the {@link Player}'s local storage
     * @param player is the player for whom to increase the points
     */
    public void addPoint ( final Player player )
    {
        final UUID uuid = player.getUniqueId ( );
        if ( ! player.isOnline ( ) || ! PLAYER_DATA_MAP.containsKey ( uuid ) ) return;
        PLAYER_DATA_MAP.get ( uuid ).addPoint ( );
    }

    /**
     * Creates a new {@link Player} in the database if he not exists
     * @param player is the player which will get created
     */
    public void createIfNotExist ( final Player player, final Runnable runnable )
    {
        final UUID uuid = player.getUniqueId ( );
        PLUGIN.getMongoManager ( ).exist ( uuid, "playerData", exist -> {
            if ( exist ) runnable.run ( );
            else
            {
                PLUGIN.getMongoManager ( ).insert (
                        new MongoDocument ( uuid, new PlayerData ( ) ),
                        "playerData",
                        throwable -> {
                            if ( throwable != null )
                            {
                                player.kickPlayer ( ChatColor.RED + String.format (
                                        "%s: %s",
                                        throwable.getClass ( ).getSimpleName ( ),
                                        throwable.getLocalizedMessage ( )
                                ) );
                                System.err.println ( String.format (
                                        "Could not insert %s[%s] into database due to %s: %s",
                                        player.getName ( ),
                                        player.getUniqueId ( ).toString ( ),
                                        throwable.getClass ( ).getCanonicalName ( ),
                                        throwable.getLocalizedMessage ( )
                                ) );
                                return;
                            }
                            runnable.run ( );
                            System.out.println ( String.format (
                                    "Player %s[%s] successfully inserted into database.\n",
                                    player.getName ( ),
                                    player.getUniqueId ( ).toString ( )
                                                 )
                            );
                        }
                );
            }
        } );
    }

    /**
     * Creates and loads a new session for a {@link Player}
     * @param player   is the player for whom to create a session
     * @param runnable will get called after successfully created the session
     */
    public void loadAndCreateSession ( final Player player, final Runnable runnable )
    {
        final UUID uuid = player.getUniqueId ( );
        if ( ! player.isOnline ( ) || PLAYER_DATA_MAP.containsKey ( uuid ) ) return;
        PLUGIN.getMongoManager ( ).find ( uuid, "playerData", ( document, throwable ) -> {
            if ( throwable != null )
            {
                player.kickPlayer ( ChatColor.RED + String.format (
                        "%s: %s",
                        throwable.getClass ( ).getSimpleName ( ),
                        throwable.getLocalizedMessage ( )
                ) );
                System.err.println ( String.format (
                        "Could not load %s[%s] in database due to %s: %s",
                        player.getName ( ),
                        player.getUniqueId ( ).toString ( ),
                        throwable.getClass ( ).getCanonicalName ( ),
                        throwable.getLocalizedMessage ( )
                ) );
                return;
            }
            final PlayerData data = MongoDocument.fromDocument ( document, PlayerData.class );
            System.out.println ( String.format (
                    "Player %s[%s] successfully got a new session with %s points.\n",
                    player.getName ( ),
                    player.getUniqueId ( ).toString ( ),
                    data.getPoints ( )
            ) );
            PLAYER_DATA_MAP.put ( uuid, data );
            runnable.run ( );
        } );
    }

    /**
     * Stores the current session of a {@link Player} in the database
     * @param player is the player to store
     */
    public void storeAndRemoveSession ( final Player player )
    {
        final UUID uuid = player.getUniqueId ( );
        if ( ! player.isOnline ( ) || ! PLAYER_DATA_MAP.containsKey ( uuid ) ) return;
        final PlayerData data = PLAYER_DATA_MAP.get ( uuid );
        PLUGIN.getMongoManager ( ).update ( new MongoDocument ( uuid, data ), "playerData", ( result, throwable ) -> {
            PLAYER_DATA_MAP.remove ( uuid );
            if ( throwable != null )
            {
                System.err.println ( String.format (
                        "Could not update %s[%s] in database due to %s: %s",
                        player.getName ( ),
                        player.getUniqueId ( ).toString ( ),
                        throwable.getClass ( ).getCanonicalName ( ),
                        throwable.getLocalizedMessage ( )
                ) );
                return;
            }
            System.out.println ( String.format (
                    "Player %s[%s] successfully updated in database with %s points.\n",
                    player.getName ( ),
                    player.getUniqueId ( ).toString ( ),
                    data.getPoints ( )
            ) );
        } );
    }

}
