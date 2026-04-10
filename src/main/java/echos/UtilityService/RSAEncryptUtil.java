package echos.UtilityService;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.util.Base64;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncryptUtil {

    public static String encrypt(String plainText) {
        try {
            System.out.println("Starting encryption...");
            String publicKeyString = RSAKeyUtil.getPublicKey();
            
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = cipher.doFinal(plainBytes);

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.err.println("Encryption failed: " + e.getMessage());
            throw new IllegalStateException("Encryption failed", e);
        }
    }
}


