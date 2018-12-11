package yt.syntax.melonmc.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import yt.syntax.melonmc.database.Credential;

/**
 * created on 03/12/2018 / 21:59
 * @author Daniel Riegler
 */
@UtilityClass
public class Constants
{

    /* credentials of the mongo database */
    public final Credential MONGO_CREDENTIAL = new Credential (
            "127.0.0.1",
            27017,
            "",
            "",
            ""
    ); // Todo fill in valid data

    /** instance of google's {@link Gson} */
    public final Gson GSON = new GsonBuilder ( ).serializeNulls ( ).create ( );

}
