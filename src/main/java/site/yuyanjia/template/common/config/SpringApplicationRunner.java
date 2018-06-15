package site.yuyanjia.template.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * spring 启动后执行
 *
 * @author seer
 * @date 2018/5/31 16:34
 */
@Component
@Order(1)
@Slf4j
public class SpringApplicationRunner implements ApplicationRunner {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.warn("Application started, init extend params begin.");

        MybatisRedisCache.setRedisTemplate(redisTemplate);
        log.warn("mybatis redis cache init done.");

        log.warn("Application started, init extend params done.");
    }
}
