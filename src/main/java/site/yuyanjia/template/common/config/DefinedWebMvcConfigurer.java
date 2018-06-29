package site.yuyanjia.template.common.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.yuyanjia.template.common.util.FastJsonUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * DefinedWebMvcConfigurer
 *
 * @author seer
 * @date 2018/6/15 10:10
 */
@Configuration
public class DefinedWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 消息转换器
     * <p>
     * {@link StringHttpMessageConverter} 默认编码是 ISO_8859_1 ，这里添加一个 UTF-8 编码的，解决 Controller 返回结果乱码问题
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(fastJsonHttpMessageConverter());
    }


    /**
     * FastJsonHttpMessageConverter
     *
     * @return
     */
    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonConfig.setParserConfig(FastJsonUtil.getParserConfig());

        fastJsonConfig.setSerializeConfig(FastJsonUtil.getSerializeConfig());
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty);

        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return fastJsonHttpMessageConverter;
    }
}
