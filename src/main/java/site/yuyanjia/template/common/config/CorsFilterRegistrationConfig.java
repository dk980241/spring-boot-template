package site.yuyanjia.template.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域过滤器
 *
 * @author seer
 * @date 2018/6/29 10:09
 */
@Configuration
@ConfigurationProperties(prefix = CorsFilterRegistrationConfig.PREFIX)
public class CorsFilterRegistrationConfig {
    public static final String PREFIX = "yuyanjia.filter.cors";

    /**
     * 允许跨域地址
     * 允许所有：*
     */
    private List<String> allowedOriginList = new ArrayList<>();

    /**
     * 允许请求头信息
     * 允许所有：*
     */
    private List<String> allowedHeaderList = new ArrayList<>();

    /**
     * 允许请求信息
     * 允许所有：*
     */
    private List<String> allowedMethodList = new ArrayList<>();

    /**
     * 允许暴露信息
     */
    private List<String> exposedHeaderList = new ArrayList<>();

    /**
     * 允许证书
     */
    private Boolean allowCredentials = true;


    /**
     * 缓存时间
     */
    private Long maxAge = 3600L;

    /**
     * 过滤地址
     */
    private String mapping = "";

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean CrosFilterRegistrationBean() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(allowedOriginList);
        corsConfiguration.setAllowedHeaders(allowedHeaderList);
        corsConfiguration.setAllowedMethods(allowedMethodList);
        corsConfiguration.setExposedHeaders(exposedHeaderList);
        corsConfiguration.setMaxAge(maxAge);
        corsConfiguration.setAllowCredentials(allowCredentials);

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration(mapping, corsConfiguration);

        CorsFilter corsFilter = new CorsFilter(configurationSource);

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(corsFilter);
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    public List<String> getAllowedOriginList() {
        return allowedOriginList;
    }

    public void setAllowedOriginList(List<String> allowedOriginList) {
        this.allowedOriginList = allowedOriginList;
    }

    public List<String> getAllowedHeaderList() {
        return allowedHeaderList;
    }

    public void setAllowedHeaderList(List<String> allowedHeaderList) {
        this.allowedHeaderList = allowedHeaderList;
    }

    public List<String> getAllowedMethodList() {
        return allowedMethodList;
    }

    public void setAllowedMethodList(List<String> allowedMethodList) {
        this.allowedMethodList = allowedMethodList;
    }

    public List<String> getExposedHeaderList() {
        return exposedHeaderList;
    }

    public void setExposedHeaderList(List<String> exposedHeaderList) {
        this.exposedHeaderList = exposedHeaderList;
    }

    public Boolean getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(Boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public Long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
