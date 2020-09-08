package xyz.me4cxy.components.sign.exception;

/**
 * @author Jayin
 * @create 2020/7/13
 */
public class InvalidSignException extends SignException {
    public InvalidSignException() {
    }

    public InvalidSignException(String message) {
        super(message);
    }

    public InvalidSignException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSignException(Throwable cause) {
        super(cause);
    }

    public InvalidSignException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}