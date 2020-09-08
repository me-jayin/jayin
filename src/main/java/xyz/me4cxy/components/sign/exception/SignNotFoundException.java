package xyz.me4cxy.components.sign.exception;

/**
 * @author Jayin
 * @create 2020/7/13
 */
public class SignNotFoundException extends SignException {
    public SignNotFoundException() {
    }

    public SignNotFoundException(String message) {
        super(message);
    }

    public SignNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignNotFoundException(Throwable cause) {
        super(cause);
    }

    public SignNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}