package xyz.me4cxy.util.method;

import java.lang.reflect.Parameter;

/**
 * 参数信息
 * @author Jayin
 * @create 2020/7/9
 */
public class ParameterInfo {
    private String name;
    private Parameter type;
    private Object value;

    ParameterInfo(String name, Parameter type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameter getType() {
        return type;
    }

    public void setType(Parameter type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}