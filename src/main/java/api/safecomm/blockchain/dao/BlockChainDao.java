package api.safecomm.blockchain.dao;

import api.safecomm.util.MongoConnect;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

public class BlockChainDao {

    private MongoCollection databaseCollection;
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

        // create a new collection as a copy/backup of the most recently valid and up-to-date blockchain

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
}
