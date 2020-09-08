package xyz.me4cxy.components.idempotent;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口幂等性处理AOP.
 * 仅处理POST/DELETE/PUT接口.
 * 开启后需要在请求头中添加 IDEMPOTENT_TOKEN 参数，并且每个有效操作URL都需要生成对应的随机字符
 * @author Jayin
 * @create 2020/6/18
 */
@Aspect
@Order(199)
//@Component
public class ApiIdempotentAop {
    /**
     * 幂等性token缓存
     */
    private final ConcurrentHashMap<String, Object> tokenCache;
    /**
     * 是否是基于方法进行幂等性拦截
     */
    private final Boolean byMethod = false;

    private Object flag = new Object();

    public ApiIdempotentAop() {
        tokenCache = new ConcurrentHashMap<>();
    }

    /** 切入被PostMapping、DeleteMapping、PutMapping修饰的方法 */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void api() {}

    @Around("api()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获得request对象
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = attributes == null ? null : attributes.getRequest();
        if (request == null) { // 如果不是有效的api接口请求则不处理
            return joinPoint.proceed(joinPoint.getArgs());
        }

        // 如果存在HttpServletRequest对象，也就是有效的请求
        String idt = request.getHeader("Idempotent-Token");
        if (idt == null || idt.isEmpty()) {
            throw new ApiIdempotentException("Idempotent-Token不能为空");
        }

        // 如果基于方法则加上方法
        String token = (byMethod ? "" : (request.getMethod() + ":")) + idt;
        // 保存幂等性令牌，校验是否已存在token，如果存在则立即抛出异常终止执行
        if (tokenCache.putIfAbsent(token, this.flag) != null) {
            throw new ApiIdempotentException("服务器正在处理中，请稍后再试");
        } else {
            try {
                return joinPoint.proceed(joinPoint.getArgs());
            } finally {
                tokenCache.remove(token);
            }
        }
    }
}