package api.safecomm.util;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by geatz on 7/20/17.
 */
public class MongoConnect {

    private MongoCollection<Document> collection;

    /**
     * minimal constructor
     */
    public MongoConnect(){}

    /**
     * full constructor
     */
    public MongoConnect(String dbName, String collectionName) {

        // Mongo DB connection, collection, and cursor
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        this.collection = collection;

    }

    public MongoCollection getCollection() {
        return this.collection;
    }

    public MongoClient createConnection() {
        return new MongoClient("localhost", 27017);
    }

}
