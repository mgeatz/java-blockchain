package api.safecomm.blockchain.dao;

import api.safecomm.util.MongoConnect;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

public class BlockChainDao {

    private MongoCollection duplicateCollection;
    private MongoCollection databaseCollection;
    private MongoDatabase database;
    private Integer previousIndex;
    private String previousHash;
    private String blockTimestamp;
    private String blockData;

    // TODO: find more efficient process to retriveing the lastest blockIndex & previousHash
    Block<Document> setPreviousIdentifiers = document -> {
        setPreviousIndex(document.getInteger("blockIndex"));
        setPreviousHash(document.getString("blockHash"));
    };

    public BlockChainDao(){}

    public BlockChainDao(String db, String collection) {
        MongoConnect mongoConnect = new MongoConnect(db, collection);
        this.databaseCollection = mongoConnect.getCollection();

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("chain");
        this.database = database;

        databaseCollection.find().sort(Sorts.ascending("blockIndex")).forEach(setPreviousIdentifiers);
    }

    public void addBlock(Integer blockIndex,
                         String blockData,
                         String previousHash,
                         String blockTimestamp,
                         String blockHash) {

        MongoCollection collection = getDatabaseCollection();

        Document document = new Document("blockIndex", blockIndex)
                .append("blockData", blockData)
                .append("previousHash", previousHash)
                .append("blockTimestamp", blockTimestamp)
                .append("blockHash", blockHash);

        collection.insertOne(document);

    }

    /**
     * Creates a duplicate of the "blocks" collection prior to inserting the new block
     * @param newHash
     */
    public void createDuplicateBlock(String newHash) {
        MongoDatabase database = getDatabase();
        database.createCollection(newHash);

        MongoCollection duplicateCollection = database.getCollection(newHash);
        setDuplicateCollection(duplicateCollection);
    }


    public void dropPreviousHashCollection(String previousHash) {
        MongoDatabase db = getDatabase();

        if (!previousHash.isEmpty()) {

            try {
                MongoCollection oldCollection = db.getCollection(previousHash);
                oldCollection.drop();
            }
            catch (Exception ex) {
                MongoCollection oldCollection = db.getCollection("blocks");
                oldCollection.drop();
            }
        }
    }


    public Integer getPreviousIndex() {
        // make a DB connection and query the previous index
        return previousIndex;
    }

    public String getPreviousHash() {
        // make a DB connection and query the previous index
        return previousHash;
    }

    public String getBlockTimestamp() {
        return blockTimestamp;
    }

    public String getBlockData() {
        return blockData;
    }

    public MongoCollection getDatabaseCollection() {
        return databaseCollection;
    }

    public void setPreviousIndex(Integer previousIndex) {
        this.previousIndex = previousIndex;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setBlockTimestamp(String blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public void setBlockData(String blockData) {
        this.blockData = blockData;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public MongoCollection getDuplicateCollection() {
        return duplicateCollection;
    }

    public void setDuplicateCollection(MongoCollection duplicateCollection) {
        this.duplicateCollection = duplicateCollection;
    }
}
