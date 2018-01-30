package api.safecomm.util;

import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    private String sha256hex;

    public SHA256() {}

    public String getSha256hex() {
        return sha256hex;
    }

    public void setSha256hex(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        this.sha256hex = new String(Hex.encode(hash));
    }

}
