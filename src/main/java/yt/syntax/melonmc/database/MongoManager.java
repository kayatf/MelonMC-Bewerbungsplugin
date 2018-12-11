package yt.syntax.melonmc.database;

import com.mongodb.ConnectionString;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * created on 03/12/2018 / 21:31
 * @author Daniel Riegler
 */
public class MongoManager extends MongoConnection
{

    /**
     * Establish a database connection
     * @param credential  is the database {@link Credential} with inherits from {@link ConnectionString}
     * @param collections are the collections of the database which should get initialized
     */
    public MongoManager ( final Credential credential, final String... collections )
    {
        super ( credential, collections );
    }

    /**
     * Inserts a {@link MongoDocument} into a collection
     * @param document   is the document which should get inserted
     * @param collection is the name of the collection
     * @param consumer   is the result which consists out of {@link Throwable} which is not null if an exception was
     *                   thrown internally
     */
    public void insert ( final MongoDocument document, final String collection, final Consumer < Throwable > consumer )
    {
        super.getCollections ( ).get ( collection ).insertOne (
                Document.parse ( document.toJson ( ) ),
                ( aVoid, throwable ) -> consumer.accept ( throwable )
        );
    }

    /**
     * Updates a {@link MongoDocument} in a collection
     * @param document   is the document which should get updated (has to contain a non-changed uniqueId)
     * @param collection is the name of the collection
     * @param consumer   is the result which consists out of {@link UpdateResult} which contains information about the
     *                   finished query and {@link Throwable} which is not null if an exception was thrown internally
     */
    public void update (
            final MongoDocument document,
            final String collection,
            final BiConsumer < UpdateResult, Throwable > consumer
    )
    {
        super.getCollections ( ).get ( collection ).updateOne (
                document.getFilter ( ),
                new Document ( "$set", document ),
                consumer :: accept
        );
    }

    /**
     * Finds a {@link MongoDocument} in a collection
     * @param uniqueId   is the uniqueId of the data/player
     * @param collection is the name of the collection
     * @param consumer   is the result which consists out of {@link MongoDocument} which is the stored document and a
     *                   {@link Throwable} which is not null if an exception was thrown internally
     */
    public void find (
            final UUID uniqueId,
            final String collection,
            final BiConsumer < MongoDocument, Throwable > consumer
    )
    {
        super.getCollections ( ).get ( collection ).find ( MongoDocument.getFilter ( uniqueId ) ).first ( ( document, throwable ) -> {
            if ( throwable != null ) consumer.accept ( null, throwable );
            else consumer.accept ( new MongoDocument ( document ), null );
        } );
    }

    /**
     * Checks if a {@link MongoDocument} exists in a collection
     * @param uniqueId   is the uniqueId of the data/player to look for
     * @param collection is the name of the collection
     * @param consumer   is the result which provides a {@link Boolean}
     */
    public void exist ( final UUID uniqueId, final String collection, final Consumer < Boolean > consumer )
    {
        super.getCollections ( ).get ( collection ).find ( MongoDocument.getFilter ( uniqueId ) ).
                first ( ( ( document, throwable ) -> consumer.accept ( document != null ) ) );
    }

}
