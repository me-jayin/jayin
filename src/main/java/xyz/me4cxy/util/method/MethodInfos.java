package xyz.me4cxy.util.method;


import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 参数信息集
 * @author Jayin
 * @create 2020/7/9
 */
public class MethodInfos {
    private String[] names;
    private Parameter[] types;
    private Object[] values;
    private Method method;

    MethodInfos(Method method) {
        this.names = MethodResolver.getMethodParameterNames(method);
        this.types = method.getParameters();
        this.method = method;
    }

    MethodInfos(Method method, Object[] values) {
        this(method);
        this.values = values;
    }

    /**
     * 获取入参信息
     * @param name
     * @return
     */
    public ParameterInfo get(String name) {
        int index = this.indexOfName(name);
        if (index < 0) return null;
        return new ParameterInfo(name, this.getParameter(index), this.getValue(index));
    }

    /**
     * 得到参数名对应的参数位置
     * @param name
     * @return
     */
    public int indexOfName(String name) {
        return ArrayUtils.indexOf(names, name);
    }

    /**
     * 获取指定位置的参数名
     * @param index
     * @return
     */
    public String getName(int index) {
        if (names == null || index < 0 || index > names.length) return null;
        return names[index];
    }

    /**
     * 获取指定位置入参值
     * @param index
     * @return
     */
    public Object getValue(int index) {
        if (values == null || index < 0 || index > values.length) return null;
        return values[index];
    }

    /**
     *
     * @param index
     * @return
     */
    public Parameter getParameter(int index) {
        if (types == null || index < 0 || index > types.length) return null;
        return types[index];
    }

    /**
     * 通过参数名称获得对应的参数.
     * @param name
     * @return
     */
    public Object getValueByName(String name) {
        return this.getValue(this.indexOfName(name));
    }

    /**
     * 返回指定参数的{@link Parameter}
     * @param name
     * @return
     */
    public Parameter getParameterByName(String name) {
        return this.getParameter(this.indexOfName(name));
    }

    /**
     * 得到参数类型。如果不存在该参数时返回{@link Object}
     * @param name
     * @return
     */
    public Class getParamTypeByName(String name) {
        Parameter p = this.getParameterByName(name);
        if (p == null) return Object.class;
        else return p.getType();
    }

    public String[] getNames() {
        return names;
    }

    public Object[] getValues() {
        return values;
    }

    public Parameter[] getTypes() {
        return types;
    }

    public Method getMethod() {
        return method;
    }
}