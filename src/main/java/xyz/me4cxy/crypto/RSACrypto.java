package xyz.me4cxy.crypto;

import cn.hutool.core.codec.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Jayin
 * @create 2020/7/10
 */
public class RSACrypto extends AbstractCrypto {
    private static final String ALGORITHM = "RSA";
    /**
     * 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 512;

    private Logger logger = LoggerFactory.getLogger(RSACrypto.class);

    private Key rsaPublicKey;
    private Key rsaPrivateKey;

    public RSACrypto(String privateKey, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.resetKey(privateKey, publicKey);
    }

    public void resetKey(String privateKey, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Base64.Decoder decoder = Base64.getDecoder();
        KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
        this.rsaPublicKey = factory.generatePublic(new X509EncodedKeySpec(decoder.decode(publicKey)));
        this.rsaPrivateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(decoder.decode(privateKey)));
    }

    /**
     * 使用密钥加密.
     * @param estr
     * @return
     */
    public String encrypt(String estr) {
        return super.encrypt(ALGORITHM, rsaPrivateKey, estr);
    }

    /**
     * 使用密钥解密.
     * @param dstr
     * @return
     */
    @Override
    public String decrypt(String dstr) {
        return super.decrypt(ALGORITHM, rsaPrivateKey, dstr);
    }

    /**
     * 使用公钥加密.
     * @param estr
     * @return
     */
    public String encryptByPublicKey(String estr) {
        return super.encrypt(ALGORITHM, rsaPublicKey, estr);
    }

    /**
     * 使用公钥解密.
     * @param dstr
     * @return
     */
    public String decryptByPublicKey(String dstr) {
        return super.decrypt(ALGORITHM, rsaPublicKey, dstr);
    }
    
    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static RSAKey generateRSAKey() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        return new RSAKey(privateKey, publicKey);
    }

    @Override
    protected Logger getLogger() {
        return this.logger;
    }

    public static class RSAKey {
        private RSAPrivateKey privateKey;
        private RSAPublicKey publicKey;

        public RSAKey(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public String publicKeyBase64String() {
            return Base64Encoder.encode(publicKey.getEncoded());
        }

        public String privateKeyBase64String() {
            return Base64Encoder.encode(privateKey.getEncoded());
        }

        public RSAPrivateKey getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(RSAPrivateKey privateKey) {
            this.privateKey = privateKey;
        }

        public RSAPublicKey getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(RSAPublicKey publicKey) {
            this.publicKey = publicKey;
        }
    }
}