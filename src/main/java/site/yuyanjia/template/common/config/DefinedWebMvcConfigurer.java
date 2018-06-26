package site.yuyanjia.template.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * DefinedWebMvcConfigurer
 *
 * @author seer
 * @date 2018/6/15 10:10
 */
@Configuration
@ConfigurationProperties(prefix = DefinedWebMvcConfigurer.PREFIX)
public class DefinedWebMvcConfigurer implements WebMvcConfigurer {
    public static final String PREFIX = "yuyanjia.webmvc";

    /**
     * 跨域允许地址
     */
    private String[] corssOrigins = {"*"};

    /**
     * 跨域匹配地址
     */
    private String corssMapping = "/**";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corssMapping)
                .allowedOrigins(corssOrigins)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public String[] getCorssOrigins() {
        return corssOrigins;
    }

    public void setCorssOrigins(String[] corssOrigins) {
        this.corssOrigins = corssOrigins;
    }

    public String getCorssMapping() {
        return corssMapping;
    }

    public void setCorssMapping(String corssMapping) {
        this.corssMapping = corssMapping;
    }
}
