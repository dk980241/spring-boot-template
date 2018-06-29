package site.yuyanjia.template.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * fastjson 工具
 *
 * @author seer
 * @date 2018/5/10 19:17
 */
public class FastJsonUtil {

    /**
     * 序列化配置
     */
    private volatile static SerializeConfig serializeConfig;

    /**
     * 反序列化配置
     */
    private volatile static ParserConfig parserConfig;

    /**
     * 获取序列化配置
     *
     * @return
     */
    public static SerializeConfig getSerializeConfig() {
        if (null == serializeConfig) {
            synchronized (FastJsonUtil.class) {
                serializeConfig = new SerializeConfig();
            }
        }
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        return serializeConfig;
    }

    /**
     * 获取反序列化配置
     *
     * @return
     */
    public static ParserConfig getParserConfig() {
        if (null == parserConfig) {
            synchronized (FastJsonUtil.class) {
                parserConfig = new ParserConfig();
            }
        }
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        return parserConfig;
    }


    /**
     * 解析请求数据
     *
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static synchronized <T> T parseJsonString(String jsonString, Class<T> clazz) {
        ParserConfig parserConfig = FastJsonUtil.getParserConfig();
        return JSON.parseObject(jsonString, clazz, parserConfig);
    }
}
