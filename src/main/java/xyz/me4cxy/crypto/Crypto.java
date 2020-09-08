package xyz.me4cxy.crypto;

public interface Crypto {
    /**
     * 加密
     * @param estr
     * @return
     */
    String encrypt(String estr);

    /**
     * 解密
     * @param dstr
     * @return
     */
    String decrypt(String dstr);
}
