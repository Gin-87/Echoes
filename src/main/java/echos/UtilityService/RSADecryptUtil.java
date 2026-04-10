package echos.UtilityService;

import echos.UtilityService.ExceptionManagement.RSADecryptionException;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.util.Base64;



import java.nio.charset.StandardCharsets;

public class RSADecryptUtil {

    public static String decrypt(String encryptedData) {
        try {
            System.out.println("Starting decryption...");
            PrivateKey privateKey = RSAKeyUtil.getPrivateKey();
            System.out.println("Private Key Loaded.");

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("Decryption failed: " + e.getMessage());
            throw new IllegalStateException("Decryption failed", e);
        }
    }
}
