package api.safecomm.web;

import java.security.NoSuchAlgorithmException;
import api.safecomm.blockchain.service.BlockChainImpl;
import api.safecomm.blockchain.service.BlockChainResult;
import api.safecomm.blockchain.service.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class PrimaryController {

    @Autowired
    BlockChainService blockChainService;

    /**
     *
     * @param blockChainImpl pojo for req obj
     * @return {json} success / fail message
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value = "/addBlock", method = RequestMethod.POST)
    public BlockChainResult addBlock(@RequestBody BlockChainImpl blockChainImpl)
            throws NoSuchAlgorithmException {

        String blockData = blockChainImpl.getBlockData();

        BlockChainService blockChainService = new BlockChainService(blockData);

        String blockHash = blockChainService.getBlockHash();

        // create new block
        if ( blockChainService.createBlock() ) {
            return new BlockChainResult(blockHash, "SUCCESS - A new block has been added to the chain.");
        } else {
            return new BlockChainResult("Failure to create block.", "ERROR - Conflict exists in block chain.");
        }
    }

}
