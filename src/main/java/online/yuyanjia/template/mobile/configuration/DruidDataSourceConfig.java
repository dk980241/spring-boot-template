package online.yuyanjia.template.mobile.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * druid 数据源配置
 *
 * @author seer
 * @date 2018/1/23 15:58
 */
@Configuration
public class DruidDataSourceConfig {

    @Value("${druid.servlet.username}")
    private String loginUsername;

    @Value("${druid.servlet.password}")
    private String loginPassword;

    /**
     * 配置druid数据源
     *
     * @return
     */
    @Bean(name = "druidDataSoure")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSoure() {
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;
    }

    /**
     * 注册 druid监控servlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", this.loginUsername);
        reg.addInitParameter("loginPassword", this.loginPassword);
        // 禁用HTML页面上的“Reset All”功能
        reg.addInitParameter("resetEnable", "false");
        return reg;
    }

    /**
     * 注册 druid 过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean druidFilter() {
        FilterRegistrationBean reg = new FilterRegistrationBean();
        reg.setFilter(new WebStatFilter());
        reg.addUrlPatterns("/");
        // 忽略资源
        reg.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        return reg;
    }

    /**
     * @Configuartion 参考spring中的 xml配置理解
     * @ConfigurationProperties 根据资源自动注入
     * @Primary 主要的
     */
}
