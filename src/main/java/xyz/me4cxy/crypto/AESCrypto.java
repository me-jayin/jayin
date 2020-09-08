package xyz.me4cxy.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES工具类
 * @author Jayin
 * @create 2020/6/16
 */
public class AESCrypto extends AbstractCrypto {
    /**
     * 基于传入的值生成密钥
     */
    public static final int RANDOM_PASSWORD = 1;
    /**
     * 使用传入的值作为密钥
     */
    public static final int KEY = 2;

    private static final String ALGORITHM = "AES";
    /**
     * AES加密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE = "AES/ECB/PKCS5Padding";
    private static final int KEY_SIZE = 128;

    private final Logger logger = LoggerFactory.getLogger(AESCrypto.class);
    private SecretKeySpec secretKeySpec;

    public AESCrypto(String key, int keyType) throws NoSuchAlgorithmException {
        this.resetKey(key, keyType);
    }

    public AESCrypto(SecretKeySpec key) {
        this.secretKeySpec = key;
    }

    public void resetKey(String key, int keyType) throws NoSuchAlgorithmException {
        switch (keyType) {
            case KEY:
                // AES专用密匙
                this.secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
                break;
            case RANDOM_PASSWORD:
                this.secretKeySpec = generateKey(key);
                break;
        }
    }

    public static SecretKeySpec generateKey(String randomKey) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        // 设置初始化大小及随机数键
        keyGen.init(KEY_SIZE, new SecureRandom(randomKey.getBytes()));
        SecretKey secretKey = keyGen.generateKey();
        // AES专用密匙
        return new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
    }

    public String encrypt(String estr) {
        return super.encrypt(ALGORITHM_MODE, secretKeySpec, estr);
    }

    public String decrypt(String dstr) {
        return super.decrypt(ALGORITHM_MODE, secretKeySpec, dstr);
    }
    
    @Override
    protected Logger getLogger() {
        return this.logger;
    }
}