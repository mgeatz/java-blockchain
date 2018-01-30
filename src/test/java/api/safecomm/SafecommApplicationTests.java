package api.safecomm;

import api.safecomm.email.EmailService;
import api.safecomm.util.GenerateBlockHash;
import api.safecomm.util.MongoConnect;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SafecommApplicationTests {

	@Test
	public void contextLoads() {
	}


    /**
     * SEND EMAIL
     */

    @Autowired
    private EmailService emailService;

    @Test
    public void testEmail(){
        emailService.sendMail("mgeatz@yahoo.com","Test suite test subject","Test suite test mail text");
    }


    /**
     * VALIDATE BLOCKCHAIN
     */

    private Boolean blockIsValid = true;

	public Boolean getBlockIsValid() {
		return blockIsValid;
	}

	public void setBlockIsValid(Boolean blockIsValid) {
		this.blockIsValid = blockIsValid;
	}

	@Test
	public void blockIsValid() {
		MongoConnect mongoConnect = new MongoConnect("chain", "blocks");
		MongoCollection collection = mongoConnect.getCollection();

		Block<Document> recalculateBlockData = document -> {
			System.out.println("re-calculate the hash " + document.toJson());

			Integer blockIndex = document.getInteger("blockIndex");
			String blockTimestamp = document.getString("blockTimestamp");
			String blockData = document.getString("blockData");
			String previousHash = document.getString("previousHash");
			String blockHash = document.getString("blockHash");

			try {
				GenerateBlockHash calculatedBlockHash = new GenerateBlockHash(blockIndex, blockTimestamp, blockData, previousHash);

				String blockHashRebuilt = calculatedBlockHash.getBlockHash();

				// if re-calculated blockHash equals existing blockHash
				if (blockHashRebuilt.equals(blockHash) && getBlockIsValid()) {
					setBlockIsValid(true);
					System.out.println("SUCCESS: Block is VALID");
				} else {
					setBlockIsValid(false);
					System.out.println("ERROR: Block is IN-VALID");
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			};

		};

		collection.find().forEach(recalculateBlockData);
	}

}
