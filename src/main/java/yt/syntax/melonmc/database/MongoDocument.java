package yt.syntax.melonmc.database;

import com.mongodb.client.model.Filters;
import yt.syntax.melonmc.misc.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;

/**
 * created on 03/12/2018 / 21:57
 * @author Daniel Riegler
 */
public class MongoDocument extends Document
{

    /**
     * Creates a new {@link Document} from another one
     * @param document is the document which will get 'cloned'
     */
    public MongoDocument ( final Document document )
    {
        super ( document );
    }

    /**
     * Creates a new {@link Document} with preset values
     * @param uniqueId is the {@link UUID} of the object/player stored in it
     * @param object   can be any json serializable data
     */
    public MongoDocument ( final UUID uniqueId, final Object object )
    {
        super ( new Document ( "uuid", uniqueId.toString ( ) ).append (
                "data",
                Document.parse ( Constants.GSON.toJson ( object ) )
        ) );
    }

    /**
     * Gets a generic object of any json deserializable data from a {@link Document}
     * @param document is the {@link Document} which contains the data (uniqueId will be ignored)
     * @param type     is the generic type class of the data/object (eg. String.class)
     * @param <T>      is the generic type itself
     * @return is the generic return value which varies
     */
    public static < T > T fromDocument ( final Document document, final Class < T > type )
    {
        return Constants.GSON.fromJson ( document.get ( "data", Document.class ).toJson ( ), type );
    }

    /**
     * Generates a {@link Bson} filter to find a {@link Document} in a collection
     * @param uniqueId is the {@link UUID} of the object/player's document
     * @return is the filter as a bson
     */
    public static Bson getFilter ( final UUID uniqueId )
    {
        return Filters.eq ( "uuid", uniqueId.toString ( ) );
    }

    /**
     * Generates a {@link Bson} filter to find a matching {@link Document} in a collection
     * @return is the filter as a bson
     */
    public Bson getFilter ( )
    {
        return Filters.eq ( "uuid", get ( "uuid", String.class ) );
    }

}
