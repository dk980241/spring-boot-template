package site.yuyanjia.template.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ApplicationProperties
 *
 * @author seer
 * @date 2018/6/15 9:55
 */
@Component
@ConfigurationProperties(prefix = ApplicationProperties.PREFIX)
public class ApplicationProperties {
    public static final String PREFIX = "yuyanjia";

    /**
     * 跨域允许地址
     */
    private String[] crossOrigins = {"*"};

    /**
     * 跨域匹配地址
     */
    private String crossMapping = "/**";

    public String[] getCrossOrigins() {
        return crossOrigins;
    }

    public void setCrossOrigins(String[] crossOrigins) {
        this.crossOrigins = crossOrigins;
    }

    public String getCrossMapping() {
        return crossMapping;
    }

    public void setCrossMapping(String crossMapping) {
        this.crossMapping = crossMapping;
    }
}