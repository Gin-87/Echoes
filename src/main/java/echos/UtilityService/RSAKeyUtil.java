package echos.UtilityService;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyUtil {

    private static final String PUBLIC_KEY_FILE = "public_key.pem";
    private static final String PRIVATE_KEY_FILE = "private_key.pem";

    private static KeyPair keyPair;




    // 初始化时生成密钥对
    static {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate RSA key pair", e);
        }
    }

    // 生成并保存密钥对
    public static void generateAndSaveKeys() throws Exception {
        // 获取公钥和私钥
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 保存公钥到文件
        saveKeyToFile(PUBLIC_KEY_FILE, encodeKeyToPEM("PUBLIC KEY", publicKey.getEncoded()));

        // 保存私钥到文件
        saveKeyToFile(PRIVATE_KEY_FILE, encodeKeyToPEM("PRIVATE KEY", privateKey.getEncoded()));

        System.out.println("Keys generated and saved to files.");
    }

    // 保存密钥到文件
    private static void saveKeyToFile(String fileName, String keyContent) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(keyContent.getBytes());
        }
    }

    // 加载公钥
    public static PublicKey loadPublicKey() throws Exception {
        String keyPEM = new String(Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE)))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(keyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 加载私钥
    public static PrivateKey loadPrivateKey() throws Exception {
        String keyPEM = new String(Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE)))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(keyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    // 将密钥编码为 PEM 格式
    private static String encodeKeyToPEM(String keyType, byte[] encodedKey) {
        return "-----BEGIN " + keyType + "-----\n" +
                Base64.getEncoder().encodeToString(encodedKey) +
                "\n-----END " + keyType + "-----";
    }

    // 获取 RSA 公钥（Base64 编码）
    public static String getPublicKey() {
        PublicKey publicKey = keyPair.getPublic();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    // 获取 RSA 私钥对象
    public static PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public static void main(String[] args) {
        try {
            // 1. 生成并保存密钥
            generateAndSaveKeys();

            // 2. 获取公钥和私钥
            String publicKeyBase64 = getPublicKey();
            PrivateKey privateKeyObject = getPrivateKey();

            // 3. 打印密钥
            System.out.println("Base64 Encoded Public Key: " + publicKeyBase64);
            System.out.println("Private Key Object: " + privateKeyObject);

            // 4. 加载保存的密钥文件
            PublicKey loadedPublicKey = loadPublicKey();
            PrivateKey loadedPrivateKey = loadPrivateKey();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

