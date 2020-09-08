package xyz.me4cxy.util.placeholder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 占位符解析
 * @author Jayin
 * @create 2020/7/9
 */
public class PlaceholderResolver {
    private static final Pattern pattern = Pattern.compile("\\{[^}]+\\}");

    /**
     * 解析键模板中需要占位符。
     * 如：<br/>
     * <code>
     *    user:{user.id} => ["user.id"]
     *    user:{user.id}{} => ["user.id] 如果括号内数据为空则不匹配
     * </code>
     * @param keyPattern
     * @return
     */
    public final static String[] resolver(String keyPattern) {
        Matcher matcher = matcher(keyPattern);
        if (matcher == null) return ArrayUtils.EMPTY_STRING_ARRAY;

        List<String> matchResult = new LinkedList<>();
        while (matcher.find()) {
            String str = matcher.group();
            str = StringUtils.substring(str, 1, str.length() - 1);
            if (StringUtils.isNotBlank(str))
                matchResult.add(str);
        }
        return matchResult.toArray(new String[matchResult.size()]);
    }

    /**
     * 得到匹配结果
     * @param keyPattern
     * @return
     */
    public final static Matcher matcher(String keyPattern) {
        if (StringUtils.isBlank(keyPattern)) return null;
        return pattern.matcher(keyPattern);
    }

    /**
     * 替换占位符
     * @param keyPattern 键模板
     * @param getter 占位符对应的值获取器
     * @return
     */
    public final static String replacePlaceholder(String keyPattern, PlaceholderValueGetter getter) {
        Matcher matcher = matcher(keyPattern);
        if (matcher == null) return keyPattern;
        String key, pv;
        // 遍历匹配结果
        while (matcher.find()) {
            // 获取键对应的值
            key = matcher.group();
            pv = getter.get(StringUtils.substring(key, 1, key.length() - 1)); // 除去{ }
            pv = pv == null ? "" : pv;
            // 替换当前占位符
            keyPattern = matcher.replaceFirst(pv);
            // 重新匹配
            matcher.reset(keyPattern);
        }
        return keyPattern;
    }

}
