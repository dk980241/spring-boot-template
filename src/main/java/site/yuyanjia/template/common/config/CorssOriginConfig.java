package site.yuyanjia.template.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * CorssOriginConfig
 *
 * @author seer
 * @date 2018/6/15 10:10
 */
@Configuration
@ConfigurationProperties(prefix = CorssOriginConfig.PREFIX)
public class CorssOriginConfig extends WebMvcConfigurationSupport {
    public static final String PREFIX = "yuyanjia.cross";

    /**
     * 跨域允许地址
     */
    private String[] origins = {"*"};

    /**
     * 跨域匹配地址
     */
    private String mapping = "/**";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(mapping)
                .allowedOrigins(origins)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }

    public String[] getOrigins() {
        return origins;
    }

    public void setOrigins(String[] origins) {
        this.origins = origins;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
