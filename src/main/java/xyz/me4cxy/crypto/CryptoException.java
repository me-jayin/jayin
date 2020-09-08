package xyz.me4cxy.crypto;

/**
 * @author Jayin
 * @create 2020/7/13
 */
public class CryptoException extends RuntimeException {
    public CryptoException() {
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoException(Throwable cause) {
        super(cause);
    }
}