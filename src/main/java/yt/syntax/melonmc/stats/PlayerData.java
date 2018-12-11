package yt.syntax.melonmc.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * created on 03/12/2018 / 22:57
 * @author Daniel Riegler
 */
@Getter
@NoArgsConstructor
public class PlayerData
{

    /* amount of points */
    private long points = 0;

    /* increases the points by one */
    public void addPoint ( )
    {
        this.points++;
    }

}
