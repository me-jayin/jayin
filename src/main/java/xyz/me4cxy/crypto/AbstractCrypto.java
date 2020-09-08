package xyz.me4cxy.crypto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Jayin
 * @create 2020/7/12
 */
public abstract class AbstractCrypto implements Crypto {
    public static final Charset UTF8 = Charset.forName("UTF-8");

    protected Cipher initCipher(String algorithm, int model, Key key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(model, key);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            this.getLogger().error("无效的密匙", e);
            throw new CryptoException("加密工具初始化失败", e);
        }
        return cipher;
    }

    protected String encrypt(String algorithm, Key key, String estr) {
        if (StringUtils.isBlank(estr)) return "";
        Cipher cipher = this.initCipher(algorithm, Cipher.ENCRYPT_MODE, key);
        try {
            return Base64.getEncoder().encodeToString(cipher.doFinal(estr.getBytes(UTF8)));
        } catch (Exception e) {
            throw new CryptoException("加密失败", e);
        }
    }

    protected String decrypt(String algorithm, Key key, String dstr) {
        if (StringUtils.isBlank(dstr)) return "";
        Cipher cipher = this.initCipher(algorithm, Cipher.DECRYPT_MODE, key);
        try {
            byte[] dbt = Base64.getDecoder().decode(dstr);
            return new String(cipher.doFinal(dbt), UTF8);
        } catch (Exception e) {
            throw new CryptoException("解密失败", e);
        }
    }

    protected abstract Logger getLogger();
}