package xyz.me4cxy.components.sign;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import xyz.me4cxy.components.sign.handler.SignValidateHandler;

/**
 * 签名校验.
 * 由请求中指定重要数据通过加密后生成sign，其中sign需要通过请求头sign参数传入.
 * @author Jayin
 * @create 2020/7/12
 */
@Aspect
public class SignValidateAop {
    private SignValidateHandler handler;

    public SignValidateAop(SignValidateHandler handler) {
        this.handler = handler;
    }

    @Pointcut("@annotation(xyz.me4cxy.components.sign.SignFields)")
    public void cut() {}

    @Around(value = "cut() && @annotation(signFields)")
    public Object process(ProceedingJoinPoint joinPoint, SignFields signFields) throws Throwable {
        return this.handler.handler(joinPoint, signFields);
    }

}