package xyz.me4cxy.components.sign.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import xyz.me4cxy.components.sign.SignFields;

/**
 * 签名校验处理器
 * @author Jayin
 * @create 2020/7/13
 */
public interface SignValidateHandler {
    Object handler(ProceedingJoinPoint joinPoint, SignFields signFields) throws Throwable;
}