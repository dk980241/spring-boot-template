package site.yuyanjia.template.common.config;

import org.springframework.beans.factory.annotation.Autowired;
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
public class CorssOriginConfig extends WebMvcConfigurationSupport {

    @Autowired
    ApplicationProperties applicationProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(applicationProperties.getCrossMapping())
                .allowedOrigins(applicationProperties.getCrossOrigins())
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
