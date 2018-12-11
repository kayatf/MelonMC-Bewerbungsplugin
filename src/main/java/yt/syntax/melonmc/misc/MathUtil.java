package yt.syntax.melonmc.misc;

import lombok.experimental.UtilityClass;

/**
 * created on 03/12/2018 / 23:35
 * @author Daniel Riegler
 */
@UtilityClass
public class MathUtil
{

    /**
     * Checks if a {@link Number} is even
     * @param number is the number (eg. 1, 2, ...)
     * @return is the number even?
     */
    public static boolean isEven ( final Number number )
    {
        return number.intValue ( ) % 2 == 0;
    }

}
