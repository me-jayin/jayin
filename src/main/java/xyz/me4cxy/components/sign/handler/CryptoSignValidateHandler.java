package xyz.me4cxy.components.sign.handler;

import org.apache.commons.lang3.StringUtils;
import xyz.me4cxy.components.sign.exception.InvalidSignException;
import xyz.me4cxy.components.sign.exception.SignException;
import xyz.me4cxy.crypto.Crypto;
import xyz.me4cxy.crypto.CryptoException;

/**
 * 使用加密进行签名校验的处理器.
 * 前端加密时需要对字符串进行切割，确保未加密的字符串不超过36位
 * @author Jayin
 * @create 2020/7/13
 */
public class CryptoSignValidateHandler extends AbstractSignValidateHandler {
    /**
     * 最大属性拼接字符串长度，防止拼接字符过长导致密文过长
     */
    private int maxFieldStringLength = 64;
    /**
     * 签名生成加密方式
     */
    private Crypto crypto;

    public CryptoSignValidateHandler(Crypto crypto) {
        this.crypto = crypto;
    }


    public CryptoSignValidateHandler setCrypto(Crypto crypto) {
        this.crypto = crypto;
        return this;
    }

    @Override
    protected void checkSign(String sign, String fieldsString) throws SignException {
        // 这里采用解密，因为AES解密效率比加密高
        String ds;
        try {
            ds = this.crypto.decrypt(sign);
        } catch (CryptoException e) {
            super.getLogger().info("签名解密失败", e);
            throw new SignException("请求签名无效");
        }
        super.getLogger().debug(
                "---------------- \n" +
                " 签名：{} \n" +
                " 签名解密：{} \n" +
                " 字段数据：{}",
                sign, ds, fieldsString
        );
        // 校验签名，由于使用加密，所以不支持明文中包含空格
        fieldsString = StringUtils.replace(fieldsString, " ", "");
        // 当数据长度过长时加密长度也会随之变长，因此需要切割为设置的最长长度
        fieldsString = StringUtils.substring(fieldsString, 0, maxFieldStringLength);
        if (!StringUtils.equals(ds, fieldsString)) {
            throw new InvalidSignException("请求签名无效");
        }
    }

    public void setMaxFieldStringLength(int maxFieldStringLength) {
        this.maxFieldStringLength = maxFieldStringLength;
    }
}