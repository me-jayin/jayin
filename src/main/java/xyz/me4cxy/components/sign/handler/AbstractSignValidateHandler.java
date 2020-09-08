package xyz.me4cxy.components.sign.handler;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.util.FieldUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.me4cxy.components.sign.SignFields;
import xyz.me4cxy.components.sign.exception.SignException;
import xyz.me4cxy.components.sign.exception.SignNotFoundException;
import xyz.me4cxy.util.method.MethodResolver;
import xyz.me4cxy.util.method.MethodInfos;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Jayin
 * @create 2020/7/13
 */
public abstract class AbstractSignValidateHandler implements SignValidateHandler{
    private Logger logger = LoggerFactory.getLogger(CryptoSignValidateHandler.class);
    // 默认null值转成空字符串
    private String nullToString = "";

    @Override
    public Object handler(ProceedingJoinPoint joinPoint, SignFields signFields) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取请求
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = attributes == null ? null : attributes.getRequest();
        if (request == null) {
            this.logger.warn("由于当前方法【{}】调用方式不属于HTTP请求调用，因此不支持使用签名校验", method);
            return joinPoint.proceed(joinPoint.getArgs());
        }
        // 获取签名
        String sign = request.getHeader(signFields.headerName());
        if (StringUtils.isBlank(sign)) {
            throw new SignNotFoundException("请求签名不存在");
        }

        // 解析方法并获取需要参与生成签名的数据
        MethodInfos infos = MethodResolver.resolverMethod(method, joinPoint.getArgs());
        String[] fields = signFields.fields();
        String fieldsString = this.fieldsString(infos, fields, signFields.separation());

        this.checkSign(sign, fieldsString);
        return joinPoint.proceed(joinPoint.getArgs());
    }

    /**
     * 校验签名有效性
     * @param sign 请求插入的签名（已加密字符串）
     * @param fieldsString 请求参数拼接后的数据（待加密字符串）
     * @throws SignException
     */
    protected abstract void checkSign(String sign, String fieldsString) throws SignException;

    /**
     * 根据需要校验的字段进行拼接数据.
     * @param infos
     * @param fields
     * @return
     * @throws IllegalAccessException
     */
    private String fieldsString(MethodInfos infos, String[] fields, String separation) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String field : fields) {
            // 切割得到参数名和嵌套属性
            int firstSeparation = StringUtils.indexOf(field, ".");

            String paramName = null, nestedFields = null;
            if (firstSeparation > 0) {
                paramName = StringUtils.substring(field, 0, firstSeparation);
                nestedFields = StringUtils.substring(field, firstSeparation + 1);
            } else {
                paramName = field;
            }

            // 获取对应入参数据
            Object value = infos.getValueByName(paramName);
            // 根据嵌套关系获取数据
            if (value != null && StringUtils.isNotBlank(nestedFields)) {
                value = FieldUtils.getFieldValue(value, nestedFields);
            }

            // 拼接数据
            if (!first) sb.append(separation);
            if (value == null) {
                sb.append(nullToString);
                this.logger.debug("{}值为null", field);
            } else {
                sb.append(value);
            }
            first = false;
        }
        return sb.toString();
    }

    public AbstractSignValidateHandler setNullToString(String nullToString) {
        this.nullToString = nullToString;
        return this;
    }

    protected Logger getLogger() {
        return logger;
    }
}