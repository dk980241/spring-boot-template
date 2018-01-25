package online.yuyanjia.template;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.Arrays;

/**
 * 启动类
 *
 * @author seer
 * @date 2018/1/25 15:29
 */
@SpringBootApplication
@MapperScan("online.yuyanjia.template.mobile.mapper")
public class Application implements CommandLineRunner {
    private static Logger LOGGER = LogManager.getLogger(Application.class);

    @Override
    public void run(String... strings) throws Exception {
        LOGGER.warn("===== spring boot web 服务启动 =====");
        LOGGER.warn("===== 启动参数: {} =====", Arrays.asList(strings));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * @SpringBootApplication 等价于同时使用@Configuration @EnableAutoConfiguration @ComponentScan
     * @MapperScan 扫描mapper
     * @ServletComponentScan 扫描servlet
     */
}
