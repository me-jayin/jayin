package xyz.me4cxy.components.sign;

import java.lang.annotation.*;

/**
 * 需要加密校验的属性
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SignFields {
    /**
     * 加密属性，可用支持 . 获取子属性
     * @return
     */
    String[] fields();

    /**
     * 请求头参数名称
     * @return
     */
    String headerName() default "sign";

    /**
     * 待加密数据拼接分隔符
     * @return
     */
    String separation() default "";
}
