package xyz.me4cxy.util.method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * 方法解析器
 * @author Jayin
 * @create 2020/7/9
 */
public class MethodResolver {
    private static final LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    public static String[] getMethodParameterNames(Method method) {
        return PARAMETER_NAME_DISCOVERER.getParameterNames(method);
    }

    /**
     * 解析方法
     * @param method
     * @return
     */
    public static MethodInfos resolverMethod(Method method) {
        return new MethodInfos(method);
    }

    /**
     * 解析方法，并设置方法入参值
     * @param method
     * @param values
     * @return
     */
    public static MethodInfos resolverMethod(Method method, Object[] values) {
        return new MethodInfos(method, values);
    }
}