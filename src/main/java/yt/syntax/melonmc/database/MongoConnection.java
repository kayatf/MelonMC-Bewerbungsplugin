package yt.syntax.melonmc.database;

import com.google.common.collect.Maps;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

import java.util.Map;

/**
 * created on 03/12/2018 / 21:31
 * @author Daniel Riegler
 */
@Getter
public class MongoConnection
{

    /* mongodb client which we'll use to manage our database */
    private final MongoClient client;

    /* main database which contains below collections */
    private final MongoDatabase database;

    /* map of all initialized database collections which are stored with a key equally to the collection name */
    private final Map < String, MongoCollection < Document > > collections = Maps.newConcurrentMap ( );

    /**
     * Establish a database connection
     * @param credential  is the database {@link Credential} with inherits from {@link com.mongodb.ConnectionString}
     * @param collections are the collections of the database which should get initialized
     */
    public MongoConnection ( final Credential credential, final String... collections )
    {

        /* creates the mongodb client from the credential */
        this.client = MongoClients.create ( credential );

        /* gets the database from the mongodb client */
        this.database = this.client.getDatabase ( credential.getDatabase ( ) );

        /* creates a database collection for each of the string parameters */
        for ( final String name : collections )
            this.collections.put ( name, this.database.getCollection ( name ) );
    }

}
