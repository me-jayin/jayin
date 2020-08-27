package xyz.me4cxy.idempotent;

/**
 * 接口幂等性异常
 * @author Jayin
 * @create 2020/6/18
 */
public class ApiIdempotentException extends RuntimeException {
    public ApiIdempotentException() {
        super();
    }

    public ApiIdempotentException(String message) {
        super(message);
    }

    public ApiIdempotentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiIdempotentException(Throwable cause) {
        super(cause);
    }
}