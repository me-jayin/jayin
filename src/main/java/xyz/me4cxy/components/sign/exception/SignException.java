package xyz.me4cxy.components.sign.exception;

/**
 * @author Jayin
 * @create 2020/7/13
 */
public class SignException extends RuntimeException {
    public SignException() {
    }

    public SignException(String message) {
        super(message);
    }

    public SignException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignException(Throwable cause) {
        super(cause);
    }

    public SignException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}