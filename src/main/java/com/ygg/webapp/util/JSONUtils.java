package com.ygg.webapp.util;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * 包含操作 {@code JSON} 数据的常用方法的工具类。
 * <p />
 * 该工具类使用的 {@code JSON} 转换引擎是 <a href="http://code.google.com/p/google-gson/"
 * mce_href="http://code.google.com/p/google-gson/" target="_blank"> {@code Google Gson}</a>。 下面是工具类的使用案例：
 *
 * @author ray.he
 *
 */
public class JSONUtils
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);
    
    /** 空的 {@code JSON} 数据 - <code>"{}"</code>。 */
    public static final String EMPTY_JSON = "{}";
    
    /** 空的 {@code JSON} 数组(集合)数据 - {@code "[]"}。 */
    public static final String EMPTY_JSON_ARRAY = "[]";
    
    /** 默认的 {@code JSON} 日期/时间字段的格式化模式。 */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * {@code Google Gson} 的 <code>@Since</code> 注解常用的版本号常量 - {@code 1.0}。
     */
    public static final double SINCE_VERSION_10 = 1.0d;
    
    /**
     * {@code Google Gson} 的 <code>@Since</code> 注解常用的版本号常量 - {@code 1.1}。
     */
    public static final double SINCE_VERSION_11 = 1.1d;
    
    /**
     * {@code Google Gson} 的 <code>@Since</code> 注解常用的版本号常量 - {@code 1.2}。
     */
    public static final double SINCE_VERSION_12 = 1.2d;
    
    /**
     * {@code Google Gson} 的 <code>@Until</code> 注解常用的版本号常量 - {@code 1.0}。
     */
    public static final double UNTIL_VERSION_10 = SINCE_VERSION_10;
    
    /**
     * {@code Google Gson} 的 <code>@Until</code> 注解常用的版本号常量 - {@code 1.1}。
     */
    public static final double UNTIL_VERSION_11 = SINCE_VERSION_11;
    
    /**
     * {@code Google Gson} 的 <code>@Until</code> 注解常用的版本号常量 - {@code 1.2}。
     */
    public static final double UNTIL_VERSION_12 = SINCE_VERSION_12;
    
    /**
     * <p>
     * <code>JSONUtils</code> instances should NOT be constructed in standard programming. Instead, the class should be
     * used as <code>JSONUtils.fromJson("foo");</code>.
     * </p>
     * <p>
     * This constructor is public to permit tools that require a JavaBean instance to operate.
     * </p>
     */
    public JSONUtils()
    {
        super();
    }
    
    /**
     * 将给定的目标对象根据指定的条件参数转换成 {@code JSON} 格式的字符串。
     * <p />
     * <strong>该方法转换发生错误时，不会抛出任何异常。若发生错误时，曾通对象返回 <code>"{}"</code>； 集合或数组对象返回 <code>"[]"</code> </strong>
     *
     * @param target 目标对象。
     * @param targetType 目标对象的类型。
     * @param isSerializeNulls 是否序列化 {@code null} 值字段。
     * @param version 字段的版本号注解。
     * @param datePattern 日期字段的格式化模式。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double version, String datePattern, boolean excludesFieldsWithoutExpose,
        boolean disableHtmlEscap)
    {
        if (target == null)
        {
            return EMPTY_JSON;
        }
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls)
        {
            builder.serializeNulls();
        }
        if (version != null)
        {
            builder.setVersion(version.doubleValue());
        }
        if (StringUtils.isBlank(datePattern))
        {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose)
        {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        if (disableHtmlEscap)
        {
            builder.disableHtmlEscaping();
        }
        
        return toJson(target, targetType, builder);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target)
    {
        return toJson(target, null, false, null, null, true, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param datePattern 日期字段的格式化模式。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, String datePattern)
    {
        return toJson(target, null, false, null, datePattern, true, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param version 字段的版本号注解({@literal @Since})。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Double version)
    {
        return toJson(target, null, false, version, null, true, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, boolean excludesFieldsWithoutExpose)
    {
        return toJson(target, null, false, null, null, excludesFieldsWithoutExpose, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param version 字段的版本号注解({@literal @Since})。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Double version, boolean excludesFieldsWithoutExpose)
    {
        return toJson(target, null, false, version, null, excludesFieldsWithoutExpose, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType)
    {
        return toJson(target, targetType, false, null, null, true, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param version 字段的版本号注解({@literal @Since})。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, Double version)
    {
        return toJson(target, targetType, false, version, null, true, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @param datePattern 日期字段的格式化模式。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose, String datePattern)
    {
        // return toJson(target, targetType, false, null,
        // datePattern,
        // excludesFieldsWithoutExpose);
        return toJson(target, targetType, true, null, datePattern, excludesFieldsWithoutExpose, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose)
    {
        return toJson(target, targetType, true, null, null, excludesFieldsWithoutExpose, false);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose, boolean disableHtmlEscap)
    {
        return toJson(target, targetType, true, null, null, excludesFieldsWithoutExpose, disableHtmlEscap);
    }
    
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param version 字段的版本号注解({@literal @Since})。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, Double version, boolean excludesFieldsWithoutExpose)
    {
        return toJson(target, targetType, false, version, null, excludesFieldsWithoutExpose, false);
    }
    
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     *
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param token {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
     * @param datePattern 日期格式模式。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern)
    {
        if (StringUtils.isBlank(json))
        {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (StringUtils.isBlank(datePattern))
        {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        Gson gson = builder.create();
        try
        {
            return (T)gson.fromJson(json, token.getType());
        }
        catch (Exception ex)
        {
            LOGGER.error(json + " 无法转换为 " + token.getRawType().getName() + " 对象!", ex);
            return null;
        }
    }
    
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     *
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param token {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, TypeToken<T> token)
    {
        return fromJson(json, token, null);
    }
    
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean} 对象。</strong>
     *
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param clazz 要转换的目标类。
     * @param datePattern 日期格式模式。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern)
    {
        if (StringUtils.isBlank(json))
        {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (StringUtils.isBlank(datePattern))
        {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        Gson gson = builder.create();
        try
        {
            return gson.fromJson(json, clazz);
        }
        catch (Exception ex)
        {
            LOGGER.error(json + " 无法转换为 " + clazz.getName() + " 对象!", ex);
            return null;
        }
    }
    
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean} 对象。</strong>
     *
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param clazz 要转换的目标类。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz)
    {
        return fromJson(json, clazz, null);
    }
    
    /**
     * 将给定的目标对象根据{@code GsonBuilder} 所指定的条件参数转换成 {@code JSON} 格式的字符串。
     * <p />
     * 该方法转换发生错误时，不会抛出任何异常。若发生错误时，{@code JavaBean} 对象返回 <code>"{}"</code>； 集合或数组对象返回 <code>"[]"</code>。 其本基本类型，返回相应的基本值。
     *
     * @param target 目标对象。
     * @param targetType 目标对象的类型。
     * @param builder 可定制的{@code Gson} 构建器。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.1
     */
    public static String toJson(Object target, Type targetType, GsonBuilder builder)
    {
        if (target == null)
            return EMPTY_JSON;
        Gson gson = null;
        if (builder == null)
        {
            gson = new Gson();
        }
        else
        {
            gson = builder.create();
        }
        String result = EMPTY_JSON;
        try
        {
            if (targetType == null)
            {
                result = gson.toJson(target == null ? "" : target);
            }
            else
            {
                result = gson.toJson(target == null ? "" : target, targetType);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            LOGGER.warn("目标对象 " + target.getClass().getName() + " 转换 JSON 字符串时，发生异常！", ex);
            if (target instanceof Collection<?> || target instanceof Iterator<?> || target instanceof Enumeration<?> || target.getClass().isArray())
            {
                result = EMPTY_JSON_ARRAY;
            }
        }
        return result;
    }
    
    /**
     *
     * @param target
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> JsonStr2Collection(String target, Class<T> clazz)
    {
        if (target == null)
        {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Collection<T> collection = (Collection<T>)gson.fromJson(target, clazz);
        return collection;
    }
    
    private static boolean isNullKeyOrValue(JsonElement value)
    {
        return null == value || value.isJsonNull();
    }
    
    public static String getValue(JsonObject json, String key, String defaultValue)
    {
        JsonElement value = json.get(key);
        if (isNullKeyOrValue(value))
        {
            return defaultValue;
        }
        return value.getAsString();
    }
    
}