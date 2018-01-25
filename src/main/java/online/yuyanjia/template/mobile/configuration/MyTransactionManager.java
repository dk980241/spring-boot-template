package online.yuyanjia.template.mobile.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * 事务管理器
 *
 * @author seer
 * @date 2018/1/24 16:32
 */
@Configuration
@AutoConfigureAfter(DruidDataSourceConfig.class)
@EnableTransactionManagement
public class MyTransactionManager implements TransactionManagementConfigurer {
    private static Logger LOGGER = LogManager.getLogger(MyTransactionManager.class);
    /**
     * 默认事务管理器
     */
    @Autowired
    @Qualifier("dataTransactionManager")
    private PlatformTransactionManager defaultManager;

    /**
     * 加载数据库事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "dataTransactionManager")
    public PlatformTransactionManager dataTransactionManager(@Qualifier(value = "druidDataSoure") DataSource dataSource) {
        // Spring容器中，我们手工注解@Bean 将被优先加载，框架不会重新实例化其他的 PlatformTransactionManager 实现类。
        LOGGER.warn("==== 数据库事务管理器 加载 ====");
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 默认事务管理器
     * 存在多个事务管理器的情况下，默认使用的
     *
     * @return
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return defaultManager;
    }

    /**
     * @AutoConfigureAfter(DruidDataSourceConfig.class) 在x之后加载
     * @EnableTransactionManagement 注解事务管理 <tx:annotation-driven />
     */
}


