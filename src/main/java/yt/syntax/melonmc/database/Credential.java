package yt.syntax.melonmc.database;

import com.mongodb.ConnectionString;

/**
 * created on 03/12/2018 / 21:35
 * @author Daniel Riegler
 */
public class Credential extends ConnectionString
{
    /**
     * Creates the {@link ConnectionString} from method parameters
     * @param host     is the hostname of the mongodb server
     * @param port     is the port of the mongodb server
     * @param database is the database which should be used
     * @param user     is the user to login into the database
     * @param password is the password of user
     */
    public Credential (
            final String host,
            final int port,
            final String database,
            final String user,
            final String password
    )
    {
        super ( String.format ( "mongodb://%s:%s@%s:%s/%s", user, password, host, port, database ) );
    }

}
