package api.safecomm.blockchain.service;

/**
 * Created by geatz on 7/8/17.
 *
 * Description: representation class which Jackson uses to
 * marshal instances of type EmailResult into JSON
 *
 * The returned JSON is formatted as such:
 *
 * {"result":"success","id":1}
 *
 */
public class BlockChainResult {

    private String result;
    private String blockHash;

    public BlockChainResult(String blockHash, String result) {
        this.result = result;
        this.blockHash = blockHash;
    }

    public String getResult() {
        return result;
    }

    public String getBlockHash() {
        return blockHash;
    }
}
