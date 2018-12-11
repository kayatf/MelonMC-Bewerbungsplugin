package yt.syntax.melonmc.tasks;

import yt.syntax.melonmc.misc.MathUtil;
import yt.syntax.melonmc.stats.PointsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * created on 03/12/2018 / 23:32
 * @author Daniel Riegler
 */
public class PointReminderRunnable implements Runnable
{

    /* Reminds every player on the server of their current points account */
    @Override
    public void run ( )
    {
        Bukkit.getOnlinePlayers ( ).forEach ( player -> {
            final long points = PointsManager.getPoints ( player );
            if ( points == - 1 ) return;
            if ( MathUtil.isEven ( points ) ) player.sendMessage ( ChatColor.YELLOW + "Deine Punkte: " + points );
            else player.sendTitle ( ChatColor.YELLOW + "Deine Punkte:", ChatColor.GOLD + String.valueOf ( points ) );
        } );
    }

}
