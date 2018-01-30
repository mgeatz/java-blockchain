package api.safecomm.util;

import java.security.NoSuchAlgorithmException;

public class GenerateBlockHash {

    SHA256 sha256 = new SHA256();
    private String blockHash;

    public GenerateBlockHash(Integer blockIndex, String blockTimestamp, String blockData, String previousHash)
            throws NoSuchAlgorithmException {
        String originalString = Integer.toString(blockIndex) + blockTimestamp + blockData + previousHash; // pre-encrypted str

        sha256.setSha256hex(originalString);
        this.blockHash = sha256.getSha256hex();
    }

    public String getBlockHash() {
        return blockHash;
    }
}
