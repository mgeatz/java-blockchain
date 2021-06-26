package api.safecomm.blockchain.service;

import api.safecomm.blockchain.dao.BlockChainDao;
import api.safecomm.util.GenerateBlockHash;
import com.mongodb.Block;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BlockChainService {

    BlockChainDao blockChainDao = new BlockChainDao("chain", "blocks");
    private Boolean blockIsValid = true;

    private Integer blockIndex;
    private String blockData;
    private String previousHash;
    private Integer previousIndex;
    private String blockTimestamp;
    private String blockHash;

    private MongoCollection duplicateCollection;
    private MongoNamespace blocksNamespace;

    public BlockChainService() {}

    public BlockChainService (String data) throws NoSuchAlgorithmException {

        Integer previousIndex = blockChainDao.getPreviousIndex(); // fetch the last block's index

        if (previousIndex == null) {
            createFirstBlock();
        }

        this.previousIndex = previousIndex; // add 1 to previous hash to create new
        this.blockIndex = previousIndex + 1;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Date date = new Date();
        this.blockTimestamp = formatter.format(date);

        this.blockData = data;
        this.previousHash = blockChainDao.getPreviousHash(); // fetch the last block's previousHash

        GenerateBlockHash calculatedBlockHash = new GenerateBlockHash(blockIndex, blockTimestamp, blockData, previousHash);
        this.blockHash = calculatedBlockHash.getBlockHash();
    }


    /**
     * To ensure the integrity of the block chain, prior to adding a new block we will recursively check all existing
     * blocks, re-calcuting their hash based on required encryptedHash() parameters, & comparing against hash stored
     * on the block record within the DB.
     */
    Block<Document> recalculateBlockData = document -> {
        Integer blockIndex = document.getInteger("blockIndex");
        String blockTimestamp = document.getString("blockTimestamp");
        String blockData = document.getString("blockData");
        String previousHash = document.getString("previousHash");
        String blockHash = document.getString("blockHash");
        String blockHashRebuilt;

        try {
            MongoCollection duplicateCollection = getDuplicateCollection();
            duplicateCollection.insertOne(document);

            GenerateBlockHash calculatedBlockHash = new GenerateBlockHash(blockIndex, blockTimestamp, blockData, previousHash);
            blockHashRebuilt = calculatedBlockHash.getBlockHash();
        } catch (NoSuchAlgorithmException e) {
            blockHashRebuilt = "ERROR";
            e.printStackTrace();
        }

        // if re-calculated blockHash equals existing blockHash
        if (blockHashRebuilt.equals(blockHash) && getBlockIsValid()) {
            setBlockIsValid(true);
        } else {
            setBlockIsValid(false);
        }
    };

    public Boolean validateBlock(Integer blockIndex,
                              String previousHash,
                              Integer previousIndex,
                              String blockHash) {

        MongoCollection collection = blockChainDao.getDatabaseCollection();
        this.blocksNamespace = collection.getNamespace();

        if (previousHash != null && previousIndex != null) {
            if (previousIndex.equals(blockIndex)) {
                // basic check: test against the previous block index
                return false;
            } else if (previousHash.equals(blockHash)) {
                // basic check: test against the previous block hash
                return false;
            } else {
                // advanced check: re-calculate all of the block hashes based on previous block data to ensure integrity
                collection.find().forEach(recalculateBlockData);
                return getBlockIsValid();
            }
        } else {
            System.out.println("Oh no, something is null");
            return false;
        }
    }

    // print collection items
    //Block<Document> printBlock = document -> System.out.println(document.toJson());

    public Boolean createBlock() {
        // get data points
        Integer blockIndex = getBlockIndex();
        String blockData = getBlockData();
        String previousHash = getPreviousHash();
        Integer previousIndex = getPreviousIndex();
        String timestamp = getBlockTimestamp();
        String blockHash = getBlockHash();

        blockChainDao.createDuplicateBlock(blockHash);
        this.duplicateCollection = blockChainDao.getDuplicateCollection();

        Boolean validateBlock = validateBlock(blockIndex, previousHash, previousIndex, blockHash);

        // validate data points
        if (validateBlock) {
            // send data points to DB (DAO)
            blockChainDao.addBlock(blockIndex, blockData, previousHash, timestamp, blockHash);
            blockChainDao.dropPreviousHashCollection(previousHash);

            return true;
        } else {
            System.out.println("ERROR creating Block. Tampered block detected.");
            return false;
        }
    }

    private void createFirstBlock() throws NoSuchAlgorithmException {
        BlockChainDao blockChainDao = new BlockChainDao("chain", "blocks");
        GenerateBlockHash calculatedBlockHash = new GenerateBlockHash(0, "YOLO", "210", "0");
        String blockHashRebuilt = calculatedBlockHash.getBlockHash();
        blockChainDao.addBlock(0, "21000000", "0", "YOLO", blockHashRebuilt);
    }

    public String getBlockData() {
        return blockData;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public Integer getPreviousIndex() {
        return previousIndex;
    }

    public Integer getBlockIndex() {
        return blockIndex;
    }

    public String getBlockTimestamp() {
        return blockTimestamp;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public Boolean getBlockIsValid() {
        return blockIsValid;
    }

    public void setBlockIsValid(Boolean blockIsValid) {
        this.blockIsValid = blockIsValid;
    }

    public MongoCollection getDuplicateCollection() {
        return duplicateCollection;
    }

    public MongoNamespace getBlocksNamespace() {
        return blocksNamespace;
    }

    public void setBlocksNamespace(MongoNamespace blocksNamespace) {
        this.blocksNamespace = blocksNamespace;
    }
}
